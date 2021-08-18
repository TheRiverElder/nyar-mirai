package io.github.theriverelder

import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.util.Registry
import io.github.theriverelder.util.io.MapData
import io.github.theriverelder.util.io.writeMapData
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path

val GAME_GROUPS: Registry<Long, GameGroup> = Registry { it.uid }
val GAMES: Registry<Long, Game> = Registry { it.uid }
val ENTITIES: Registry<Long, Entity> = Registry { it.uid }
val NAME_INDEXED_GAME_UIDS: MutableMap<String, Long> = HashMap()
val NAME_INDEXED_ENTITY_UIDS: MutableMap<String, Long> = HashMap()

object SaveConfig {
    var dirRoot: File = File("./")

    fun getFile(vararg path: String): File {
        var file = dirRoot
        for (s in path) {
            file = File(file, s)
        }
        return file
    }
}

fun indexNames() {
    NAME_INDEXED_GAME_UIDS.clear()
    for (game in GAMES.getAll()) {
        NAME_INDEXED_GAME_UIDS[game.name] = game.uid
    }

    NAME_INDEXED_ENTITY_UIDS.clear()
    for (entity in ENTITIES.getAll()) {
        NAME_INDEXED_ENTITY_UIDS[entity.name] = entity.uid
    }
}

fun ensureDirectoryExists(dirName: String): File? {
    val dir = SaveConfig.getFile(dirName)
    if (!dir.exists()) return if (dir.mkdirs()) dir else null
    if (!dir.isDirectory) return null
    return dir
}

fun save(all: Boolean = false) {
    if (all) {
        GAMES.items.forEach { it.save() }
        GAME_GROUPS.items.forEach { it.save() }
    } else {
	    GAMES.items.filter { it.dirty } .forEach { it.save() }
        GAME_GROUPS.items.filter { it.dirty } .forEach { it.save() }
	}
}

fun Entity.save() {
    val file = SaveConfig.getFile("entity", uid.toString())
    val data = MapData()
    write(data)
    val output = DataOutputStream(FileOutputStream(file))
    writeMapData(output, data)
    output.close()
}

fun Game.save() {
    val file = SaveConfig.getFile("game", uid.toString())
    val data = MapData()
    write(data)
    val output = DataOutputStream(FileOutputStream(file))
    writeMapData(output, data)
    output.close()
}

fun GameGroup.save() {
    val file = SaveConfig.getFile("group", uid.toString())
    val data = MapData()
    write(data)
    val output = DataOutputStream(FileOutputStream(file))
    writeMapData(output, data)
	output.close()
}