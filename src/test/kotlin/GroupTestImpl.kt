package io.github.theriverelder

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.ContactList
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.GroupSettings
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.Voice
import net.mamoe.mirai.utils.ExternalResource
import kotlin.coroutines.CoroutineContext

class GroupTestImpl(
    override val id: Long,
    override var name: String,
) : Group {
    override val bot: Bot
        get() = TODO("Not yet implemented")
    override val botAsMember: NormalMember
        get() = TODO("Not yet implemented")
    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
    override val members: ContactList<NormalMember>
        get() = TODO("Not yet implemented")
    override val owner: NormalMember
        get() = TODO("Not yet implemented")
    override val settings: GroupSettings
        get() = TODO("Not yet implemented")

    override fun contains(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): NormalMember? {
        TODO("Not yet implemented")
    }

    override suspend fun quit(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message): MessageReceipt<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun setEssenceMessage(source: MessageSource): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(resource: ExternalResource): Image {
        TODO("Not yet implemented")
    }

    override suspend fun uploadVoice(resource: ExternalResource): Voice {
        TODO("Not yet implemented")
    }
}