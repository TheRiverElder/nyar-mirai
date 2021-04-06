package io.github.theriverelder

import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.data.GameGroup
import io.github.theriverelder.data.PropertyValue
import io.github.theriverelder.util.check.CheckHardness
import java.io.PrintWriter
import java.lang.Long.parseLong
import io.github.theriverelder.util.check.check

fun getGameGroup(groupUid: Long, output: PrintWriter): GameGroup? {
    val group = GAME_GROUPS[groupUid]
    if (group == null) {
        output.println("Cannot find group with uid: $groupUid")
    }
    return group
}

fun searchGame(name: String, output: PrintWriter): Game? {
    val games = GAMES.getAll().filter { it.name == name }
    if (games.size > 1) {
        output.println("Duplicated game name: " + games.joinToString(", ") { "${it.name}(#${it.uid})" })
        return null
    } else if (games.isEmpty()) {
        output.println("No game with name: $name")
        return null
    }
    return games[0]
}

fun getGame(groupUid: Long, output: PrintWriter): Game? {
    val group = getGameGroup(groupUid, output) ?: return null
    val game = group.game
    if (game == null) {
        output.println("No running game for group uid: $groupUid")
    }
    return game
}

fun getEntity(group: GameGroup, game: Game, playerUid: Long, output: PrintWriter): Entity? {
    val entity = group.getControl(playerUid)

    if (entity == null) {
        output.println("Player dose not control any entity: #$playerUid")
    } else if (!game.entities.hasItem(entity)) {
        output.println("Entity ${entity.name}(#${entity.uid}) dose not belong to game: ${game.name}(#${game.uid})")
    }
    return entity
}

fun getEntity(game: Game, entityStr: String, output: PrintWriter): Entity? {
    var entity: Entity? = null
    if (entityStr.matches(Regex("^#\\d+$"))) {
        entity = game.entities[parseLong(entityStr.substring(1))]
    } else {
        val entities = game.entities.getAll().filter { it.name == entityStr }
        if (entities.size > 1) {
            output.println("Duplicated name, use #UID instead: " + entities.joinToString(", ") { "${it.name}(uid: ${it.uid})" })
            return null
        }
        entity = entities.getOrNull(0)
    }

    if (entity == null) {
        output.println("Entity not exists: $entityStr")
        return null
    }
    return entity
}

fun getEntityProperty(game: Game, entity: Entity, propName: String, output: PrintWriter): PropertyValue? {
    val prop = game.propertyTypes.getAll().find { it.name == propName }
    if (prop == null) {
        output.println("Property not exists in ${entity.name}: $propName")
        return null
    }
    return entity.getOrCreateProperty(prop)
}

fun checkEntityProperty(game: Game, entity: Entity, propName: String, hardness: CheckHardness, output: PrintWriter) {
    val prop = getEntityProperty(game, entity, propName, output) ?: return

    val result = check(prop.value, hardness)
    output.println("Check ${result.hardness} ${prop.type.name} of ${entity.name}: " +
        "${result.target}/${result.value} -> ${result.points}, " +
        "${if (result.succeed) "succeeded" else "failed"}! " +
        "(as ${result.resultType})"
    )
}