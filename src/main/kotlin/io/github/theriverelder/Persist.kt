package io.github.theriverelder

import io.github.theriverelder.data.*
import io.github.theriverelder.util.Registry
import io.github.theriverelder.util.io.readMapData
import io.github.theriverelder.util.persistent.FormatIOPersistentHelper
import io.github.theriverelder.util.persistent.PersistentHelper
import java.io.*
import java.lang.Exception
import java.lang.Long.parseLong

lateinit var GAME_PERSISTENT_HELPER: PersistentHelper<Long, Game>
lateinit var GAME_GROUP_PERSISTENT_HELPER: PersistentHelper<Long, GameGroup>
lateinit var ENTITY_PERSISTENT_HELPER: PersistentHelper<Long, Entity>

fun initializePersistentHelpers(dataDirectory: File) {
    GAME_PERSISTENT_HELPER = FormatIOPersistentHelper(
        GAMES,
        File(dataDirectory, "game"),
        { Game() },
        Game::uid,
    )
    GAME_GROUP_PERSISTENT_HELPER = FormatIOPersistentHelper(
        GAME_GROUPS,
        File(dataDirectory, "group"),
        { GameGroup() },
        GameGroup::uid,
    )
    ENTITY_PERSISTENT_HELPER = FormatIOPersistentHelper(
        ENTITIES,
        File(dataDirectory, "entity"),
        { Entity() },
        Entity::uid,
    )

}

fun <K, V> saveRegistryItems(
    registry: Registry<K, V>,
    persistentHelper: PersistentHelper<K, V>,
    shouldSave: (V) -> Boolean
) {
    registry.getAll().filter(shouldSave).forEach(persistentHelper::save)
}


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
    saveRegistryItems(GAMES, GAME_PERSISTENT_HELPER, Game::dirty)
    saveRegistryItems(GAME_GROUPS, GAME_GROUP_PERSISTENT_HELPER, GameGroup::dirty)
    saveRegistryItems(ENTITIES, ENTITY_PERSISTENT_HELPER, Entity::dirty)
}

fun loadAll() {
//    try {
//        val indexInput = DataInputStream(FileInputStream(File(SaveConfig.dirRoot, "index")))
//        val count = indexInput.readInt()
//        NAME_INDEXED_GAME_UIDS.clear()
//        for (i in 0 until count) {
//            val uid = indexInput.readLong()
//            val name = indexInput.readUTF()
//            NAME_INDEXED_GAME_UIDS[name] = uid
//        }
//    } catch (e: Exception) {
//        PluginMain.logger.error(e)
//    }



    ensureDirectoryExists("group")?.let { file ->
        file.list()?.forEach { GAME_PERSISTENT_HELPER.load(parseLong(it)) }
    }
    ensureDirectoryExists("game")?.let { file ->
        file.list()?.forEach { GAME_GROUP_PERSISTENT_HELPER.load(parseLong(it)) }
    }
    ensureDirectoryExists("entity")?.let { file ->
        file.list()?.forEach { ENTITY_PERSISTENT_HELPER.load(parseLong(it)) }
    }
}

fun tryLoadEntity(entityUid: Long): Entity? {
    return try {
        ENTITY_PERSISTENT_HELPER.load(entityUid)
    } catch (e: Exception) {
        null
    }
}

fun tryLoadGame(gameUid: Long): Game? {
    return try {
        GAME_PERSISTENT_HELPER.load(gameUid)
    } catch (e: Exception) {
        null
    }
}

fun tryLoadGameGroup(groupUid: Long): GameGroup? {
    return try {
        GAME_GROUP_PERSISTENT_HELPER.load(groupUid)
    } catch (e: Exception) {
        null
    }
}