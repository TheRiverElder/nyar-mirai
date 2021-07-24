package io.github.theriverelder.data

import java.lang.StringBuilder
import io.github.theriverelder.util.Serializable
import io.github.theriverelder.util.genUid
import java.io.DataInput
import java.io.DataOutput

class Entity(val uid: Long = genUid(), var name: String = "Entity") : Serializable {

    private val properties: MutableMap<String, Int> = HashMap()

//    public var shown: Boolean = true
    public var dirty: Boolean = true

    public fun setProperty(key: String, value: Int): Int {
        val prev = properties.getOrDefault(key, 0)
        properties[key] = value
        dirty = dirty || prev != value
        return prev
    }

    public fun changeProperty(key: String, delta: Int): Int = setProperty(key, getProperty(key) + delta)

    public fun getProperty(key: String): Int = properties.getOrDefault(key, 0)

    public fun getProperties() = properties.entries

    public fun removeProperty(key: String): Int {
        if (properties.containsKey(key)) {
            dirty = true
            return properties.remove(key) ?: 0
        }
        return 0
    }

    override fun read(input: DataInput) {
        // read name
        this.name = input.readUTF()

        // read properties
        val propertyCount = input.readInt()
        properties.clear()
        for (i in 0 until propertyCount) {
            val key = input.readUTF()
            val value = input.readInt()
            properties[key] = value
        }
    }

    override fun write(output: DataOutput) {
        output.writeLong(uid)
        output.writeUTF(name)

        output.writeInt(properties.size)
        properties.forEach {
            output.writeUTF(it.key)
            output.writeInt(it.value)
        }
    }


    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(name).append("{ ");
        properties.entries.joinTo(builder, ", ") { "${it.key}${it.value}" }
        builder.append("}")
        return builder.toString()
    }

}

fun readEntity(input: DataInput): Entity {
    val uid = input.readLong()
    val entity = Entity(uid)
    entity.read(input)
    return entity
}