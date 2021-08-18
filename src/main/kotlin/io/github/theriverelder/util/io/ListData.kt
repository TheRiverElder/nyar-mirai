package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class ListData(vararg value: Data<*>) : Data<MutableList<Data<*>>> {

    override val code: Byte = DATA_LIST

    override var value: MutableList<Data<*>> = value.toMutableList()

    override fun write(output: DataOutput) {
        output.writeInt(value.size)
        value.forEach { writeData(output, it) }
    }

    override fun read(input: DataInput) {
        value.clear()
        for (i in 0 until input.readInt()) {
            value.add(readData(input))
        }
    }
    
    fun getInt(index: Int, default: Int = 0): Int = value[index].let { if (it is IntData) it.value else default }
    fun getLong(index: Int, default: Long = 0): Long = value[index].let { if (it is LongData) it.value else default }
    fun getDouble(index: Int, default: Double = 0.0): Double = value[index].let { if (it is DoubleData) it.value else default }
    fun getString(index: Int, default: String = ""): String = value[index].let { if (it is StringData) it.value else default }
    fun getMap(index: Int, default: MapData = MapData()): MapData = value[index].let { if (it is MapData) it else default }
    fun getList(index: Int, default: ListData = ListData()): ListData = value[index].let { if (it is ListData) it else default }

}