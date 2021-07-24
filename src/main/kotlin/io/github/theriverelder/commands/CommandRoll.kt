package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.DICE_1D100
import io.github.theriverelder.util.Dice
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.dice
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.literal

fun commandRoll(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("roll", "r", "投掷")).addArguments(
        dice("dice", default = DICE_1D100).add(
            end { output ->
                val dice: Dice = getOrDefault("dice", DICE_1D100)
                output.println("$dice -> ${dice.roll()}")
            }
        )
    )
}