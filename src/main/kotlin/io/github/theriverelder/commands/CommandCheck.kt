package io.github.theriverelder.commands

import io.github.theriverelder.checkEntityProperty
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Game
import io.github.theriverelder.getEntity
import io.github.theriverelder.util.check.CheckHardness
import io.github.theriverelder.util.command.argument.*

val HARDNESS_MAP = mapOf(
    Pair("normal", CheckHardness.NORMAL),
    Pair("hard", CheckHardness.HARD),
    Pair("extreme", CheckHardness.EXTREME),

    Pair("n", CheckHardness.NORMAL),
    Pair("h", CheckHardness.HARD),
    Pair("e", CheckHardness.EXTREME),

    Pair("普通", CheckHardness.NORMAL),
    Pair("困难", CheckHardness.HARD),
    Pair("极难", CheckHardness.EXTREME),
)

fun commandCheck(): LiteralArgumentNode<CommandEnv> {

    return command("check", "c", "检定") {
        string("property") {
            options(
                "hardness",
                *HARDNESS_MAP.keys.toTypedArray(),
                default = CheckHardness.NORMAL,
                processor = { _, arg, _ -> HARDNESS_MAP[arg] }
            ) {
                end { output ->
                    val game: Game = env.getGame()
                    checkEntityProperty(
                        getEntity(game, env.playerUid),
                        get("property"),
                        getOrDefault("hardness", CheckHardness.NORMAL),
                        output,
                    )
                }
            }
        }
    }
}
