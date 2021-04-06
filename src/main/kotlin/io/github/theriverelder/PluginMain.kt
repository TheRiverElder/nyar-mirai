package io.github.theriverelder

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.util.DICE_1D100
import io.github.theriverelder.util.Dice
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.command.argument.*
import io.github.theriverelder.util.createOutput
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.info

import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import okhttp3.internal.concat
import java.io.PrintWriter
import java.lang.StringBuilder
import kotlin.system.exitProcess

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.theriverelder.nyarmirai",
        name = "NyarMirai",
        version = "0.2.0"
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
        registerBuiltinCommands(dispatcher)
    }

    override fun onEnable() {
        logger.info { "Plugin $name v$version loaded" }

        setup()

        GlobalEventChannel.context(coroutineContext).subscribeAlways<GroupMessageEvent> {
            val c = message.content
            if (c.length < 2 || !c.startsWith("/")) return@subscribeAlways

            val input = message.content.substring(1)
            val builder = StringBuilder()
            val output = createOutput(builder)
            val env = CommandEnv(group.id, sender.id)
            executeCommand(input, dispatcher, env, output)

            val returnMessage: String = builder.toString()
            if (returnMessage.isNotEmpty()) {
                group.sendMessage(PlainText(returnMessage))
            } else {
                group.sendMessage(PlainText("啊嘞嘞~娜叶酱收到了你的请求，但是好像没有回复呢~"))
            }
        }
    }
}
