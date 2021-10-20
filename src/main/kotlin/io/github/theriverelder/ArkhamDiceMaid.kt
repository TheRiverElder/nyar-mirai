package io.github.theriverelder

import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.util.Registry
import java.io.File

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