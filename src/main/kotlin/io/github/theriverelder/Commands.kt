package io.github.theriverelder

import io.github.theriverelder.data.*
import io.github.theriverelder.util.DICE_1D100
import io.github.theriverelder.util.Dice
import io.github.theriverelder.util.check.CheckHardness
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.genUid
import okhttp3.internal.concat
import java.io.PrintWriter
import kotlin.system.exitProcess

fun matchName(s: String): Boolean {
    if (s.isEmpty()) return false

    for (ch in s) {
        if (!Character.isLetter(ch) && ch != '·') return false
    }
    return true
}

val NAME_MATCHER: (String) -> Boolean = { matchName(it) }

fun registerBuiltinCommands(dispatcher: NyarCommandDispatcher<CommandEnv>, doAddDangerousCommands: Boolean = false) {
    if (doAddDangerousCommands) {
        //#region exit
        dispatcher.register(
            literal(literals = arrayOf("exit")).addArguments(
                number("status", true, default = 0).add(
                    end { exitProcess(getOrDefault<Int>("status", 0)) }
    //                end { it.println("Exit with code: ${getOrDefault<Int>("status", 0)}") }
                )
            )
        )
        //#endregion
    }

    //#region roll
    dispatcher.register(
        literal(literals = arrayOf("roll", "r")).addArguments(
            dice("dice", default = DICE_1D100).add(
                end { output ->
                    val dice: Dice = getOrDefault("dice", DICE_1D100)
                    output.println("$dice -> ${dice.roll()}")
                }
            )
        )
    )
    //#endregion

    //#region calculate
    dispatcher.register(
        literal(literals = arrayOf("calculate", "calc")).addArguments(
            expression("value").add(
                end { output ->
                    val value: Number = getOrDefault<Number>("value", 0)
                    output.println(value)
                }
            )
        )
    )
    //#endregion

    //#region entity
    dispatcher.register(
        literal(literals = arrayOf("entity", "e")).addArguments(
            literal(literals = arrayOf("create", "c")).add(
                string(key = "name", matcher = NAME_MATCHER).add(
                    literal(
                        "doControl",
                        arrayOf("control"),
                        default = false,
                        processor = { _, _, _ -> true }
                    ).add(
                        end { output ->
                            val group: GameGroup = getGameGroup(env.groupUid, output) ?: return@end Unit
                            val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                            val name: String = getOrDefault("name", "UnnamedEntity")
                            val doControl: Boolean = getOrDefault("doControl", false)
                            val entity = Entity(genUid(), game, name)
                            game.entities.register(entity)
                            if (doControl) {
                                group.setControl(env.playerUid, entity)
                            }
                            output.println("Entity created: ${entity.name}(uid: ${entity.uid})")
                        }
                    )
                )
            )
        ).addArguments(
            literal(literals = arrayOf("control", "ctrl")).add(
                string(key = "name", matcher = NAME_MATCHER).add(
                    end { output ->
                        val group: GameGroup = getGameGroup(env.groupUid, output) ?: return@end Unit
                        val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                        val name: String = getOrDefault("name", "UnnamedEntity")
                        val entity: Entity = getEntity(game, name, output) ?: return@end Unit
                        group.setControl(env.playerUid, entity)
                        output.println("Entity Controlled: ${entity.name}(#${entity.uid}) by #${env.playerUid}")
                    }
                )
            )
        ).addArguments(
            string("entity").add(
                literal(literals = arrayOf("property", "prop", "p")).add(
                    string("propName").add(
                        literal(literals = arrayOf("remove", "rm")).add(
                            string("propName", matcher = NAME_MATCHER) .add(
                                end { output ->
                                    val game: Game = getGame(env.groupUid, output) ?: return@end Unit

                                    val entityStr = get<String>("entity") ?: return@end Unit
                                    val propName: String = get<String>("propName") ?: return@end Unit

                                    val entity: Entity = getEntity(game, entityStr, output) ?: return@end Unit
                                    val prop: PropertyValue = getEntityProperty(game, entity, propName, output) ?: return@end Unit

                                    entity.removeProperty(prop.type)
                                    output.println("Property removed from ${entity.name}: $prop")
                                }
                            )
                        )
                    ).add(
                        literal(key = "operation", literals = arrayOf("set", "add", "minus", "=", "+", "-")).add(
                            number(key = "value").add(
                                end { output ->
                                    val game: Game = getGame(env.groupUid, output) ?: return@end Unit

                                    val entityStr = get<String>("entity") ?: return@end Unit
                                    val propName: String = getOrDefault("propName", "")
                                    val value: Number = getOrDefault<Number>("value", 0)
                                    val operation = get<String>("operation")

                                    val entity: Entity = getEntity(game, entityStr, output) ?: return@end Unit
                                    val prop: PropertyValue = getEntityProperty(game, entity, propName, output) ?: return@end Unit

                                    val prev = prop.value
                                    when (operation) {
                                        "set", "=" -> entity.setProperty(prop.type, value.toInt())
                                        "add", "+" -> entity.changeProperty(prop.type, value.toInt())
                                        "minus", "-" -> entity.changeProperty(prop.type, -value.toInt())
                                    }

                                    output.println("Property $operation $propName of ${entity.name}: $prev -> ${prop.value}")
                                }
                            )
                        )
                    )
                )
            )
        )
    )
    //#endregion

    //#region property
    dispatcher.register(
        literal(literals = arrayOf("property", "prop", "p")).addArguments(
            string(key = "name", matcher = NAME_MATCHER).add(
                literal(literals = arrayOf("remove", "rm")).add(
                    end { output ->
                        val group: GameGroup = getGameGroup(env.groupUid, output) ?: return@end Unit
                        val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                        val self: Entity = getEntity(group, game, env.playerUid, output) ?: return@end Unit

                        val propName: String = get<String>("name") ?: return@end Unit

                        val prop: PropertyValue = getEntityProperty(game, self, propName, output) ?: return@end Unit

                        self.removeProperty(prop.type)
                        output.println("Property removed from ${self.name}: $prop")
                    }
                )
            ).add(
                literal(key = "operation", literals = arrayOf("set", "add", "minus", "=", "+", "-")).add(
                    number(key = "value").add(
                        end { output ->
                            val group: GameGroup = getGameGroup(env.groupUid, output) ?: return@end Unit
                            val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                            val self: Entity = getEntity(group, game, env.playerUid, output) ?: return@end Unit

                            val propName: String = getOrDefault("name", "")
                            val value: Number = getOrDefault<Number>("value", 0)
                            val operation = get<String>("operation")

                            val prop: PropertyValue = getEntityProperty(game, self, propName, output) ?: return@end Unit
                            val prev = prop.value
                            when (operation) {
                                "set", "=" -> self.setProperty(prop.type, value.toInt())
                                "add", "+" -> self.changeProperty(prop.type, value.toInt())
                                "minus", "-" -> self.changeProperty(prop.type, -value.toInt())
                            }
                            output.println("Property $operation $propName of ${self.name}: $prev -> ${prop.value}")
                        }
                    )
                )
            )
        )
    )
    //#endregion

    val hardnessMap = mapOf(
        Pair("normal", CheckHardness.NORMAL),
        Pair("hard", CheckHardness.HARD),
        Pair("extreme", CheckHardness.EXTREME),

        Pair("n", CheckHardness.NORMAL),
        Pair("h", CheckHardness.HARD),
        Pair("e", CheckHardness.EXTREME),
    )

    //#region check
    dispatcher.register(
        literal(literals = arrayOf("check", "c")).addArguments(
            string("property").add(
                literal(
                    "hardness",
                    arrayOf("normal", "hard", "extreme", "n", "h", "e"),
                    default = CheckHardness.NORMAL,
                    processor = { _, arg, _ -> hardnessMap[arg] }
                ).add(
                    end { output ->
                        val group: GameGroup = getGameGroup(env.groupUid, output) ?: return@end Unit
                        val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                        checkEntityProperty(
                            game,
                            getEntity(group, game, env.playerUid, output) ?: return@end Unit,
                            get<String>("property") ?: return@end Unit,
                            get<CheckHardness>("hardness") ?: return@end Unit,
                            output,
                        )
                    }
                )
            )
        )
    )
    //#endregion

    //#region game
    dispatcher.register(
        literal(literals = arrayOf("game")).addArguments(
            literal(literals = arrayOf("register_property", "rp")).add(
                string("propName").add(
                    number("defaultValue", default = 0).add(
                        number("defaultLimit", default = -1).add(
                            end { output ->
                                val game: Game = getGame(env.groupUid, output) ?: return@end Unit
                                val propName = get<String>("propName") ?: return@end Unit
                                val defaultValue = get<Number>("defaultValue") ?: return@end Unit
                                val defaultLimit = get<Number>("defaultLimit") ?: return@end Unit

                                val prop: PropertyType = game.registerProperty(propName, defaultValue.toInt(), defaultLimit.toInt())
                                output.println("Registered property: ${prop.name}(#${prop.code}, ${prop.defaultValue}/${prop.defaultLimit})")
                                true
                            }
                        )
                    )
                )
            )
        ).addArguments(
            literal("operation", arrayOf("create", "c", "use", "u", "open", "o")).add(
                string(key = "name").add(
                    end { output ->

                        var group: GameGroup? = GAME_GROUPS[env.groupUid]
                        if (group == null) {
                            group = GameGroup(env.groupUid)
                            GAME_GROUPS.register(group)
                        }

                        val name = getOrDefault("name", "UnnamedGame")
                        val game: Game = when (val operation = getOrDefault<String>("operation", "unknown")) {
                            "create", "c" -> Game(genUid(), name)
                            "use", "u" -> searchGame(name, output) ?: return@end Unit
                            "open", "o" -> searchGame(name, output) ?: Game(genUid(), name)
                            else -> {
                                output.println("Unknown operation: $operation")
                                return@end Unit
                            }
                        }

                        if (!GAMES.hasItem(game)) {
                            GAMES.register(game)
                        }

                        group.game = game

                        output.println("Game created: ${game.name}(#${game.uid})")
                    }
                )
            )
        )
    )
    //#endregion

    //#region execute_file
    dispatcher.register(
        literal(literals = arrayOf("execute_file", "ef")).addArguments(
            string("filePath").add(
                end { output ->
                    val filePath = get<String>("filePath") ?: return@end Unit
                    val file = SaveConfig.getFile("script", filePath)
                    if (!file.exists()) {
                        output.println("File not found: $filePath")
                        return@end Unit
                    }
                    val lines: List<String> = file.readLines()
                    lines.forEach { executeCommand(it, dispatcher, env, output) }
                }
            )
        )
    )
    //#endregion
}

fun executeCommand(raw: String, dispatcher: NyarCommandDispatcher<CommandEnv>, env: CommandEnv, output: PrintWriter) {
    val result = dispatcher.dispatch(raw, env)
    if (result.succeed) {
        result.best?.execute(output)
    } else {
        output.println("${result.hints.size} Hint(s):")
        val displayHints: Array<String> =
            if (result.hints.size < 5) result.hints
            else result.hints.copyOfRange(0, 4).concat("...")
        displayHints.forEach { output.println("  $it") }
    }
    output.flush()
}
