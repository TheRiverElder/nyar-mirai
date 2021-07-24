package io.github.theriverelder.util

import java.util.function.Function

open class Registry<K, V>(private val keyGetter: Function<V, K>) {

    private val defaultValue: V? = null
    private val data: MutableMap<K, V> = HashMap()

    public open fun register(item: V) {
        data[keyGetter.apply(item)] = item
    }

    public fun unregisterItem(item: V): V? = unregisterKey(keyGetter.apply(item))

    public open fun unregisterKey(key: K): V? = data.remove(key)

    public fun has(key: K): Boolean = data.containsKey(key)

    public fun hasItem(item: V): Boolean = data.containsValue(item)

    public operator fun get(key: K): V? = data[key]

    public fun getAll(): Collection<V> = data.values

    public fun getOrDefault(key: K): V? = data[key] ?: defaultValue

    public fun getOrDefault(key: K, default: V): V = data[key] ?: default

    public fun clear() = data.clear()

    public val size: Int get() = data.size

    public val items: Collection<V> get() = data.values

}

typealias RegisterEventHandler<K, V> = SubscribeRegistry<K, V>.(V) -> Nothing
typealias UnregisterEventHandler<K, V> = SubscribeRegistry<K, V>.(V) -> Nothing

class SubscribeRegistry<K, V>(keyGetter: Function<V, K>) : Registry<K, V>(keyGetter) {

    private val registerEventHandlers: MutableSet<RegisterEventHandler<K, V>> = HashSet()
    private val unregisterEventHandlers: MutableSet<UnregisterEventHandler<K, V>> = HashSet()

    fun addRegisterEventHandler(handler: RegisterEventHandler<K, V>): SubscribeRegistry<K, V> {
        registerEventHandlers.add(handler)
        return this
    }

    fun addUnregisterEventHandler(handler: UnregisterEventHandler<K, V>): SubscribeRegistry<K, V> {
        unregisterEventHandlers.add(handler)
        return this
    }

    override fun register(item: V): Unit {
        super.register(item)
        registerEventHandlers.forEach { it(item) }
    }

    override fun unregisterKey(key: K): V? {
        val r = super.unregisterKey(key)
        if (r != null) {
            unregisterEventHandlers.forEach { it(r) }
        }
        return r
    }
}