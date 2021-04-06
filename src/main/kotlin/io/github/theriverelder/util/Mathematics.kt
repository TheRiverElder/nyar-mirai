package io.github.theriverelder.util

import io.github.theriverelder.util.command.SafeReader

val FACTORIAL_CACHE: Map<Int, Int> = HashMap(16)

fun Int.factorial(): Int {
    val num = coerceAtLeast(1)
    val cache = FACTORIAL_CACHE[num]
    if (cache != null) return cache

    var result = 1
    for (i in 2..num) {
        result *= i
    }
    return result
}

private var idCount: Long = System.currentTimeMillis()

fun genUid(): Long = ++idCount
