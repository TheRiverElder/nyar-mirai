package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.SafeReader
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.lang.Long.parseLong

class NumberArgumentNode<E>(
    key: String,
    private val digit: Boolean = false,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null
) : ArgumentNode<E>(key, default = default, processor = processor) {
    override fun doParse(reader: SafeReader, buffer: ChainArgumentBuffer): Number? = reader.readNumber(digit)

    override fun getValueHint(): String = if (digit) "digit_number" else "number"
}

fun SafeReader.readNumber(digit: Boolean = false): Number? {
    val start: Int = pointer

    if (hasMore() && peek() == '-') {
        read()
    }

    var digitPartIsEmpty = true
    while (hasMore() && Character.isDigit(peek())) {
        read()
        digitPartIsEmpty = false
    }

    return if (!digit && hasMore() && peek() == '.') {
        read()
        var fractionPartIsEmpty = true
        while (hasMore() && Character.isDigit(peek())) {
            read()
            fractionPartIsEmpty = false
        }
        if (!fractionPartIsEmpty) parseDouble(slice(start)) else null
    } else {
        if (!digitPartIsEmpty) parseLong(slice(start)) else null
    }
}