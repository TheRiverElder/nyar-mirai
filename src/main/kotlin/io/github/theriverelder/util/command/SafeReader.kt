package io.github.theriverelder.util.command

class SafeReader(val data: String) {

    var pointer: Int = 0

    fun hasMore(): Boolean = pointer < data.length

    fun read(): Char = data[pointer++]

    fun peek(): Char = data[pointer]

    fun readWord(): String? {
        if (!hasMore()) return null

        val start = pointer
        while (hasMore() && !Character.isWhitespace(peek())) {
            read()
        }
        return if (pointer == start) null else data.substring(start, pointer)
    }

    fun slice(start: Int = 0, end: Int = pointer): String = data.substring(start, end)

}