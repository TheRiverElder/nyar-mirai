package io.github.theriverelder

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.createOutput
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.info

import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import java.lang.StringBuilder

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.theriverelder.nyarmirai",
        name = "NyarMirai",
        version = "0.2.8"
    ) {
        author("The River Elder")

        info("""
            Arkham-Dice-Maid-2 的Mirai版本
        """.trimIndent())

        // author 和 info 可以删除.
    }
) {
    private val dispatcher: NyarCommandDispatcher<CommandEnv> = NyarCommandDispatcher()

    private fun setup() {
        SaveConfig.dirRoot = this.resolveDataFile(".")
        registerBuiltinCommands(dispatcher, true)
        loadAll()
        indexNames()
    }

    override fun onEnable() {
        logger.info { "Plugin $name v$version loaded" }

        setup()

        GlobalEventChannel.context(coroutineContext).subscribeAlways<GroupMessageEvent> {
            val c = message.content
            val lines = c.split("\n").map { it.trim() }
            if (lines.isEmpty() || !lines.all { it.length >= 2 && it.startsWith("/") }) return@subscribeAlways

            val builder = StringBuilder()
            val output = createOutput(builder)
            val env = CommandEnv(group, sender)
            lines.forEach { executeCommand(it.substring(1), dispatcher, env, output) }
            saveAll()
            PluginMain.logger.verbose("All changes are saved")

            val returnMessage: String = builder.toString().trim()
            if (returnMessage.isNotEmpty()) {
                group.sendMessage(PlainText(returnMessage))
            } else {
                group.sendMessage(PlainText("啊嘞嘞~娜叶酱收到了你的请求，但是好像没有回复呢~"))
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        saveAll()
    }
}
