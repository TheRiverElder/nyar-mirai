package io.github.theriverelder.commands

import io.github.theriverelder.checkEntityProperty
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.getEntity
import io.github.theriverelder.util.check.CheckHardness
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.literal
import io.github.theriverelder.util.command.argument.string

fun commandCheck(): LiteralArgumentNode<CommandEnv> {

    val hardnessMap = mapOf(
        Pair("normal", CheckHardness.NORMAL),
        Pair("hard", CheckHardness.HARD),
        Pair("extreme", CheckHardness.EXTREME),

        Pair("n", CheckHardness.NORMAL),
        Pair("h", CheckHardness.HARD),
        Pair("e", CheckHardness.EXTREME),
    )

    return literal(literals = arrayOf("check", "c", "检定")).addArguments(
        string("property").add(
            literal(
                "hardness",
                arrayOf("normal", "hard", "extreme", "n", "h", "e"),
                default = CheckHardness.NORMAL,
                processor = { _, arg, _ -> hardnessMap[arg] }
            ).add(
                end { output ->
                    val group: GameGroup = env.getOrCreateGameGroup()
                    val game: Game = env.getGame()
                    checkEntityProperty(
                        getEntity(game, env.playerUid),
                        get("property"),
                        getOrDefault("hardness", CheckHardness.NORMAL),
                        output,
                    )
                }
            )
        )
    )
}
