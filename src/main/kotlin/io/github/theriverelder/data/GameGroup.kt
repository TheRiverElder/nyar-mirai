package io.github.theriverelder.data

import io.github.theriverelder.PluginMain
import io.github.theriverelder.ensureDirectoryExists
import io.github.theriverelder.getOrLoadGame
import io.github.theriverelder.util.io.Serializable
import io.github.theriverelder.util.io.*
import java.io.*

class GameGroup(uid: Long = 0) : Serializable {

    public var uid: Long = uid
    public var dirty: Boolean = true

    public var recordWriter: OutputStreamWriter? = null
        private set

    public var recordFileName: String = ""
        set(value) {
            val fileName = value.trim()
            if (fileName == "") {
                recordWriter?.flush()
                recordWriter?.close()
                recordWriter = null
                field = fileName
            } else if (fileName.matches(Regex(".+"))) {
                if (fileName == field) return
                field = fileName
                dirty = dirty || field != fileName
                recordWriter?.flush()
                recordWriter?.close()
                ensureDirectoryExists("record")?.also {
                    val file = File(it, fileName)
                    if (!it.exists()) {
                        it.createNewFile()
                    }
                    try {
                        recordWriter = OutputStreamWriter(FileOutputStream(file, true), Charsets.UTF_8)
                    } catch (e: IOException) {
                        PluginMain.logger.error(e)
                        recordWriter = null
                        field = ""
                    }
                }
            }
        }

    public var gameUid: Long = 0
        set(value) {
            dirty = dirty || field != value
            field = value
        }

    public val game: Game
        get() {
            if (gameUid == 0L) throw Exception("群（#${uid}）未绑定团")
            return getOrLoadGame(this.gameUid)
        }



    private val commandAliases: MutableMap<String, String> = HashMap()

    // 若之前设置过，则覆盖，并返回之前的命令模板
    public fun setCommandAlias(head: String, pattern: String): String? {
        return commandAliases[head] ?: run {
            commandAliases[head] = pattern
            dirty = true
            null
        }
    }

    public fun tryGetCommandAlias(head: String): String? = commandAliases[head]

    override fun read(input: MapData) {
        uid = input.getLong("uid")
        gameUid = input.getLong("gameUid")
        recordFileName = input.getString("recordFileName")

        commandAliases.clear()
        for (i in input.getList("commandAliases").value) {
            if (i is ListData) {
                val head = i.getString(0)
                val pattern = i.getString(1)
                commandAliases[head] = pattern
            }
        }
        dirty = true
    }

    override fun write(output: MapData) {
        output.value["uid"] = LongData(uid)
        output.value["gameUid"] = LongData(gameUid)
        output.value["recordFileName"] = StringData(recordFileName)
        output.value["commandAliases"] = ListData(
            *commandAliases.map { ListData(StringData(it.key), StringData(it.value)) }.toTypedArray()
        )
        dirty = false
    }


}