package io.github.theriverelder.util.command.argument

class ChainArgumentBuffer(val parent: ChainArgumentBuffer?, val key: String, val value: Any) {

    fun derive(key: String, value: Any): ChainArgumentBuffer = ChainArgumentBuffer(this, key, value)


}