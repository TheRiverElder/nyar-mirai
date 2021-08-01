package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.CommandCollection
import io.github.theriverelder.util.command.HintCollection
import io.github.theriverelder.util.command.RawArgumentReader

open class RootArgumentParser<E> : ArgumentParser<E> {

    override fun parse(reader: RawArgumentReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean {
        var r = true
        val checkPoint = reader.pointer
        for (command in commands + otherCommands) {
            reader.pointer = checkPoint
            val commandParsedSuccess = command.parse(reader, buffer, res, hints, env)
            r = r || commandParsedSuccess
        }
        return r
    }

    fun parse(reader: RawArgumentReader, res: CommandCollection, hints: HintCollection, env: E) {
        this.parse(reader, ChainArgumentBuffer(null, "", Unit), res, hints, env)
    }

    private val commands: MutableList<LiteralArgumentNode<E>> = ArrayList()
    private val commandHeadMap: MutableMap<String, LiteralArgumentNode<E>> = HashMap()

    private val otherCommands: MutableList<ArgumentNode<E>> = ArrayList()

    fun add(command: LiteralArgumentNode<E>): RootArgumentParser<E> {
        commands.add(command)
        for (head in command.literals) {
            commandHeadMap[head] = command
        }
        return this
    }

    fun add(command: ArgumentNode<E>): RootArgumentParser<E> {
        otherCommands.add(command)
        return this
    }

    fun getAllCommands(): Array<LiteralArgumentNode<E>> = ArrayList(commands).toTypedArray()

    fun getCommand(head: String): LiteralArgumentNode<E>? = commandHeadMap[head]

    override fun getHelp(proceeding: String, res: MutableList<String>) {
        commands.forEach { it.getHelp(proceeding, res) }
        otherCommands.forEach { it.getHelp(proceeding, res) }
    }
}