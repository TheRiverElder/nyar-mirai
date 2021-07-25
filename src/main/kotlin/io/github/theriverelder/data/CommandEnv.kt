package io.github.theriverelder.data

import io.github.theriverelder.*
import io.github.theriverelder.exception.NyarException
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import java.lang.Exception

class CommandEnv(
    public val group: Group,
    public val player: Member,
) {
    val groupUid = group.id
    val playerUid = player.id

    fun getGameGroup(): GameGroup = getOrLoadGameGroup(groupUid)

    fun getOrCreateGameGroup(): GameGroup = GAME_GROUPS[groupUid]
        ?: run {
            val group = GameGroup(groupUid)
            GAME_GROUPS.register(group)
            return group
        }

    fun getGame(): Game {
        val group = getOrCreateGameGroup()
        if (group.gameUid == 0L) throw NyarException("该群没有正在跑的团：（#${groupUid}）")
        return group.game
    }

    fun tryGetGame(): Game? {
        val group = getOrCreateGameGroup()
        return try {
            group.game
        } catch (e: Exception) {
            null
        }
    }

    fun getEntity(): Entity {
        val group = getOrCreateGameGroup()
        val game = group.game
        return game.getControl(playerUid)
    }

    fun tryGetEntity(): Entity? {
        val group = getOrCreateGameGroup()
        return try {
            val game = group.game
            game.getControl(playerUid)
        } catch (e: Exception) {
            null
        }
    }
}