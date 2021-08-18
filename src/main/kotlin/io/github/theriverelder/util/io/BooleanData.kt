package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class BooleanData(override var value: Boolean = false) : Data<Boolean> {

    override val code: Byte = DATA_BOOLEAN

    override fun write(output: DataOutput) {
        output.writeBoolean(value)
    }

    override fun read(input: DataInput) {
        value = input.readBoolean()
    }
}