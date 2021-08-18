package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.command
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.string

fun commandAlias(): LiteralArgumentNode<CommandEnv> {
    return command("alias", "别名") {
        string("head") {
            string("pattern") {
                end { output ->
                    val head = get<String>("head")
                    val pattern = get<String>("pattern")

                    val group = env.getOrCreateGameGroup()

                    val prevPattern = group.setCommandAlias(head, pattern)
                    output.println("添加新的别名：$head")
                    output.println("对应命令模板：")
                    output.println("/$pattern")
                    if (prevPattern != null) {
                        output.println("对被替换的命令模板：")
                        output.println("/$prevPattern")
                    }
                }
            }
        }
    }
}