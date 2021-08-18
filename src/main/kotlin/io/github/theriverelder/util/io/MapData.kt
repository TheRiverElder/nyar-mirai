package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class MapData(override var value: MutableMap<String, Data<*>> = HashMap()) : Data<MutableMap<String, Data<*>>> {

    override val code: Byte = DATA_MAP

    override fun write(output: DataOutput) {
        output.writeInt(value.size)
        value.forEach {
            output.writeUTF(it.key)
            writeData(output, it.value)
        }
    }

    override fun read(input: DataInput) {
        value.clear()
        for (i in 0 until input.readInt()) {
            val key = input.readUTF()
            val v = readData(input)
            value[key] = v
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean = value[key]?.let { if (it is BooleanData) it.value else default } ?: default
    fun getInt(key: String, default: Int = 0): Int = value[key]?.let { if (it is IntData) it.value else default } ?: default
    fun getLong(key: String, default: Long = 0): Long = value[key]?.let { if (it is LongData) it.value else default } ?: default
    fun getDouble(key: String, default: Double = 0.0): Double = value[key]?.let { if (it is DoubleData) it.value else default } ?: default
    fun getString(key: String, default: String = ""): String = value[key]?.let { if (it is StringData) it.value else default } ?: default
    fun getMap(key: String, default: MapData = MapData()): MapData = value[key]?.let { if (it is MapData) it else default } ?: default
    fun getList(key: String, default: ListData = ListData()): ListData = value[key]?.let { if (it is ListData) it else default } ?: default

}