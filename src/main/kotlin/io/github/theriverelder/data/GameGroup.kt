package io.github.theriverelder.data

import io.github.theriverelder.getOrLoadGame
import io.github.theriverelder.tryLoadGame
import io.github.theriverelder.util.Serializable
import java.io.DataInput
import java.io.DataOutput

class GameGroup(val uid: Long) : Serializable {

    public var dirty: Boolean = true

    public var gameUid: Long = 0
        set(value) {
            dirty = dirty || field != value
            field = value
        }

    public val game: Game
        get() {
            if (gameUid == 0L) throw Exception("群（#${uid}）未绑定团")
            return getOrLoadGame(this.gameUid)
        }

    override fun read(input: DataInput) {
        gameUid = input.readLong()
    }

    override fun write(output: DataOutput) {
        output.writeLong(uid)
        output.writeLong(gameUid)
    }


}

fun readGameGroup(input: DataInput): GameGroup {
    val uid = input.readLong()
    val group = GameGroup(uid)
    group.read(input)
    return group
}