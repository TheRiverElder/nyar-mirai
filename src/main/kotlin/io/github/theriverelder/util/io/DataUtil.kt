package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

fun makeData(code: Byte): Data<*> {
    return when (code) {
        DATA_INT -> IntData()
        DATA_LONG -> LongData()
        DATA_DOUBLE -> DoubleData()
        DATA_LIST -> ListData()
        DATA_MAP -> MapData()
        DATA_STRING -> StringData()
        DATA_BOOLEAN -> BooleanData()
        else -> throw Exception("Unorganized code: $code")
    }
}

fun readData(input: DataInput): Data<*> {
    val data: Data<*> = makeData(input.readByte())
    data.read(input)
    return data
}

fun writeData(output: DataOutput, data: Data<*>) {
    output.writeByte(data.code.toInt())
    data.write(output)
}


fun readMapData(input: DataInput): MapData {
    val data = MapData()
    data.read(input)
    return data
}

fun writeMapData(output: DataOutput, data: MapData) {
    data.write(output)
}