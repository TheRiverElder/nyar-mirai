package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.SafeReader
import io.github.theriverelder.util.command.CommandCollection
import io.github.theriverelder.util.command.HintCollection

interface ArgumentParser<E> {
    fun parse(reader: SafeReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean
}

abstract class ArgumentNode<E>(
    private val key: String? = null,
    private val hintKey: String? = null,
    private val default: Any? = null,
    private val processor: ArgumentProcessor<E>? = null,
) : ArgumentParser<E> {

    private fun process(env: E, arg: Any, hints: HintCollection): Any? = if (processor != null) processor.invoke(env, arg, hints) else arg

    override fun parse(reader: SafeReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean {
        reader.skipWhitespace()
        val start: Int = reader.pointer
        var arg: Any? = doParse(reader, buffer)
        if (arg == null) {
            reader.pointer = start
            hints.add("/${reader.slice().replace(Regex("\\s*$"), "")} <${getHint()}>")
            if (default == null) return false
            arg = default
        } else {
            arg = process(env, arg, hints)
        }

        if (arg == null) return false

        val thisArgument = setArgument(buffer, arg)

        reader.skipWhitespace()
        var result = false
        val checkPoint = reader.pointer
        for (nextParser in nextParsers) {
            if (nextParser.parse(reader, thisArgument, res, hints, env)) {
                result = true
            }
            reader.pointer = checkPoint
        }
        return result
    }

    abstract fun doParse(reader: SafeReader, buffer: ChainArgumentBuffer): Any?

    private val nextParsers: MutableList<ArgumentParser<E>> = ArrayList()

    fun add(node: ArgumentParser<E>): ArgumentNode<E> {
        nextParsers.add(node)
        return this
    }

    fun setArgument(buffer: ChainArgumentBuffer, value: Any): ChainArgumentBuffer =
        if (key != null) buffer.derive(key, value) else buffer

    fun getHint(): String {
        return if (hintKey != null) hintKey + ":" + getValueHint() else getValueHint()
    }

    abstract fun getValueHint(): String
}

fun SafeReader.skipWhitespace() {
    while (hasMore() && Character.isWhitespace(peek())) {
        read()
    }
}