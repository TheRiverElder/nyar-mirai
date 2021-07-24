package io.github.theriverelder.util

import kotlin.math.floor

fun <T> choose(choices: Array<T>): T {
    return choices[floor(Math.random() * choices.size).toInt()]
}
