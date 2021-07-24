package io.github.theriverelder.commands

import io.github.theriverelder.*
import io.github.theriverelder.data.*
import io.github.theriverelder.util.command.argument.*

fun commandProperty(): LiteralArgumentNode<CommandEnv> {
    return literal(literals = arrayOf("property", "prop", "p", "属性")).addArguments(
        string(key = "name", matcher = NAME_MATCHER).add(
            literal(literals = arrayOf("remove", "rm")).add(
                end { output ->
                    val game: Game = env.getGame()
                    val self: Entity = env.getEntity()

                    val propName: String = get("name") ?: return@end Unit

                    val prevValue = self.removeProperty(propName)
                    output.println("已删除属性：${self.name} 的 $propName（$prevValue）")
                }
            )
        ).add(
            literal(key = "operation", literals = arrayOf("set", "add", "minus", "=", "+", "-")).add(
                number(key = "value").add(
                    end { output ->
                        val game: Game = env.getGame()
                        val self: Entity = env.getEntity()

                        val propName: String = get("name")
                        val value: Number = get("value")
                        val operation: String = get("operation")

                        val prev = self.getProperty(propName)
                        when (operation) {
                            "set", "=" -> self.setProperty(propName, value.toInt())
                            "add", "+" -> self.changeProperty(propName, value.toInt())
                            "minus", "-" -> self.changeProperty(propName, -value.toInt())
                        }
                        output.println("属性已更新：${self.name} 的 $propName: $prev -> ${self.getProperty(propName)}")
                    }
                )
            )
        )
    )
}