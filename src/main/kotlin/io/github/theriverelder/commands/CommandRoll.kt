package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.math.DICE_1D100
import io.github.theriverelder.util.math.Dice
import io.github.theriverelder.util.command.argument.*

fun commandRoll(): LiteralArgumentNode<CommandEnv> {
    return command("roll", "r", "投掷") {
        dice("dice", default = DICE_1D100) {
            end { output ->
                val dice: Dice = getOrDefault("dice", DICE_1D100)
                output.println("$dice -> ${dice.roll()}")
            }
        }
    }
}