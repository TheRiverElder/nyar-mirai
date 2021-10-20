package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader
import io.github.theriverelder.util.command.SafeReader
import io.github.theriverelder.util.math.*
import java.lang.Integer.parseInt

class DiceArgumentNode<E>(key: String, default: Dice? = null, processor: ArgumentProcessor<E>? = null) : ArgumentNode<E>(key, default = default, processor = processor) {

    override fun doParse(argReader: RawArgumentReader, buffer: ChainArgumentBuffer): Dice? {
        if (!argReader.hasMore()) return null

        return parseDice((argReader.read().getString()))
    }

    override fun getValueHint(): String = "骰子"
}

fun parseDiceItem(reader: SafeReader, isFirst: Boolean): Dice? {
    var negative = false

    if (reader.hasMore() && (reader.peek() == '+' || reader.peek() == '-')) {
        negative = reader.read() == '-'
    } else if (!isFirst) {
        return null
    }


    var numberStart = reader.pointer
    while (reader.hasMore() && Character.isDigit(reader.peek())) {
        reader.read()
    }
    if (numberStart == reader.pointer) return null

    val v1: Int = parseInt(reader.slice(numberStart))

    if (!reader.hasMore() || reader.peek().toLowerCase() != 'd') {
        val content = ConstantDice(v1)
        return if (negative) NegativeDice(content) else content
    }
    val checkPoint = reader.pointer
    reader.read()
    numberStart = reader.pointer
    while (reader.hasMore() && Character.isDigit(reader.peek())) {
        reader.read()
    }
    if (numberStart == reader.pointer) {
        reader.pointer = checkPoint
        val content = ConstantDice(v1)
        return if (negative) NegativeDice(content) else content
    }

    val v2: Int = parseInt(reader.slice(numberStart))

    val content = RandomDice(v1, v2)
    return if (negative) NegativeDice(content) else content
}

fun parseDice(expr: String): Dice? {
    val reader = SafeReader(expr)
    var checkPoint = reader.pointer
    var dice: Dice? = parseDiceItem(reader, true)
    val items: MutableList<Dice> = ArrayList()
    while (dice != null) {
        items.add(dice)
        checkPoint = reader.pointer
        dice = parseDiceItem(reader, false)
    }
    reader.pointer = checkPoint
    if (items.size == 0) return null

    return if (items.size == 1) items[0] else ComplexDice(items.toTypedArray())
}