package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.CommandCollection
import io.github.theriverelder.util.command.HintCollection
import io.github.theriverelder.util.command.RawArgumentReader

abstract class ArgumentNode<E>(
    private val key: String? = null,
    private val hintKey: String? = null,
    private val default: Any? = null,
    private val processor: ArgumentProcessor<E>? = null,
) : ArgumentParser<E> {

    private fun process(env: E, arg: Any, hints: HintCollection): Any? = if (processor != null) processor.invoke(env, arg, hints) else arg

    override fun parse(reader: RawArgumentReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean {
        val start: Int = reader.pointer

        var arg: Any? = doParse(reader, buffer)
        arg = if (arg != null) {
            process(env, arg, hints)
        } else {
            hints.add("${reader.slice().replace(Regex("\\s*$"), "")} ${getHint()}")
            default
        }

        if (arg == null) return false

        val thisArgument = setArgument(buffer, arg)

        var result = false
        val checkPoint = reader.pointer
        for (nextParser in nextParsers) {
            reader.pointer = checkPoint
            if (nextParser.parse(reader, thisArgument, res, hints, env)) {
                result = true
            }
        }
        return result
    }

    abstract fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): Any?

    private val nextParsers: MutableList<ArgumentParser<E>> = ArrayList()

    fun add(node: ArgumentParser<E>): ArgumentNode<E> {
        nextParsers.add(node)
        return this
    }

    private fun setArgument(buffer: ChainArgumentBuffer, value: Any): ChainArgumentBuffer =
        if (key != null) buffer.derive(key, value) else buffer

    open fun getHint(): String {
        return "<" + (if (hintKey != null) hintKey + ":" + getValueHint() else getValueHint()) + ">"
    }

    override fun getHelp(proceeding: String, res: MutableList<String>) {
        val newProceeding = proceeding + " " + getHint()
        nextParsers.forEach { it.getHelp(newProceeding, res) }
    }

    abstract fun getValueHint(): String
}