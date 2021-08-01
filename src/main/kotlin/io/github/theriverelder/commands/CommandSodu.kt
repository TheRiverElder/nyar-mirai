package io.github.theriverelder.commands

import io.github.theriverelder.SaveConfig
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.executeCommand
import io.github.theriverelder.loadAll
import io.github.theriverelder.saveAll
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import java.io.File
import java.io.PrintWriter
import kotlin.system.exitProcess

fun commandSudo(dispatcher: NyarCommandDispatcher<CommandEnv>): LiteralArgumentNode<CommandEnv> {
    return command("sudo") {
        literal("save_all", "sa") {
            end { output ->
                saveAll()
                output.println("已保存所有改变");
            }
        }
        literal("load_all", "la") {
            end { output ->
                loadAll()
                output.println("已载入所有文件");
            }
        }
        literal("dir") {
            end { output ->
                output.println("根目录：${SaveConfig.dirRoot}")
                printDirHierarchy(SaveConfig.dirRoot, 0, output)
            }
        }
        literal("exit") {
            number("status", true, default = 0) {
                end { exitProcess(getOrDefault<Int>("status", 0)) }
                // end { it.println("Exit with code: ${getOrDefault<Int>("status", 0)}") }
            }
        }
        literal("execute_file", "ef") {
            string("filePath") {
                end { output ->
                    val filePath: String = get("filePath")
                    val file = SaveConfig.getFile("script", filePath)
                    if (!file.exists()) throw Exception("文件未找到：$filePath")

                    val lines: List<String> = file.readLines()
                    lines.forEach { executeCommand(it, dispatcher, env, output) }
                }
            }
        }
    }
}

fun printDirHierarchy(item: File, level: Int, output: PrintWriter) {
    if (!item.exists()) return
    output.println("| ".repeat(level) + item.name)
    if (item.isDirectory) {
        item.listFiles()?.onEach { printDirHierarchy(it, level + 1, output) }
    }
}