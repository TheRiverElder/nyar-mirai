package io.github.theriverelder.util.command.argument

import io.github.theriverelder.util.command.CommandCollection
import io.github.theriverelder.util.command.HintCollection
import io.github.theriverelder.util.command.RawArgumentReader

interface ArgumentParser<E> {
    fun parse(reader: RawArgumentReader, buffer: ChainArgumentBuffer, res: CommandCollection, hints: HintCollection, env: E): Boolean

    fun getHelp(proceeding: String, res: MutableList<String>)
}