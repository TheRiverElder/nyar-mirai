package io.github.theriverelder.util.io

interface Serializable {
    fun read(input: MapData)
    fun write(output: MapData)
}