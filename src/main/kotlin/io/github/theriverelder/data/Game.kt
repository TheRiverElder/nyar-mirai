package io.github.theriverelder.data

import io.github.theriverelder.util.Registry
import io.github.theriverelder.util.RegistryItem
import io.github.theriverelder.util.Serializable
import java.io.DataInput
import java.io.DataOutput

class Game(public val uid: Long, public var name: String = "") : Serializable, RegistryItem<Long> {

    val entities: Registry<Long, Entity> = Registry()
    val propertyTypes: Registry<Int, PropertyType> = Registry()
    var dirty: Boolean = true
    var propertyIdCount: Int = 1

    fun genPropId(): Int = propertyIdCount++

    fun registerProperty(name: String, defaultValue: Int = 0, defaultLimit: Int = -1): PropertyType {
        val pt = PropertyType(genPropId(), name, defaultValue, defaultLimit)
        propertyTypes.register(pt)
        return pt
    }

    override fun read(input: DataInput) {
        this.name = input.readUTF()

        this.propertyIdCount = input.readInt()

        entities.clear()
        val entityCount = input.readInt()
        for (i in 0 until entityCount) {
            val entity = readEntity(input, this)
            entities.register(entity)
        }
    }

    override fun write(output: DataOutput) {
        output.writeLong(uid)

        output.writeInt(propertyIdCount)

        output.writeUTF(name)

        output.writeInt(entities.size)
        entities.getAll().forEach { it.write(output) }
    }

    override val key: Long get() = uid
}

fun readGame(input: DataInput): Game {
    val gameUid = input.readLong()
    val game = Game(gameUid)
    game.read(input)
    return game
}