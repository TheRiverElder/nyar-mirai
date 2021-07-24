package io.github.theriverelder.commands

import io.github.theriverelder.SaveConfig
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.loadAll
import io.github.theriverelder.saveAll
import io.github.theriverelder.util.command.argument.*
import java.io.File
import java.io.PrintWriter
import kotlin.system.exitProcess

fun commandSudo(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("sudo")).addArguments(
        literal(literals = arrayOf("save_all", "sa")).add(
            end { output ->
                saveAll()
                output.println("已保存所有改变");
            }
        )
    ).addArguments(
        literal(literals = arrayOf("load_all", "la")).add(
            end { output ->
                loadAll()
                output.println("已载入所有文件");
            }
        )
    ).addArguments(
        literal(literals = arrayOf("dir")).add(
            end { output ->
                output.println("根目录：${SaveConfig.dirRoot}")
                printDirHierarchy(SaveConfig.dirRoot, 0, output)
            }
        )
    ).addArguments(
        literal(literals = arrayOf("exit")).addArguments(
            number("status", true, default = 0).add(
                end { exitProcess(getOrDefault<Int>("status", 0)) }
                //                end { it.println("Exit with code: ${getOrDefault<Int>("status", 0)}") }
            )
        )
    )
}

fun printDirHierarchy(item: File, level: Int, output: PrintWriter) {
    if (!item.exists()) return
    output.println("| ".repeat(level) + item.name)
    if (item.isDirectory) {
        item.listFiles()?.onEach { printDirHierarchy(it, level + 1, output) }
    }
}