package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.argument.*

fun commandCalculate(): LiteralArgumentNode<CommandEnv> {
    return command("calculate", "calc") {
        expression("value") {
            end { output ->
                val value: Number = getOrDefault<Number>("value", 0)
                output.println(value)
            }
        }
    }
}