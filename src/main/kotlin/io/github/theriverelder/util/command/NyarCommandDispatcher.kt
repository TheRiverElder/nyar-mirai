package io.github.theriverelder.util.command

import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.RootArgumentParser

class DispatchResult(
    val input: String,
    val commands: Array<BufferedCommand<*>>,
    val hints: Array<String>,
    val succeed: Boolean,
    val best: BufferedCommand<*>?,
)

class NyarCommandDispatcher<E> {

    private val rootParser: RootArgumentParser<E> = RootArgumentParser()

    fun register(command: LiteralArgumentNode<E>): NyarCommandDispatcher<E> {
        rootParser.add(command)
        return this
    }

    fun getAllCommands(): Array<LiteralArgumentNode<E>> = rootParser.getAllCommands()

    fun getCommand(head: String): LiteralArgumentNode<E>? = rootParser.getCommand(head)

    fun dispatch(raw: String, env: E): DispatchResult {
        val reader = SafeReader(raw)
        val res: MutableList<BufferedCommand<*>> = ArrayList()
        val hints: MutableList<String> = ArrayList()
        rootParser.parse(reader, res, hints, env)
        return DispatchResult(
            reader.slice(),
            res.toTypedArray(),
            hints.toTypedArray(),
            res.size > 0,
            if (res.size > 0) res[0] else null,
        )
    }

}