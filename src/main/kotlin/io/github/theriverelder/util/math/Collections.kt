package io.github.theriverelder.util.math

import java.io.DataOutput

fun <K, V> Map<K, V>.write(output: DataOutput, keyWriter: (K) -> Nothing, valueWriter: (V) -> Nothing) {
    output.writeInt(size)
}
