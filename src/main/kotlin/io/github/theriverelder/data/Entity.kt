package io.github.theriverelder.data

import java.lang.StringBuilder
import io.github.theriverelder.util.io.Serializable
import io.github.theriverelder.util.io.*

class Entity(var uid: Long = 0, var name: String = "Entity") : Serializable {

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

    public fun hasProperty(key: String): Boolean = properties.containsKey(key)

    public fun getProperty(key: String): Int = properties.getOrDefault(key, 0)

    public fun getProperties() = properties.entries

    public fun removeProperty(key: String): Int {
        if (properties.containsKey(key)) {
            dirty = true
            return properties.remove(key) ?: 0
        }
        return 0
    }

    override fun read(input: MapData) {
        uid = input.getLong("uid")
        name = input.getString("name")

        properties.clear()
        for (i in input.getList("properties").value) {
            if (i is ListData) {
                properties[i.getString(0)] = i.getInt(1)
            }
        }
        dirty = true
    }

    override fun write(output: MapData) {
        output.value["uid"] = LongData(uid)
        output.value["name"] = StringData(name)
        output.value["properties"] = ListData(
            *properties.map { ListData(StringData(it.key), IntData(it.value)) }.toTypedArray()
        )
        dirty = false
    }


    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(name).append("{ ");
        properties.entries.joinTo(builder, ", ") { "${it.key}${it.value}" }
        builder.append("}")
        return builder.toString()
    }

}