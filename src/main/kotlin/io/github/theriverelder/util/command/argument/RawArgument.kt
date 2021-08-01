package io.github.theriverelder.util.command.argument

class RawArgument(val input: String, val position: Int, private val str: String) {
    fun getString(): String = this.str

    fun slice(): String = input.slice(0 until position)
}