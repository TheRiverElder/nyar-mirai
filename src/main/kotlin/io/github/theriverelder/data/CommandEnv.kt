package io.github.theriverelder.data

import io.github.theriverelder.GAME_GROUPS
import io.github.theriverelder.exception.NyarException
import io.github.theriverelder.getOrLoadGame
import io.github.theriverelder.getOrLoadGameGroup
import io.github.theriverelder.tryLoadGameGroup
import kotlinx.coroutines.runBlocking

class CommandEnv(
    val groupUid: Long,
    val playerUid: Long,
) {
    fun getGroup(): GameGroup = getOrLoadGameGroup(groupUid)

    fun getOrCreateGroup(): GameGroup = GAME_GROUPS[groupUid]
        ?: run {
            val group = GameGroup(groupUid)
            GAME_GROUPS.register(group)
            return group
        }

    fun getGame(): Game {
        val group = getOrCreateGroup()
        if (group.gameUid == 0L) throw NyarException("No running game for Group(#${groupUid})")
        return group.game
    }

    fun getEntity(): Entity {
        val group = getOrCreateGroup()
        val game = group.game
        return game.getControl(playerUid) ?: throw NyarException("No entity is controlled by Player(#${playerUid})")
    }
}