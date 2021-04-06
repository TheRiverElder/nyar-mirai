package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.SafeReader
import io.github.theriverelder.util.command.CommandCollection
import io.github.theriverelder.util.command.HintCollection

open class RootArgumentParser<E> : ArgumentParser<E> {

    override fun parse(reader: SafeReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean {
        while (reader.hasMore() && Character.isWhitespace(reader.peek())) {
            reader.read()
        }

        val checkPoint = reader.pointer
        for (command in commands) {
            val parseResult = command.doParse(reader, buffer)
            if (parseResult != null) {
                reader.pointer = checkPoint
                if (command.parse(reader, buffer, res, hints, env)) {
                    return true
                }
            }
            reader.pointer = checkPoint
        }
        return false
    }

    fun parse(reader: SafeReader, res: CommandCollection, hints: HintCollection, env: E) {
        this.parse(reader, ChainArgumentBuffer(null, "", Unit), res, hints, env)
    }

    private val commands: MutableList<LiteralArgumentNode<E>> = ArrayList()
    private val commandHeadMap: MutableMap<String, LiteralArgumentNode<E>> = HashMap()

    fun add(command: LiteralArgumentNode<E>): RootArgumentParser<E> {
        commands.add(command)
        for (head in command.literals) {
            commandHeadMap[head] = command
        }
        return this
    }

    fun getAllCommands(): Array<LiteralArgumentNode<E>> = ArrayList(commands).toTypedArray()

    fun getCommand(head: String): LiteralArgumentNode<E>? = commandHeadMap[head]
}