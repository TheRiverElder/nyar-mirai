package io.github.theriverelder.data

import io.github.theriverelder.getOrLoadEntity
import io.github.theriverelder.util.io.Serializable
import io.github.theriverelder.util.io.ListData
import io.github.theriverelder.util.io.LongData
import io.github.theriverelder.util.io.MapData
import io.github.theriverelder.util.io.StringData

class Game(public var uid: Long = 0, public var name: String = "") : Serializable {

//    val propertyTypes: Registry<Int, PropertyType> = Registry()
    var dirty: Boolean = true
//    var propertyIdCount: Int = 0
    // 记录玩家与其所控制角色的映射表
    private val controlMap: MutableMap<Long, Long> = HashMap()

//    fun genPropId(): Int {
//        val id = propertyIdCount++
//        dirty = true
//        return id
//    }
//
//    fun registerProperty(name: String, defaultValue: Int = 0, defaultLimit: Int = -1): PropertyType {
//        val pt = PropertyType(genPropId(), name, defaultValue, defaultLimit)
//        dirty = true
//        propertyTypes.register(pt)
//        return pt
//    }
//
//    fun unregisterProperty(prop: PropertyType): Boolean {
//        if (propertyTypes.unregister(prop) != null) {
//            dirty = true
//            return true
//        }
//        return false
//    }

    // 设置玩家控制的角色，若原先已有控制的角色
    // 若被控制角色UID为空，则去除该玩家的控制
    public fun setControl(playerUid: Long, entityUid: Long): Boolean {
        if (entityUid != 0L) {
            controlMap[playerUid] = entityUid
            dirty = true
        } else {
            controlMap.remove(playerUid)
            dirty = true
        }
        return true
    }

    public fun hasControl(playerUid: Long): Boolean = controlMap.containsKey(playerUid)

    public fun getControl(playerUid: Long): Entity {
        val entityUid: Long = controlMap[playerUid] ?: throw Exception("玩家（#${playerUid}）未与实体未绑定")
        return getOrLoadEntity(entityUid)
    }

    public fun getInvolvedEntities(): List<Entity> {
        return controlMap.values.map { getOrLoadEntity(it) }
    }

    override fun read(input: MapData) {
        uid = input.getLong("uid")
        name = input.getString("name")

        controlMap.clear()
        for (i in input.getList("controlMap").value) {
            if (i is ListData) {
                controlMap[i.getLong(0)] = i.getLong(1)
            }
        }
        dirty = true
    }

    override fun write(output: MapData) {
        output.value["uid"] = LongData(uid)
        output.value["name"] = StringData(name)
        output.value["controlMap"] = ListData(
            *controlMap.map { ListData(LongData(it.key), LongData(it.value)) }.toTypedArray()
        )
        dirty = false
    }
}