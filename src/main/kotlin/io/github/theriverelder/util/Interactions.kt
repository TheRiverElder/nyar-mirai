package io.github.theriverelder.util

import java.io.PrintWriter
import java.io.Writer
import java.lang.StringBuilder

class StringBuilderWriter(val builder: StringBuilder) : Writer() {

    override fun close() = Unit

    override fun flush() = Unit

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        builder.append(cbuf, off, len)
    }
}

fun createOutput(builder: StringBuilder): PrintWriter = PrintWriter(StringBuilderWriter(builder))