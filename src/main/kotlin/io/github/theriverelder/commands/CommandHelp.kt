package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*

fun commandHelp(dispatcher: NyarCommandDispatcher<CommandEnv>): LiteralArgumentNode<CommandEnv> {
    return command("help", "h", "帮助") {
        string("commandName") {
            end { output ->
                val commandName = get<String>("commandName")
                val cmd = dispatcher.getCommand(commandName)
                if (cmd == null) {
                    output.println("找不到指令：$commandName")
                } else {
                    val res = ArrayList<String>()
                    cmd.getHelp("", res)
                    res.forEach { output.println("/" + it.substring(1)) }
                }
            }
        }
        end { output ->
            val res = ArrayList<String>()
            dispatcher.getAllCommands().forEach { it.getHelp("", res) }

            output.println("可用指令：")
            res.forEach { output.println("/" + it.substring(1)) }
        }
    }
}