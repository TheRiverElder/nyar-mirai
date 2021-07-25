package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.DICE_3D6
import io.github.theriverelder.util.Dice
import io.github.theriverelder.util.choose
import io.github.theriverelder.util.command.argument.*

fun commandCoc(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("coc")).addArguments(
        number("totalPoints").add(
            number("step", default = 5).add(
                literal("includesLuck", literals = arrayOf("includes_luck", "excludes_luck"), default = "includes_luck").add(
                    end { output ->
                        val totalPoints: Int = get<Number>("totalPoints").toInt()
                        val step: Int = get<Number>("step").toInt()
                        val includesLuck: Boolean = "includes_luck" == get<String>("includesLuck")

                        val distributePoints = totalPoints / step
                        var properties: Array<String> = arrayOf("力量", "体质", "体型", "敏捷", "外貌", "智力", "意志", "教育")
                        if (includesLuck) {
                            properties += "幸运"
                        }
                        val result: MutableMap<String, Int> = HashMap(properties.size)
                        properties.forEach { result[it] = 0 }
                        for (i in 0 until distributePoints) {
                            val prop = choose(properties)
                            result[prop] = result[prop]!! + step
                        }

                        output.println(properties.joinToString(", ") { "$it=${result[it]}" } + ", total: ${distributePoints * step}")
                    }
                )
            )
        )
    ).addArguments(
        literal(literals = arrayOf("roll")).addArguments(
            number("step", default = 5).add(
                dice(key = "dice", default = DICE_3D6).add(
                    end { output ->
                        val step: Int = get<Number>("step").toInt()
                        val dice: Dice = get("dice")

                        val properties: Array<String> = arrayOf("力量", "体质", "体型", "敏捷", "外貌", "智力", "意志", "教育")

                        val result: MutableMap<String, Int> = HashMap(properties.size + 1)
                        var totalPoints = 0
                        val luck = dice.roll() * step
                        for(prop in properties) {
                            val points = dice.roll() * step
                            result[prop] = points
                            totalPoints += points
                        }
                        result["幸运"] = luck


                        output.println(properties.joinToString(", ") { "$it=${result[it]}" } + ", total: $totalPoints / ${totalPoints + luck}")
                    }
                )
            )
        )
    )
}