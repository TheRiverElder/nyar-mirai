package io.github.theriverelder

import io.github.theriverelder.commands.*
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.math.Dice
import io.github.theriverelder.util.check.CheckHardness
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import okhttp3.internal.concat
import java.io.PrintWriter

fun registerBuiltinCommands(dispatcher: NyarCommandDispatcher<CommandEnv>, doAddDangerousCommands: Boolean = false) {
    if (doAddDangerousCommands) {
        dispatcher.register(commandSudo(dispatcher))
    }

    dispatcher.register(commandSanCheck())
    dispatcher.register(commandRecord())
    dispatcher.register(commandHelp(dispatcher))
    dispatcher.register(commandAlias())
    dispatcher.register(commandRoll())
    dispatcher.register(commandCalculate())
    dispatcher.register(commandEntity())
    dispatcher.register(commandProperty())
    dispatcher.register(commandCheck())
    dispatcher.register(commandGame())
    dispatcher.register(commandCoc())
    dispatcher.register(commandSt())
    dispatcher.register(commandTodayFortune())

    dispatcher.register(DiceArgumentNode("dice")) {
        end { output ->
            val dice = get<Dice>("dice")
            output.println("$dice -> ${dice.roll()}")
        }
    }

//    dispatcher.register(StringArgumentNode("head")) {
//        stringList("arguments") {
//            end { output ->
//                val head = get<String>("head")
//                val arguments = get<List<String>>("arguments")
//                val pattern = env.getGameGroup().tryGetCommandAlias(head) ?: throw
//            }
//        }
//    }

    fun propertyExist(env: CommandEnv, arg: String): Any? {
        val entity = env.tryGetEntity() ?: return null
        return if (entity.hasProperty(arg)) arg else null
    }

    dispatcher.register(StringArgumentNode("propName",
        false,
        processor = { env, arg, _ -> propertyExist(env, arg as String)}
    )) {
        options(
            "hardness",
            *HARDNESS_MAP.keys.toTypedArray(),
            default = CheckHardness.NORMAL,
            processor = { _, arg, _ -> HARDNESS_MAP[arg] }
        ) {
            string("targetName", default = "") {
                end { output ->
                    val targetName: String = getOrDefault("targetName", "")
                    val property: String = get("property")
                    val hardness: CheckHardness = getOrDefault("hardness", CheckHardness.NORMAL)

                    checkEntityProperty(
                        env.getEntity(),
                        property,
                        -1,
                        hardness,
                        output,
                        targetName
                    )
                }
            }
        }
    }
}

fun executeCommand(raw: String, dispatcher: NyarCommandDispatcher<CommandEnv>, env: CommandEnv, output: PrintWriter) {
    val result = dispatcher.dispatch(raw, env)
    if (result.succeed) {
        try {
            result.best?.execute(output)
        } catch (e: Exception) {
            output.println(e.message)
        }
    } else {
        output.println("${result.hints.size}个提示：")
        val displayHints: Array<String> =
            if (result.hints.size < 5) result.hints
            else result.hints.copyOfRange(0, 4).concat("...")
//        val displayHints: Array<String> = result.hints
        displayHints.forEach { output.println("/$it") }
    }
    output.flush()
}
