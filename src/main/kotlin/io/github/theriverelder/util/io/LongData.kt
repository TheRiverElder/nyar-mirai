package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class LongData(override var value: Long = 0) : Data<Long> {

    override val code: Byte = DATA_LONG

    override fun write(output: DataOutput) {
        output.writeLong(value)
    }

    override fun read(input: DataInput) {
        value = input.readLong()
    }
}