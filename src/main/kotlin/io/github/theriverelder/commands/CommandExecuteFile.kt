package io.github.theriverelder.commands

import io.github.theriverelder.SaveConfig
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.executeCommand
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.literal
import io.github.theriverelder.util.command.argument.string

fun commandExecuteFile(dispatcher: NyarCommandDispatcher<CommandEnv>): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("execute_file", "ef")).addArguments(
        string("filePath").add(
            end { output ->
                val filePath: String = get("filePath")
                val file = SaveConfig.getFile("script", filePath)
                if (!file.exists()) throw Exception("文件未找到：$filePath")

                val lines: List<String> = file.readLines()
                lines.forEach { executeCommand(it, dispatcher, env, output) }
            }
        )
    )
}