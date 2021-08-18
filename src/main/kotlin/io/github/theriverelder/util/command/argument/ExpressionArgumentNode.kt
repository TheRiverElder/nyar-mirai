package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.RawArgumentReader
import io.github.theriverelder.util.math.factorial
import java.lang.Double.parseDouble
import java.util.*
import kotlin.math.pow


val PART_REG = Regex("([+\\-*/%^()!])|(\\d+(\\.\\d+)?)")

class ExpressionArgumentNode<E>(key: String, val digit: Boolean = false, processor: ArgumentProcessor<E>? = null) : ArgumentNode<E>(key, processor = processor) {

    override fun doParse(reader: RawArgumentReader, buffer: ChainArgumentBuffer): Number? {
        if (!reader.hasMore()) return null

        val str = reader.read().getString().replace(Regex("\\s+"), "")


        val valueStack = Stack<Number>()
        val operatorStack = Stack<Operator>()

        var prevIsNumber = false
        var prevOperator = Operator.POSITIVE
        var r = PART_REG.find(str)
        while (r != null) {
            val groups = r.groups
            if (groups[2]?.value != null) {
                if (!prevIsNumber && (prevOperator == Operator.RIGHT_PAREN || prevOperator == Operator.FACTORIAL)) {
                    if (pushOperator(valueStack, operatorStack, Operator.MULTIPLY, digit)) return null
                }
                valueStack.push(parseDouble(r.value))
                prevIsNumber = true
            } else {
                val ch = r.value[0]
                val operator: Operator = when {
                    ch == '(' -> Operator.LEFT_PAREN
                    ch == ')' -> Operator.RIGHT_PAREN
                    ch == '!' -> Operator.FACTORIAL
                    prevIsNumber -> BINARY_OPERATOR_SIGNS[ch] ?: return null
                    else -> UNARY_OPERATOR_SIGNS[ch] ?: return null
                }

                if (!pushOperator(valueStack, operatorStack, operator, digit)) return null
                prevIsNumber = operator == Operator.RIGHT_PAREN || operator == Operator.FACTORIAL || false
                prevOperator = operator
            }
            r = r.next()
        }
        while (operatorStack.isNotEmpty()) {
            popOperator(valueStack, operatorStack, digit)
        }

//        val value = reader.readAndCalculateExpression(digit)
        return if (valueStack.size != 1) null else valueStack.pop()
    }

    override fun getValueHint(): String = "表达式"

}

fun pushOperator(valueStack: Stack<Number>, operatorStack: Stack<Operator>, operator: Operator, digit: Boolean): Boolean {
    if (operator == Operator.RIGHT_PAREN) {
        while (operatorStack.isNotEmpty() && operatorStack.peek() != Operator.LEFT_PAREN) {
            popOperator(valueStack, operatorStack, digit)
        }
        if (operatorStack.isEmpty() || operatorStack.pop() != Operator.LEFT_PAREN) {
            return false
        }
    } else if (operatorStack.isEmpty() || operator.level > operatorStack.peek().level || operator == Operator.LEFT_PAREN) {
        operatorStack.push(operator)
    } else {
        while (operatorStack.isNotEmpty() && operator.level <= operatorStack.peek().level) {
            popOperator(valueStack, operatorStack, digit)
        }
        operatorStack.push(operator)
    }
    if (operator == Operator.FACTORIAL) return popOperator(valueStack, operatorStack, digit)
    return true
}

fun popOperator(valueStack: Stack<Number>, operatorStack: Stack<Operator>, digit: Boolean): Boolean {
    if (valueStack.isEmpty()) return false
    val v1 = valueStack.pop()

    if (operatorStack.isEmpty()) return false
    val operator = operatorStack.pop()
    if (!operator.isUnary && valueStack.isEmpty()) return false

    val r: Number = when (operator) {
        Operator.POSITIVE -> v1
        Operator.NEGATIVE -> -v1.toDouble()
        Operator.FACTORIAL -> v1.toInt().factorial()
        Operator.ADD -> valueStack.pop().toDouble() + v1.toDouble()
        Operator.SUBTRACT -> valueStack.pop().toDouble() - v1.toDouble()
        Operator.MULTIPLY -> valueStack.pop().toDouble() * v1.toDouble()
        Operator.DIVIDE -> valueStack.pop().toDouble() / v1.toDouble()
        Operator.MODULO -> valueStack.pop().toDouble() % v1.toDouble()
        Operator.POWER -> valueStack.pop().toDouble().pow(v1.toDouble())
        else -> return false
    }
    valueStack.push(r)
    return true
}

val BINARY_OPERATOR_SIGNS: Map<Char, Operator> = Operator.values().filter { !it.isUnary }.map { Pair(it.literal, it) }.toMap()
val UNARY_OPERATOR_SIGNS: Map<Char, Operator> = Operator.values().filter { it.isUnary }.map { Pair(it.literal, it) }.toMap()

enum class Operator(val literal: Char, val level: Int, val isUnary: Boolean = false) {
    POSITIVE('+', 2, true),
    NEGATIVE('-', 2, true),
    FACTORIAL('!', 5, true),
    ADD('+', 1),
    SUBTRACT('-', 1),
    MULTIPLY('*', 3),
    DIVIDE('/', 3),
    MODULO('%', 3),
    POWER('^', 4),
    LEFT_PAREN('(', 0),
    RIGHT_PAREN(')', 8),
    ;
}