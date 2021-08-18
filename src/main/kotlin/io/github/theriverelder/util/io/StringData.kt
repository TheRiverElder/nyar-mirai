package io.github.theriverelder.util.io

import java.io.DataInput
import java.io.DataOutput

class StringData(override var value: String = "") : Data<String> {

    override val code: Byte = DATA_STRING

    override fun write(output: DataOutput) {
        output.writeUTF(value)
    }

    override fun read(input: DataInput) {
        value = input.readUTF()
    }
}