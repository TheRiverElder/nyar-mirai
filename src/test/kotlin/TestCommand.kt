package io.github.theriverelder

import io.github.theriverelder.data.*
import io.github.theriverelder.util.command.NyarCommandDispatcher
import okhttp3.internal.concat
import java.io.File
import java.io.PrintWriter

fun main() {
    val dispatcher = NyarCommandDispatcher<CommandEnv>()

    val testGroup = GroupTestImpl(1077572661L, "疯狂输出")
    val testMember = MemberTestImpl(testGroup, 765375399L, "RiverElder")
    val env = CommandEnv(testGroup, testMember)

    SaveConfig.dirRoot = File("./test/")

    registerBuiltinCommands(dispatcher, true)

    val output = PrintWriter(System.out)
    while (true) {
        print(">>> ")
        if (!loop(dispatcher, env, { readLine() }, output)) break
    }
}

fun loop(dispatcher: NyarCommandDispatcher<CommandEnv>, env: CommandEnv, lineReader: () -> String?, output: PrintWriter): Boolean {
    val input = lineReader() ?: return false
    val result = dispatcher.dispatch(input, env)
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
    return true
}
