package io.github.theriverelder.util.command

import io.github.theriverelder.data.CommandEnv

class NyarCommandContext<E>(val env: E) {

    val values: MutableMap<String, Any> = HashMap()

    fun put(key: String, value: Any) {
        values[key] = value
    }

    inline fun <reified T> get(key: String): T? {
        val value: Any? = values[key]
        return if (value is T) value else null
    }

    inline fun <reified T> getOrDefault(key: String, defaultValue: T): T {
        val value: Any? = values[key]
        return if (value is T) value else defaultValue
    }

    inline fun <reified T> getOrElse(key: String, defaultValueProvider: () -> T): T {
        val value: Any? = values[key]
        return if (value is T) value else defaultValueProvider()
    }

}