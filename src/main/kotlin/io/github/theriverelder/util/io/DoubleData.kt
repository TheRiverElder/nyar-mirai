package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class DoubleData(override var value: Double = 0.0) : Data<Double> {

    override val code: Byte = DATA_DOUBLE

    override fun write(output: DataOutput) {
        output.writeDouble(value)
    }

    override fun read(input: DataInput) {
        value = input.readDouble()
    }
}