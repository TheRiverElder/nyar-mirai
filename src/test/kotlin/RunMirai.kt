package io.github.theriverelder

import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.BotConfiguration

@ConsoleExperimentalApi
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    PluginMain.enable()

    // 123456, ""
    val bot = MiraiConsole.addBot(744973592, "Nyar1ath0tep") {
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()
}