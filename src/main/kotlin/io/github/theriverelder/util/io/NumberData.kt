package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class NumberData : Data<Number> {

    override val code: Byte = DATA_NUMBER
    override var value: Number = 0

    override fun write(output: DataOutput) {
        output.write(value.toInt())
    }

    override fun read(input: DataInput) {
        TODO("Not yet implemented")
    }
}