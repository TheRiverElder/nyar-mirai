package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader
import java.lang.Double.parseDouble
import java.lang.Long.parseLong

class NumberArgumentNode<E>(
    key: String,
    private val digit: Boolean = false,
    default: Number? = null,
    processor: ArgumentProcessor<E>? = null
) : ArgumentNode<E>(key, default = default, processor = processor) {

    override fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): Number? {
        if (!reader.hasMore()) return null
        val rawArgumentString = reader.read().getString()
        return try {
            if (digit) parseLong(rawArgumentString) else parseDouble(rawArgumentString)
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun getValueHint(): String = if (digit) "整数" else "数字"
}

//fun SafeReader.readNumber(digit: Boolean = false): Number? {
//    val start: Int = pointer
//
//    if (hasMore() && peek() == '-') {
//        read()
//    }
//
//    var digitPartIsEmpty = true
//    while (hasMore() && Character.isDigit(peek())) {
//        read()
//        digitPartIsEmpty = false
//    }
//
//    return if (!digit && hasMore() && peek() == '.') {
//        read()
//        var fractionPartIsEmpty = true
//        while (hasMore() && Character.isDigit(peek())) {
//            read()
//            fractionPartIsEmpty = false
//        }
//        if (!fractionPartIsEmpty) parseDouble(slice(start)) else null
//    } else {
//        if (!digitPartIsEmpty) parseLong(slice(start)) else null
//    }
//}