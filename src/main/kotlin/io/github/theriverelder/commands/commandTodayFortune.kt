package io.github.theriverelder.commands

import io.github.theriverelder.data.*
import io.github.theriverelder.getGame
import io.github.theriverelder.getGameGroup
import io.github.theriverelder.util.command.argument.LiteralArgumentNode
import io.github.theriverelder.util.command.argument.end
import io.github.theriverelder.util.command.argument.literal
import java.util.*
import java.util.Calendar
import kotlin.math.roundToInt
import kotlin.random.Random


fun commandTodayFortune(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("today_fortune", "tf", "今日运势")).addArguments(
        end { output ->
            val game: Game = env.getGame()
            val entity: Entity = env.getEntity()

            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            val propertyPart: Int = Random(entity.getProperties().sumOf { it.value / 100.0 }.roundToInt()).nextInt(0, 80)
            val namePart: Int = Random(entity.name.hashCode()).nextInt(0, 20)


            output.println("${entity.name}的今日运势为：${propertyPart + namePart}")
        }
    )
}