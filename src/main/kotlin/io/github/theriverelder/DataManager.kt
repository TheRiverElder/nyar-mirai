package io.github.theriverelder

import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.exception.LoadFailedException
import io.github.theriverelder.exception.NyarException

fun getOrLoadGameGroup(groupUid: Long): GameGroup =
    GAME_GROUPS[groupUid] ?: tryLoadGameGroup(groupUid) ?: throw LoadFailedException("Group(#${groupUid})")

fun getOrLoadGame(gameUid: Long): Game =
    GAMES[gameUid] ?: tryLoadGame(gameUid) ?: throw LoadFailedException("Game(#${gameUid})")

fun getOrLoadEntity(entityUid: Long): Entity =
    ENTITIES[entityUid] ?: tryLoadEntity(entityUid) ?: throw LoadFailedException("Entity(#${entityUid})")

fun getOrLoadGameByName(gameName: String): Game =
    GAMES.getAll().find { it.name === gameName }
        ?: tryLoadGame(NAME_INDEXED_GAME_UIDS[gameName]
            ?: throw NyarException("Unrecognized game name: $gameName"))
        ?: throw LoadFailedException("Game $gameName")