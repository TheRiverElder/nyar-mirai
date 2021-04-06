package io.github.theriverelder.data

import io.github.theriverelder.GAMES
import io.github.theriverelder.util.RegistryItem
import io.github.theriverelder.util.Serializable
import java.io.DataInput
import java.io.DataOutput

class GameGroup(val uid: Long) : RegistryItem<Long>, Serializable {

    public var dirty: Boolean = false

    override val key: Long get() = uid

    public var game: Game? = null

    // 记录玩家与其所控制角色的映射表
    private val controlMap: MutableMap<Long, Entity> = HashMap()

    // 设置玩家控制的角色，若原先已有控制的角色
    // 若被控制角色UID为空，则去除该玩家的控制
    public fun setControl(playerUid: Long, entity: Entity?): Boolean {
        if (entity != null) {
            if (game == null || !game?.entities?.has(entity.key)!!) return false
            controlMap[playerUid] = entity
        } else {
            controlMap.remove(playerUid)
        }
        return true
    }

    public fun getControl(playerUid: Long): Entity? = controlMap[playerUid]

    override fun read(input: DataInput) {
        val gameUid = input.readLong()
        val game = GAMES[gameUid] ?: return
        this.game = game

        controlMap.clear()
        val controlMapSize = input.readInt()
        for (i in 0 until controlMapSize) {
            val playerUid = input.readLong()
            val entityUid = input.readLong()

            val entity = game.entities[entityUid] ?: return
            controlMap.put(playerUid, entity)
        }
    }

    override fun write(output: DataOutput) {
        output.writeLong(uid)

        output.writeLong(game?.uid ?: 0L)

        output.writeInt(controlMap.size)
        controlMap.forEach { (playerUid, entity) ->
            output.writeLong(playerUid)
            output.writeLong(entity.uid)
        }
    }


}

fun readGameGroup(input: DataInput) {
    val uid = input.readLong()
    val group = GameGroup(uid)
    return group.read(input)
}