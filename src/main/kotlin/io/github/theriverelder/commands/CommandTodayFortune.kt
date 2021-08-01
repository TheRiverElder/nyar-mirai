package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Entity
import io.github.theriverelder.data.Game
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.command
import io.github.theriverelder.util.command.argument.end
import net.mamoe.mirai.contact.nameCardOrNick
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

// 首先由当前日期，团内人数、团名、实体名组成一个种子
// 而后根据该种子，依次对实体的每个属性进行一次0/1检定后求和
// 该和与总属性数的比值乘上100
// 该值再加上一个0到1的随机浮点数，取1模，即为其运势
fun commandTodayFortune(): LiteralArgumentNode<CommandEnv> {
    return command("today_fortune", "tf", "今日运势") {
        end { output ->
            val game: Game? = env.tryGetGame()
            val entity: Entity? = if (game != null) env.tryGetEntity() else null
            val gameName = game?.name ?: env.group.name
            val entityName = entity?.name ?: env.player.nameCardOrNick

            val gameNameFactor = Integer.toUnsignedLong(gameName.hashCode()).toDouble()
            val entityCountFactor = (game?.getInvolvedEntities()?.size ?: env.group.members.size).toDouble()
            val entityNameFactor = Integer.toUnsignedLong(entityName.hashCode()).toDouble()

            val now = System.currentTimeMillis()
            val dateFactor = now - now % (1000 * 60 * 60 * 24)

            val seed = floor(
                ((gameNameFactor + entityNameFactor) / 2).pow(2)
                    / (gameNameFactor.pow(2) + entityNameFactor.pow(2))
                    / entityCountFactor
                    * dateFactor
            ).toBits()
            val random = Random(seed)

            val propertyPart = if (entity != null) {
                val properties = entity.getProperties()
                val acc =
                    properties.map { if (it.value == 0) 0 else if (random.nextInt(100) < it.value) 1 else 0 }.sum()
                val propertyCount = properties.size
                if (propertyCount == 0) 0.5 else acc / propertyCount.toDouble()
            } else {
                0.5
            }

            val fix = random.nextDouble()
            val result = floor((propertyPart + fix) % 1.0 * 100).toInt()

            output.println("${entityName}的今日运势为：$result")
//            output.println("""
//                gameName = $gameName
//                entityName = $entityName
//                gameNameFactor = $gameNameFactor
//                entityCountFactor = $entityCountFactor
//                entityNameFactor = $entityNameFactor
//                seed = $seed
//                propertyPart = $propertyPart
//                fix = $fix
//                result = $result
//            """.trimIndent())
        }
    }
}