package io.github.theriverelder

import io.github.theriverelder.commands.*
import io.github.theriverelder.data.*
import io.github.theriverelder.util.*
import io.github.theriverelder.util.check.CheckHardness
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import okhttp3.internal.concat
import java.io.PrintWriter
import java.lang.Integer.parseInt
import kotlin.system.exitProcess
import kotlin.String

fun registerBuiltinCommands(dispatcher: NyarCommandDispatcher<CommandEnv>, doAddDangerousCommands: Boolean = false) {
    if (doAddDangerousCommands) {
        dispatcher.register(commandSudo(dispatcher))
    }

    dispatcher.register(commandHelp(dispatcher))
    dispatcher.register(commandRoll())
    dispatcher.register(commandCalculate())
    dispatcher.register(commandEntity())
    dispatcher.register(commandProperty())
    dispatcher.register(commandCheck())
    dispatcher.register(commandGame())
    dispatcher.register(commandCoc())
    dispatcher.register(commandSt())
    dispatcher.register(commandTodayFortune())

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
//        val displayHints: Array<String> =
//            if (result.hints.size < 5) result.hints
//            else result.hints.copyOfRange(0, 4).concat("...")
        val displayHints: Array<String> = result.hints
        displayHints.forEach { output.println("/$it") }
    }
    output.flush()
}
