package io.github.theriverelder.util.command

import io.github.theriverelder.util.command.argument.*
import kotlin.text.StringBuilder

class DispatchResult(
    val input: String,
    val commands: Array<BufferedCommand<*>>,
    val hints: Array<String>,
    val succeed: Boolean,
    val best: BufferedCommand<*>?,
)

val QUOTE_PAIRS = mapOf(
    Pair('"', '"'),
    Pair('\'', '\''),
    Pair('“', '”'),
    Pair('‘', '’'),
)

val RawArgument = String

class NyarCommandDispatcher<E> {

    private val rootParser: RootArgumentParser<E> = RootArgumentParser()

    fun register(command: LiteralArgumentNode<E>): NyarCommandDispatcher<E> {
        rootParser.add(command)
        return this
    }

    fun register(command: ArgumentNode<E>, init: ArgumentNodeInitiator<E>? = null): NyarCommandDispatcher<E> {
        if (init != null) {
            command.init()
        }
        rootParser.add(command)
        return this
    }

    fun getAllCommands(): Array<LiteralArgumentNode<E>> = rootParser.getAllCommands()

    fun getCommand(head: String): LiteralArgumentNode<E>? = rootParser.getCommand(head)

    fun dispatch(raw: String, env: E): DispatchResult {
        val reader = SafeReader(raw)
        val rawArguments = readArguments(reader)
        val rawArgumentReader = RawArgumentReader(rawArguments)
        val res: MutableList<BufferedCommand<*>> = ArrayList()
        val hints: MutableList<String> = ArrayList()
        rootParser.parse(rawArgumentReader, res, hints, env)
        return DispatchResult(
            reader.slice(),
            res.toTypedArray(),
            hints.toTypedArray(),
            res.size > 0,
            if (res.size > 0) res[0] else null,
        )
    }

    fun readArguments(reader: SafeReader): List<RawArgument> {
        val result: MutableList<RawArgument> = ArrayList()

        reader.skipWhitespace()
        while (reader.hasMore()) {
            val start = reader.pointer

            val argumentString: String =
                reader.readQuotedString()
                ?: reader.readWord()
                ?: throw Exception("参数解析错误：@${start}")

            result.add(RawArgument(reader.data, start, argumentString))
            reader.skipWhitespace()
        }

        if (reader.hasMore() || result.size == 0) throw Exception("参数不完整")

        return result
    }

}

fun SafeReader.readQuotedString(): String? {
    if (peek() in QUOTE_PAIRS.keys) {
        val start = pointer
        val endQuote = QUOTE_PAIRS[read()]
        val builder = StringBuilder()
        var escaped = false

        while (hasMore()) {
            val ch = read()
            if (escaped) {
                val r = when (ch) {
                    'n' -> '\n'
                    't' -> '\t'
                    'r' -> '\r'
                    '\\' -> '\\'
                    else -> ch
                }
                builder.append(r)
                escaped = false
            } else {
                when (ch) {
                    '\\' -> escaped = true
                    endQuote -> return builder.toString()
                    else -> builder.append(ch)
                }
            }
        }

        throw Exception("不完整的引号字符串：@${start}")
    } else {
        return null
    }
}

fun SafeReader.readWord(): String? {
    if (!hasMore()) return null

    val start = pointer
    while (hasMore() && !Character.isWhitespace(peek())) {
        read()
    }
    return if (pointer == start) null else data.substring(start, pointer)
}

fun SafeReader.skipWhitespace() {
    while (hasMore() && Character.isWhitespace(peek())) {
        read()
    }
}

fun String.toRawArgumentString(): String {
    val builder: StringBuilder = StringBuilder()
    var hasEscaped = false
    for (ch in this) {
        var thisChEscaped: Boolean = true
        builder.append(when (ch) {
            '\n' -> "\\n"
            '\t' -> "\\t"
            '\r' -> "\\r"
            '\\' -> "\\\\"
            '"' -> "\\\""
            else -> {
                thisChEscaped = false
                ch
            }
        })
        hasEscaped = hasEscaped || thisChEscaped
    }
    return if (hasEscaped) "\"$builder\"" else builder.toString();
}