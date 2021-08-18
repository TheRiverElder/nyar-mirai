package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader

class StringArgumentNode<E>(
    key: String,
    val allowEmpty: Boolean = true,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null
) : ArgumentNode<E>(key, default = default, processor = processor) {

    override fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): String? {
        if (!reader.hasMore()) return null
        val s = reader.read().getString()
        return if (this.allowEmpty) s else if (s.isEmpty()) null else s
    }

    override fun getValueHint(): String = "字符串"
}