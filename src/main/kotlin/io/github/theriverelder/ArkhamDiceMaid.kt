package io.github.theriverelder

import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.util.Registry
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path

val GAME_GROUPS: Registry<Long, GameGroup> = Registry()
val GAMES: Registry<Long, Game> = Registry()

object SaveConfig {
    var dirPath: String = "./"

    fun getFile(vararg path: String): File = Path.of(dirPath, *path).toFile()
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

fun Game.save() {
    val file = SaveConfig.getFile("game", name)
	val output = DataOutputStream(FileOutputStream(file))
    write(output)
	output.close()
}

fun GameGroup.save() {
    val file = SaveConfig.getFile("group", uid.toString())
	val output = DataOutputStream(FileOutputStream(file))
    write(output)
	output.close()
}