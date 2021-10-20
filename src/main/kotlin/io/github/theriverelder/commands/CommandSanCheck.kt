package io.github.theriverelder.commands

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Entity
import io.github.theriverelder.util.command.argument.*
import kotlinx.coroutines.delay
import io.github.theriverelder.util.check.check as check


fun commandSanCheck(): LiteralArgumentNode<CommandEnv> {
    return command("san-check", "sc", "SC", "SanCheck", "意志检定") {
        string("expr") {
            end { output ->
                val expr = get<String>("expr")

                val exprList = expr.split('/')
                if (exprList.size != 2) throw Exception("不合法的表达式：没有没有找到斜杠‘/’")
                val succeedExpr = exprList[0]
                val failExpr = exprList[1]

                val succeedDice = parseDice(succeedExpr) ?: throw Exception("无效的成功骰子：${succeedExpr}")
                val failDice = parseDice(failExpr) ?: throw Exception("无效的失败骰子：${failExpr}")

                val entity = env.getEntity()
                val propertyValue = tryGetProperty(entity,
                    "意志", "理智", "POW", "Pow", "pow", "SAN", "San", "san")
                    ?: throw Exception("未找到${entity.name} 的理智")

                val result = check(propertyValue)
                val desc = when {
                    propertyValue >= 80 -> "帅气精神"
                    propertyValue >= 60 -> "沉着冷静"
                    propertyValue >= 40 -> "略显不安"
                    propertyValue >= 20 -> "精神萎靡"
                    propertyValue >= 10 -> "近乎癫狂"
                    else -> "绝望盲目"
                }
                output.println("${entity.name}进行了一个${desc}的意志检定：${result.value}->${result.points}")
                if (result.succeed) {
                    val delta = -succeedDice.roll()
                    output.println("是一个成功呢，${entity.name}的意志${if (delta < 0) delta else "+$delta"}")
                } else {
                    val delta = -failDice.roll()
                    output.println("失败了呢，${entity.name}的意志${if (delta < 0) delta else "+$delta"}")
                }
            }
        }
    }
}

fun tryGetProperty(entity: Entity, vararg propertyNames: String): Int? {
    for (propertyName in propertyNames) {
        if (entity.hasProperty(propertyName)) return entity.getProperty(propertyName)
    }
    return null
}