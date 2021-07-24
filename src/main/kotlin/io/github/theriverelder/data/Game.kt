package io.github.theriverelder.data

import io.github.theriverelder.getOrLoadEntity
import io.github.theriverelder.util.Serializable
import java.io.DataInput
import java.io.DataOutput

class Game(public val uid: Long, public var name: String = "") : Serializable {

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

    override fun read(input: DataInput) {
        this.name = input.readUTF()

        controlMap.clear()
        val controlMapSize = input.readInt()
        for (i in 0 until controlMapSize) {
            val playerUid = input.readLong()
            val entityUid = input.readLong()
            controlMap[playerUid] = entityUid
        }
    }

    override fun write(output: DataOutput) {
        output.writeLong(uid)
        output.writeUTF(name)

        output.writeInt(controlMap.size)
        controlMap.forEach {
            output.writeLong(it.key)
            output.writeLong(it.value)
        }
    }
}

fun readGame(input: DataInput): Game {
    val gameUid = input.readLong()
    val game = Game(gameUid)
    game.read(input)
    return game
}