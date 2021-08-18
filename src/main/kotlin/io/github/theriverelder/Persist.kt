package io.github.theriverelder

import io.github.theriverelder.data.*
import io.github.theriverelder.util.io.readMapData
import java.io.*
import java.lang.Exception

fun saveAll() {
    try {
        val indexOutput = DataOutputStream(FileOutputStream(File(SaveConfig.dirRoot, "index")))
        indexOutput.writeInt(GAMES.size)
        GAMES.getAll().forEach {
            indexOutput.writeLong(it.uid)
            indexOutput.writeUTF(it.name)
        }
        indexOutput.close()
    } catch (e: Exception) {
        PluginMain.logger.error(e)
    }
    ensureDirectoryExists("group")?.also { GAME_GROUPS.getAll().filter { it.dirty }.forEach { it.save() } }
    ensureDirectoryExists("game")?.also { GAMES.getAll().filter { it.dirty }.forEach { it.save() } }
    ensureDirectoryExists("entity")?.also { ENTITIES.getAll().filter { it.dirty }.forEach { it.save() } }
}

fun loadAll() {
    try {
        val indexInput = DataInputStream(FileInputStream(File(SaveConfig.dirRoot, "index")))
        val count = indexInput.readInt()
        NAME_INDEXED_GAME_UIDS.clear()
        for (i in 0 until count) {
            val uid = indexInput.readLong()
            val name = indexInput.readUTF()
            NAME_INDEXED_GAME_UIDS[name] = uid
        }
    } catch (e: Exception) {
        PluginMain.logger.error(e)
    }

    ensureDirectoryExists("game")?.let {
        it.listFiles()?.forEach { file ->
            try {
                val stream = FileInputStream(file)
                val game = Game()
                val data = readMapData(DataInputStream(stream))
                game.read(data)
                GAMES.register(game)
                stream.close()
            } catch (e: Exception) {
                PluginMain.logger.error(e)
            }
        }
    }

    ensureDirectoryExists("group")?.let {
        it.listFiles()?.forEach { file ->
            try {
                val stream = FileInputStream(file)
                val gameGroup = GameGroup()
                val data = readMapData(DataInputStream(stream))
                gameGroup.read(data)
                GAME_GROUPS.register(gameGroup)
                stream.close()
            } catch (e: Exception) {
                PluginMain.logger.error(e)
            }
        }
    }

    ensureDirectoryExists("entity")?.let {
        it.listFiles()?.forEach { file ->
            try {
                val stream = FileInputStream(file)
                val entity = Entity()
                val data = readMapData(DataInputStream(stream))
                entity.read(data)
                ENTITIES.register(entity)
                stream.close()
            } catch (e: Exception) {
                PluginMain.logger.error(e)
            }
        }
    }
}

fun tryLoadEntity(entityUid: Long): Entity? {
    return ensureDirectoryExists("entity")?.run {
        val file = File(this, entityUid.toString())
        if (!file.exists() || !file.isFile) return null
        val input = DataInputStream(FileInputStream(file))
        val entity = Entity()
        val data = readMapData(input)
        entity.read(data)
        ENTITIES.register(entity)
        input.close()
        return entity
    }
}

fun tryLoadGame(gameUid: Long): Game? {
    return ensureDirectoryExists("game")?.run {
        val file = File(this, gameUid.toString())
        if (!file.exists() || !file.isFile) return null
        val input = DataInputStream(FileInputStream(file))
        val game = Game()
        val data = readMapData(input)
        game.read(data)
        GAMES.register(game)
        input.close()
        return game
    }
}

fun tryLoadGameGroup(groupUid: Long): GameGroup? {
    return ensureDirectoryExists("group")?.run {
        val file = File(this, groupUid.toString())
        if (!file.exists() || !file.isFile) return null
        val input = DataInputStream(FileInputStream(file))
        val gameGroup = GameGroup()
        val data = readMapData(input)
        gameGroup.read(data)
        GAME_GROUPS.register(gameGroup)
        input.close()
        return gameGroup
    }
}