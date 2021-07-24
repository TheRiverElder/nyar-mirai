package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.expression
import io.github.theriverelder.util.command.argument.literal

fun commandCalculate(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("calculate", "calc")).addArguments(
        expression("value").add(
            end { output ->
                val value: Number = getOrDefault<Number>("value", 0)
                output.println(value)
            }
        )
    )
}