package io.github.theriverelder

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Entity
import io.github.theriverelder.util.command.NyarCommandDispatcher
import io.github.theriverelder.util.io.createOutput
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.event.subscribeOtherClientMessages
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.theriverelder.nyarmirai",
        name = "NyarMirai",
        version = "0.3.6"
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
        SaveConfig.dirRoot = this.dataFolder
        initializePersistentHelpers(SaveConfig.dirRoot)
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

            val env = CommandEnv(group, sender)

            GAME_GROUPS[group.id]?.also {
                val game = env.tryGetGame()
                var entity: Entity? = null
                val name = if (game != null && game.hasControl(sender.id)) {
                    entity = game.getControl(sender.id)
                    entity.name
                } else {
                    sender.nameCardOrNick
                }
                it.recordWriter?.write("[name=${name},senderId=${sender.id},entityId=${entity?.uid ?: 0}]\n")
                it.recordWriter?.write(c.lines().joinToString("\n") { line -> ">$line" })
                it.recordWriter?.write("\n\n")
                it.recordWriter?.flush()
            }

            if (lines.isEmpty() || !lines.all { it.length >= 2 && it.startsWith("/") }) return@subscribeAlways

            val builder = StringBuilder()
            val output = createOutput(builder)
            lines.forEach { executeCommand(it.substring(1), dispatcher, env, output) }
            saveAll()
            PluginMain.logger.verbose("All changes are saved")

            val returnMessage: String = builder.toString().trim().ifEmpty { "啊嘞嘞~娜叶酱收到了你的请求，但是好像没有回复呢~" }
            group.sendMessage(message.quote() + returnMessage)

            GAME_GROUPS[group.id]?.also {
                it.recordWriter?.write("[name=${bot.nick},senderId=${bot.id},entityId=0]\n")
                it.recordWriter?.write(returnMessage.lines().joinToString("\n") { line -> ">$line" })
                it.recordWriter?.write("\n\n")
                it.recordWriter?.flush()
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        saveAll()
    }
}
