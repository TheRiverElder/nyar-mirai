package io.github.theriverelder.util.persistent

import io.github.theriverelder.GAMES
import io.github.theriverelder.SaveConfig
import io.github.theriverelder.data.Game
import io.github.theriverelder.util.Registry
import io.github.theriverelder.util.io.MapData
import io.github.theriverelder.util.io.Serializable
import io.github.theriverelder.util.io.readMapData
import io.github.theriverelder.util.io.writeMapData
import java.io.*

class FormatIOPersistentHelper<TValue : Serializable>(
    val registry: Registry<Long, TValue>,
    val directory: File,
    val create: () -> TValue,
    val getKey: (TValue) -> Long,
) : PersistentHelper<Long, TValue> {

    private fun ensureDirectoryExist() {
        if (!directory.exists()) {
            if (!directory.mkdirs()) throw Exception("$directory 文件夹创建失败！")
        } else if (!directory.isDirectory) throw Exception("$directory 不是文件夹！")
    }

    override fun load(key: Long): TValue {
        ensureDirectoryExist()

        val fileName: String = key.toString()
        val file = File(directory, fileName)

        val stream = FileInputStream(file)
        val value = create()
        val data = readMapData(DataInputStream(stream))
        value.read(data)
        stream.close()
        registry.register(value)
        return value
    }

    override fun save(value: TValue) {
        val key: Long = getKey(value)
        val fileName: String = key.toString()

        val file = File(directory, fileName)
        val data = MapData()
        value.write(data)
        val output = DataOutputStream(FileOutputStream(file))
        writeMapData(output, data)
        output.close()
    }

}