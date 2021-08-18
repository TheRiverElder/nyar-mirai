package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Entity
import io.github.theriverelder.util.command.argument.*

fun commandProperty(): LiteralArgumentNode<CommandEnv> {
    return command("property", "prop", "p", "属性") {
        literal("list", "ls") {
            end { output ->
                val entity: Entity = env.getEntity()
                val properties = entity.getProperties()
                output.println("${entity.name}的属性（${properties.size}个）：")
                properties.forEach { output.println("${it.key}=${it.value}") }
            }
        }
        string("name") {
            literal("remove", "rm") {
                end { output ->
                    val self: Entity = env.getEntity()

                    val propName: String = get("name") ?: return@end Unit

                    val prevValue = self.removeProperty(propName)
                    output.println("已删除属性：${self.name} 的 $propName（$prevValue）")
                }
            }
            literal("operation", "set", "add", "minus", "=", "+", "-", "设为" , "增加", "减少") {
                number("value") {
                    end { output ->
                        val self: Entity = env.getEntity()

                        val propName: String = get("name")
                        val value: Number = get("value")
                        val operation: String = get("operation")

                        val prev = self.getProperty(propName)
                        when (operation) {
                            "set", "=", "设为" -> self.setProperty(propName, value.toInt())
                            "add", "+", "增加" -> self.changeProperty(propName, value.toInt())
                            "minus", "-", "减少" -> self.changeProperty(propName, -value.toInt())
                        }
                        output.println("属性已更新：${self.name} 的 $propName: $prev -> ${self.getProperty(propName)}")
                    }
                }
            }
        }
    }
}