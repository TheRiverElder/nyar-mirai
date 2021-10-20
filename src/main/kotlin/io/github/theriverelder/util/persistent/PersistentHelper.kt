package io.github.theriverelder.util.persistent


interface PersistentHelper<TKey, TValue> {
    fun load(key: TKey): TValue
    fun save(value: TValue)
}