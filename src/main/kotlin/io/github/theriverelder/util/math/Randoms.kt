package io.github.theriverelder.util.math

import kotlin.math.floor

fun <T> choose(choices: Array<T>): T {
    return choices[floor(Math.random() * choices.size).toInt()]
}
