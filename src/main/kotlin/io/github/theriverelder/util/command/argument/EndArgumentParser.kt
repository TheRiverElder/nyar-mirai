package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.*

class EndArgumentParser<E>(private val executor: NyarCommandExecutor<E>) : ArgumentParser<E> {

    override fun parse(reader: SafeReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean {
        if (!reader.hasMore()) {
            res.add(BufferedCommand(buffer.flatten(env), executor))
            return true
        }
        hints.add(reader.slice())
        return false
    }

}

fun <E> ChainArgumentBuffer.flatten(env: E): NyarCommandContext<E> {
    val context = NyarCommandContext(env)
    var buffer: ChainArgumentBuffer? = this;
    while (buffer != null) {
        context.put(buffer.key, buffer.value)
        buffer = buffer.parent
    }
    return context
}