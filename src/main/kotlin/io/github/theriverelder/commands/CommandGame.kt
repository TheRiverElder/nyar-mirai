package io.github.theriverelder.commands

import io.github.theriverelder.*
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.genUid

fun commandGame(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("game", "团")).addArguments(
        literal("operation", arrayOf("create", "c", "use", "u", "open", "o")).add(
            string(key = "name").add(
                end { output ->

                    val group: GameGroup = env.getOrCreateGameGroup()

                    val name: String = get("name")
                    val game: Game = when (val operation: String = get("operation")) {
                        "create", "c" -> Game(genUid(), name)
                        "use", "u" -> searchGame(name)
                        "open", "o" -> trySearchGame(name) ?: Game(genUid(), name)
                        else -> throw Exception("未知操作：$operation")
                    }

                    GAMES.register(game)

                    group.gameUid = game.uid

                    output.println("已绑定：群(#${group.uid}) <=> 团(#${game.uid})")
                }
            )
        )
    ).addArguments(
        literal(literals = arrayOf("list", "ls")).add(
            end { output ->
                output.println("共找到${GAMES.size}个团：")
                GAMES.getAll().forEach {
                    output.println("${it.name}#${it.uid}：${it.getInvolvedEntities().size}人")
                }
            }
        )
    )
}