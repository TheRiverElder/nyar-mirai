package io.github.theriverelder

import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.util.check.CheckHardness
import java.io.PrintWriter
import java.lang.Long.parseLong
import io.github.theriverelder.util.check.check
import io.github.theriverelder.util.check.toLocaleString

fun getGameGroup(groupUid: Long): GameGroup {
    tryLoadGameGroup(groupUid)?.also { return@getGameGroup it }
    return GAME_GROUPS[groupUid] ?: throw Exception("找不到组#$groupUid")
}

fun searchGame(name: String): Game {
    val games = GAMES.getAll().filter { it.name == name }
    if (games.size > 1) {
        throw Exception("存在重名团，使用#UID形式确定实体：" + games.joinToString("，") { "#${it.uid}" })
    } else if (games.isEmpty()) {
        throw Exception("不存在名为${name}的团")
    }
    return games[0]
}

fun trySearchGame(name: String): Game? {
    val games = GAMES.getAll().filter { it.name == name }
    if (games.size > 1) {
        throw Exception("存在重名团，使用#UID形式确定实体：" + games.joinToString("，") { "#${it.uid}" })
    } else if (games.isEmpty()) {
        return null
    }
    return games[0]
}

fun getGame(groupUid: Long): Game = getGameGroup(groupUid).game

fun getEntity(game: Game, playerUid: Long): Entity = game.getControl(playerUid)

fun getEntity(game: Game, entityStr: String): Entity {
    return if (entityStr.matches(Regex("^#\\d+$"))) {
        getOrLoadEntity(parseLong(entityStr.substring(1)))
    } else {
//        val entities = game.getInvolvedEntities().filter { it.name == entityStr }
        val entities = ENTITIES.getAll().filter { it.name == entityStr }
        if (entities.size > 1) throw Exception("存在重名实体，使用#UID形式确定实体：" + entities.joinToString("，") { "#${it.uid}" })
        else if (entities.isEmpty()) throw Exception("未找到实体：$entityStr")
        entities[0]
    }
}

fun checkEntityProperty(entity: Entity, propName: String, initialValue: Int, hardness: CheckHardness, output: PrintWriter, targetName: String = "") {
    val value = if (initialValue > 0) initialValue else entity.getProperty(propName)
    val result = check(value, hardness)

    with(result) {
        val msg = if (targetName.isBlank()) {
            if (succeed) "成功了呢" else "失败了哟"
        } else {
            if (succeed) "${entity.name}把${targetName}给${propName}了呢"
            else "${entity.name}没能把${targetName}给${propName}哟"
        }
        output.println("检定${entity.name}的${if (hardness == CheckHardness.NORMAL) "" else hardness.toLocaleString()}$propName：" +
            "${target}/${value} -> ${points}, " +
            "${msg}! " +
            "（是${resultType.toLocaleString()}哦）"
        )
    }

}