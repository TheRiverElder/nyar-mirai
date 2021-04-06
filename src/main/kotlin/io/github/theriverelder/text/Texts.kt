package io.github.theriverelder.text

import okhttp3.internal.format

abstract class TextComponent {
    abstract fun render(): String

    open operator fun plus(other: TextComponent): TextComponent = ComplexTextComponent(arrayOf(this, other))
}

class ComplexTextComponent(val content: Array<TextComponent>, val separator: String = "") : TextComponent() {
    override fun render(): String = content.joinToString(separator) { it.render() }

    override operator fun plus(other: TextComponent): TextComponent = ComplexTextComponent(content + arrayOf(other), separator)
}

class PlainTextComponent(val content: String) : TextComponent() {
    override fun render(): String = content
}

class TranslateTextComponent(val key: String, val data: Map<String, String>) : TextComponent() {

    constructor(key: String, d: List<String>) : this(key, mapOf(*d.mapIndexed { i, v -> Pair(i.toString(), v) }.toTypedArray()))

    override fun render(): String =
        getPattern().replace(Regex("\\$\\{(\\w+)}")) { data[it.groups[1]?.value] ?: (it.groups[1]?.value).toString() }

    fun getPattern(): String {
        return key
    }
}

