package io.github.theriverelder.data

import io.github.theriverelder.util.RegistryItem

data class PropertyType(
    val code: Int,
    val name: String,
    val defaultValue: Int = 0,
    val defaultLimit: Int = -1,
) : RegistryItem<Int> {
    override val key: Int get() = code
}