package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.SafeReader
import java.lang.StringBuilder

val QUOTE_PAIRS = mapOf(
    Pair('"', '"'),
    Pair('\'', '\''),
    Pair('“', '”'),
    Pair('‘', '’'),
)

class StringArgumentNode<E>(
    key: String,
    val matcher: (String) -> Boolean = { true },
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null
) : ArgumentNode<E>(key, default = default, processor = processor) {
    override fun doParse(reader: SafeReader, buffer: ChainArgumentBuffer): String? {
        val s = reader.readString() ?: return null
        return if (matcher(s)) s else null
    }

    override fun getValueHint(): String = "string"
}

fun SafeReader.readString(): String? {
    if (hasMore() && peek() in QUOTE_PAIRS.keys) {
        val endQuote = QUOTE_PAIRS[read()]
        val builder = StringBuilder()
        var escaped = false

        while (hasMore()) {
            val ch = read()
            if (escaped) {
                val r = when (ch) {
                    'n' -> '\n'
                    't' -> '\t'
                    'r' -> '\r'
                    '\\' -> '\\'
                    else -> ch
                }
                builder.append(r)
                escaped = false
            }
            when (ch) {
                '\\' -> escaped = true
                endQuote -> return builder.toString()
                else -> builder.append(ch)
            }
        }
        return null
    }

    return readWord()
}