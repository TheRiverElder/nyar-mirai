package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class IntData(override var value: Int = 0) : Data<Int> {

    override val code: Byte = DATA_INT

    override fun write(output: DataOutput) {
        output.writeInt(value)
    }

    override fun read(input: DataInput) {
        value = input.readInt()
    }
}