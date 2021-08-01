package io.github.theriverelder.commands

import io.github.theriverelder.data.*
import io.github.theriverelder.getGame
import io.github.theriverelder.getGameGroup
import io.github.theriverelder.util.command.argument.*
import java.lang.Integer.parseInt

fun commandSt(): LiteralArgumentNode<CommandEnv> {
    return command("set", "st") {
        string("valueStr") {
            end { output ->
                val valueStr: String = get("valueStr")
                val reg = Regex("(\\D+)(\\d+)")
                val values: MutableMap<String, Number> = HashMap()

                val entity: Entity = env.getEntity()

                reg.findAll(valueStr).forEach {
                    val name: String = it.groups[1]!!.value
                    val value: Int = parseInt(it.groups[2]!!.value)
                    values[name] = value
                    entity.setProperty(name, value)
                }

                output.println("设置${entity.name}(#${entity.uid})的属性:")
                output.println(values.entries.joinToString(", ") { "${it.key}=${it.value}" })

            }
        }
    }
}