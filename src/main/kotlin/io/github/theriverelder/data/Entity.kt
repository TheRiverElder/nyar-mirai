package io.github.theriverelder.data

import io.github.theriverelder.util.Registry
import io.github.theriverelder.util.RegistryItem
import java.lang.StringBuilder
import io.github.theriverelder.util.Serializable
import io.github.theriverelder.util.genUid
import java.io.DataInput
import java.io.DataOutput

class Entity(val uid: Long = genUid(), public val game: Game, var name: String = "Entity") : RegistryItem<Long>, Serializable {

    private val properties: Registry<PropertyType, PropertyValue> = Registry()

    public var shown: Boolean = true

    public fun setProperty(key: PropertyType, value: Int): Int {
        val v = getOrCreateProperty(key)
        val prev = v.value
        v.value = value
        return prev
    }

    public fun changeProperty(key: PropertyType, delta: Int): Int = setProperty(key, getOrCreateProperty(key).value + delta)

    public fun getProperty(key: PropertyType): PropertyValue? = properties[key]

    public fun getOrCreateProperty(key: PropertyType): PropertyValue {
        var v = properties[key]
        if (v != null) return v
        v = PropertyValue(key)
        properties.register(v)
        return v
    }

    public fun removeProperty(key: PropertyType): PropertyValue? = properties.unregister(key)

    override fun read(input: DataInput) {
        // read name
        this.name = input.readUTF()

        // read properties
        val propertyCount = input.readInt()
        properties.clear()
        for (i in 0 until propertyCount) {
            val code = input.readInt()
            val key = game.propertyTypes[code] ?: continue
            val prop = PropertyValue(key)
            prop.read(input)
            properties.register(prop)
        }
    }

    override fun write(output: DataOutput) {
        output.writeUTF(name)
        output.writeLong(uid)

        output.writeInt(properties.size)
        properties.getAll().forEach { it.write(output) }
    }


    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(name).append("{ ");
        properties.getAll().joinTo(builder, ", ")
        builder.append("}")
        return builder.toString()
    }

    override val key get() = uid

}

fun readEntity(input: DataInput, game: Game): Entity {
    val uid = input.readLong()
    val entity = Entity(uid, game)
    entity.read(input)
    return entity
}