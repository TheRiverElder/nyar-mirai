package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader
import io.github.theriverelder.util.command.SafeReader

class LiteralArgumentNode<E>(
    key: String = "",
    val literals: Array<out String>,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null
) : ArgumentNode<E>(key, default = default, processor = processor) {

    override fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): Any? {
        if (!reader.hasMore()) return null
        val result: String = reader.read().getString()
        return if (result.isNotBlank() && result in literals) result else null
    }

    override fun getHint(): String = if (literals.size == 1) literals[0] else "<${getValueHint()}>"

    override fun getValueHint(): String = literals.joinToString("|")

    fun addArguments(node: ArgumentParser<E>): LiteralArgumentNode<E> {
        super.add(node)
        return this
    }

}