package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader

class StringListArgumentNode<E>(
    key: String,
    private val allowEmpty: Boolean,
) : ArgumentNode<E>(key) {
    override fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): Any? {
        val result: MutableList<String> = ArrayList()
        while (reader.hasMore()) {
            result.add(reader.read().getString())
        }
        return if (!allowEmpty && result.size == 0) null else result
    }

    override fun getValueHint(): String = "string[]"
}