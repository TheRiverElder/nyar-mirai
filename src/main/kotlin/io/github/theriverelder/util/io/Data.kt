package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

val DATA_NULL: Byte = 0
val DATA_BYTE: Byte = 1
val DATA_SHORT: Byte = 2
val DATA_INT: Byte = 3
val DATA_LONG: Byte = 4
val DATA_FLOAT: Byte = 5
val DATA_DOUBLE: Byte = 6
val DATA_STRING: Byte = 7
val DATA_MAP: Byte = 8

val DATA_LIST: Byte = 9
val DATA_NUMBER: Byte = 11

val DATA_BYTE_LIST: Byte = 11
val DATA_SHORT_LIST: Byte = 12
val DATA_INT_LIST: Byte = 13
val DATA_LONG_LIST: Byte = 14
val DATA_FLOAT_LIST: Byte = 15
val DATA_DOUBLE_LIST: Byte = 16
val DATA_STRING_LIST: Byte = 17
val DATA_MAP_LIST: Byte = 18

val DATA_BOOLEAN: Byte = 21


interface Data<T> {

//    companion object val NULL: Data = Data(Unit, DATA_NULL)

    val code: Byte
    val value: T

    fun write(output: DataOutput)

    fun read(input: DataInput)

//    private val content: Any
//
//    private constructor(content: Any, code: Byte) {
//        this.content = content
//        this.code = code
//    }
//
//    constructor(content: Byte) : this(content, DATA_BYTE)
//    constructor(content: Short) : this(content, DATA_SHORT)
//    constructor(content: Int) : this(content, DATA_INT)
//    constructor(content: Long) : this(content, DATA_LONG)
//    constructor(content: Float) : this(content, DATA_FLOAT)
//    constructor(content: Double) : this(content, DATA_DOUBLE)
//    constructor(content: String) : this(content, DATA_STRING)
//    constructor(content: Map<String, Data>) : this(content, DATA_MAP)
//
//    constructor(content: Array<Byte>) : this(content, DATA_BYTE_LIST)
//    constructor(content: Array<Short>) : this(content, DATA_SHORT_LIST)
//    constructor(content: Array<Int>) : this(content, DATA_INT_LIST)
//    constructor(content: Array<Long>) : this(content, DATA_LONG_LIST)
//    constructor(content: Array<Float>) : this(content, DATA_FLOAT_LIST)
//    constructor(content: Array<Double>) : this(content, DATA_DOUBLE_LIST)
//    constructor(content: Array<String>) : this(content, DATA_STRING_LIST)
//    constructor(content: Array<Map<String, Data>>) : this(content, DATA_MAP_LIST)
//
//    fun asByte(): Byte = (content as Number).toByte()
//    fun asShort(): Short = (content as Number).toShort()
//    fun asInt(): Int = (content as Number).toInt()
//    fun asLong(): Long = (content as Number).toLong()
//    fun asFloat(): Float = (content as Number).toFloat()
//    fun asDouble(): Double = (content as Number).toDouble()
//    fun asString(): String = content.toString()
//    fun asMap(): Map<String, Data> = content as Map<String, Data>
//
//    fun asByteList(): Array<Byte> = content as Array<Byte>
//    fun asShortList(): Array<Short> = content as Array<Short>
//    fun asIntList(): Array<Int> = content as Array<Int>
//    fun asLongList(): Array<Long> = content as Array<Long>
//    fun asFloatList(): Array<Float> = content as Array<Float>
//    fun asDoubleList(): Array<Double> = content as Array<Double>
//    fun asStringList(): Array<String> = content as Array<String>
//    fun asMapList(): Array<Map<String, Data>> = content as Array<Map<String, Data>>
//
//    fun write(output: DataOutput) {
//        output.writeByte(code.toInt())
//        when (code) {
//            DATA_BYTE -> output.writeByte(asByte().toInt())
//            DATA_SHORT -> output.writeByte(asShort().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//            DATA_BYTE_LIST -> output.writeByte(asByte().toInt())
//        }
//    }


}