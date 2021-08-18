package io.github.theriverelder.commands

import io.github.theriverelder.PluginMain
import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.ensureDirectoryExists
import io.github.theriverelder.util.command.argument.*
import java.io.File
import java.io.IOException

fun commandRecord(): LiteralArgumentNode<CommandEnv> {
    return command("record", "rec", "记录") {
        literal("start", "开始") {
            string("fileName") {
                end { output ->
                    val group = env.getOrCreateGameGroup()
                    val fileName = get<String>("fileName")
                    group.recordFileName = fileName
                    output.println("开始记录群(${env.groupUid})：$fileName")
                    output.println("所有以括号（无论全角或半角）开始或结束的文字都会被当作超游哦~")
                }
            }
            end { output ->
                val group = env.tryGetGameGroup() ?: return@end Unit
                val fileName = group.uid.toString() + ".txt"
                group.recordFileName = fileName
                output.println("开始记录群(${env.groupUid})：$fileName")
                output.println("所有以括号（无论全角或半角）开始或结束的文字都会被当作超游哦~")
            }
        }
        literal("stop", "停止") {
            end { output ->
                val group = env.tryGetGameGroup() ?: return@end Unit
                val fileName = group.recordFileName
                group.recordFileName = ""
                if (fileName != "") {
                    output.println("已经停止记录群(${env.groupUid})：$fileName")
                } else {
                    output.println("群(${env.groupUid})没有正在正在进行的录像哦~")
                }
            }
        }
        literal("display", "展示") {
            string("fileName") {
                number("head", true, 5) {
                    number("tail", true, 5) {
                        end { output ->
                            val fileName = get<String>("fileName")
                            val head = get<Number>("head").toInt().coerceAtLeast(1)
                            val tail = get<Number>("tail").toInt().coerceAtLeast(1)

                            ensureDirectoryExists("record")?.also { dir ->
                                val file = File(dir, fileName)
                                try {
                                    val text = file.readText(Charsets.UTF_8)
                                    val lines = text.split(Regex("\r?\n"))

                                    val headEnd = head.coerceAtMost(lines.size)
                                    val tailStart = (lines.size - tail).coerceAtLeast(headEnd)

                                    output.println("读取记录$fileName（头${head}行+尾${tail}行）：")

                                    lines.slice(0 until headEnd).forEach { output.println(it) }
                                    if (headEnd < tailStart) {
                                        output.println("……")
                                    }
                                    lines.slice(tailStart until lines.size).forEach { output.println(it) }
                                } catch (e: IOException) {
                                    output.println("啊，读文件出现问题了呢")
                                    PluginMain.logger.error(e)
                                }
                            } ?: {
                                output.println("啊，记录空空如也")
                            }
                        }
                    }
                }
            }
        }
        literal("submit", "发布") {
            end { output ->
                output.println("阿诺内，介个功能还没实现哦~")
            }
        }
        literal("list", "ls", "列表") {
            end { output ->
                ensureDirectoryExists("record")?.also { dir ->
                    val fileNames = dir.list()
                    output.println("已有的记录（${fileNames?.size ?: 0}个）：")
                    fileNames?.forEach { output.println(it) }
                } ?: {
                    output.println("啊，记录空空如也")
                }
            }
        }
        literal("remove", "rm", "移除") {
            string("fileName") {
                end { output ->
                    val fileName = get<String>("fileName")
                    ensureDirectoryExists("record")?.also {
                        val file = File(it, fileName)
                        if (!file.exists()) {
                            output.println("不存在叫做 $fileName 的记录哟~")
                            return@also
                        }
                        val succeed = file.delete()
                        output.println("删除" + (if (succeed) "成功" else "失败"))
                    } ?: {
                        output.println("啊，记录空空如也")
                    }
                }
            }
        }
    }
}