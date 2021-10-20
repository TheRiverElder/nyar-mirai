package io.github.theriverelder.util.math

import java.lang.Integer.max
import kotlin.random.Random

val DICE_0D0: Dice = RandomDice(0, 0)
val DICE_1D100: Dice = RandomDice(1, 100)
val DICE_3D6: Dice = RandomDice(3, 6)

interface Dice {
    fun roll(): Int
}

class ComplexDice(val items: Array<Dice>) : Dice {
    override fun roll(): Int {
        var sum = 0
        items.forEach { sum += it.roll() }
        return sum
    }

    override fun toString(): String =
        items.joinToString("") { if (it !is NegativeDice) "+$it" else it.toString() }
}

class NegativeDice(val content: Dice) : Dice {
    override fun roll(): Int = -content.roll()

    override fun toString(): String = "-$content"
}

class ConstantDice(val value: Int) : Dice {
    override fun roll(): Int = value

    override fun toString(): String = value.toString()
}

class RandomDice(val count: Int, val faces: Int) : Dice {
    override fun roll(): Int {
        var sum = 0
        for (i in 0 until count) {
            sum += ((Random.nextDouble() * faces).toInt() + 1).coerceIn(0, max(0, faces))
        }
        return sum
    }

    override fun toString(): String = "${count}d${faces}"
}