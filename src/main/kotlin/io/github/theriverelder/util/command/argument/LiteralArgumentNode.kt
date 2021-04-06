package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.SafeReader

class LiteralArgumentNode<E>(key: String = "", val literals: Array<String>, default: Any? = null, processor: ArgumentProcessor<E>? = null) : ArgumentNode<E>(key, default = default, processor = processor) {
    override fun doParse(reader: SafeReader, buffer: ChainArgumentBuffer): Any? {
        val result: String? = reader.readWord()
        return if (result != null && result in literals) result else null
    }

    override fun getValueHint(): String = literals.joinToString("|")

    fun addArguments(node: ArgumentParser<E>): LiteralArgumentNode<E> {
        super.add(node)
        return this
    }

}