package io.github.theriverelder.util.command;

import io.github.theriverelder.util.command.argument.RawArgument

public class RawArgumentReader(val rawArguments: List<RawArgument>) {

    var pointer: Int = 0

    fun read(): RawArgument {
        return rawArguments[pointer++]
    }

    fun hasMore(): Boolean = pointer < rawArguments.size

    fun slice(): String {
        if (rawArguments.isEmpty()) return ""
        if (pointer >= rawArguments.size)
            return with(rawArguments[rawArguments.size - 1]) { slice() + getString() }
        return rawArguments[pointer].slice()
    }
}
