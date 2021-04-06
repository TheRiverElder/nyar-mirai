package io.github.theriverelder.data

import io.github.theriverelder.util.RegistryItem
import io.github.theriverelder.util.Serializable
import java.io.DataInput
import java.io.DataOutput
import java.lang.Integer.min

class PropertyValue(
    val type: PropertyType,
    var limit: Int = type.defaultLimit,
    initialValue: Int = type.defaultValue,
) : RegistryItem<PropertyType>, Serializable {

    override val key: PropertyType get() = type

    var value = initialValue
        set(value) {
            field = if (limit <= 0) value else min(value, limit)
        }

    override fun read(input: DataInput) {
        this.value = input.readInt()
        this.limit = input.readInt()
    }

    override fun write(output: DataOutput) {
        output.writeInt(type.code)
        output.writeInt(value)
        output.writeInt(limit)
    }

    override fun toString(): String {
        return type.name + "=" + value + (if (limit >= 0) "/$limit" else "")
    }

}