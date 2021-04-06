package io.github.theriverelder.util

import java.io.DataInput
import java.io.DataOutput

interface Serializable {
    fun read(input: DataInput)
    fun write(output: DataOutput)
}