package io.github.theriverelder.commands

import io.github.theriverelder.*
import io.github.theriverelder.data.*
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.math.genUid

//fun matchName(s: String): Boolean = s.isNotEmpty() && s.all { Character.isLetter(it) || it != '·' }
//
//val NAME_MATCHER: ArgumentProcessor<CommandEnv> = { _, arg, _ -> matchName(arg as String) }

fun commandEntity(): LiteralArgumentNode<CommandEnv> {
    return command("entity", "e", "实体") {
        literal("create", "c", "创建") {
            string("name") {
                end { output ->
                    val game: Game = env.getGame()
                    val name: String = get("name")

                    val entity = Entity(genUid(), name)
                    ENTITIES.register(entity)

                    game.setControl(env.playerUid, entity.uid)

                    output.println("实体已创建：${entity.name}(#${entity.uid})")
                    output.println("已绑定：玩家(${env.playerUid}) <=> 实体(${entity.name}#${entity.uid})")
                }
            }
        }
        literal("control", "ctrl", "控制") {
            string("name") {
                end { output ->
                    val game: Game = env.getGame()
                    val name: String = get("name")
                    val entity: Entity = getEntity(game, name)
                    game.setControl(env.playerUid, entity.uid)
                    output.println("Attached: Player(${env.playerUid}) <=> Entity(${entity.name}#${entity.uid})")
                }
            }
        }
        literal("list", "列表") {
            end { output ->
                val game: Game = getGame(env.groupUid)
                val entities = game.getInvolvedEntities()
                output.println("团${game.name}中绑定了${entities.size}个实体：")
                entities.forEach { output.println(it.name) }
            }
        }
    }
}