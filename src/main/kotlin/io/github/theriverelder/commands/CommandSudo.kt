package io.github.theriverelder.commands

import io.github.theriverelder.*
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.io.*
import java.io.*
import java.util.*
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
        literal("data", "ef") {
            literal("read") {
                string("filePath") {
                    end { output ->
                        val filePath: String = get("filePath")
                        val file = SaveConfig.getFile(filePath)
                        if (!file.exists()) throw Exception("文件未找到：$filePath")
                        if (!file.isFile) throw Exception("这个不是文件：$filePath")

                        try {
                            val input = DataInputStream(FileInputStream(file))
                            val data = readMapData(input)
                            input.close()

                            printDataHierarchy(data, 0, output)
                        } catch (e: IOException) {
                            PluginMain.logger.error(e)
                            output.println("哎呀呀，读文件出错了呢")
                        }
                    }
                }
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

fun printDataHierarchy(item: Data<*>, level: Int, output: PrintWriter) {
    when (item) {
        is MapData -> {
            output.println("{")
            for (entry in item.value) {
                output.print("  ".repeat(level + 1) + entry.key + ": ")
                printDataHierarchy(entry.value, level + 1, output)
                output.println(",")
            }
            output.print("  ".repeat(level) + "}")
        }
        is ListData -> {
            output.println("[")
            for (elem in item.value) {
                output.print("  ".repeat(level + 1))
                printDataHierarchy(elem, level + 1, output)
                output.println(",")
            }
            output.print("  ".repeat(level) + "]")
        }
        is BooleanData -> output.print(item.value)
        is IntData -> output.print(item.value)
        is LongData -> output.print("${item.value}L")
        is DoubleData -> output.print(item.value)
        is StringData -> output.print("\"${item.value.replace("\"", "\\\"")}\"")
        else -> output.print(item.value)
    }
}