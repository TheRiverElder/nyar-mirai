package io.github.theriverelder.util.command;

import io.github.theriverelder.util.command.argument.RawArgument

public class RawArgumentReader(val rawArguments: List<RawArgument>) {

    var pointer: Int = 0

    fun read(): RawArgument {
        return rawArguments[pointer++]
    }

    fun hasMore(): Boolean = pointer < rawArguments.size

    fun slice(): String {
        val index = pointer.coerceAtMost(rawArguments.size - 1)
        return if (index == 0) "" else rawArguments[index].slice()
    }
}
