package io.github.theriverelder.util.command

class SafeReader(val data: String) {

    var pointer: Int = 0

    fun hasMore(): Boolean = pointer < data.length

    fun read(): Char = data[pointer++]

    fun peek(): Char = data[pointer]

    fun slice(start: Int = 0, end: Int = pointer): String = data.substring(start, end)

}