package io.github.theriverelder

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.action.MemberNudge
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.ExternalResource
import kotlin.coroutines.CoroutineContext

class MemberTestImpl(
    override val group: Group,
    override val id: Long,
    override val nick: String,
) : Member {
    override val bot: Bot
        get() = TODO("Not yet implemented")
    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
    override val nameCard: String
        get() = TODO("Not yet implemented")
    override val permission: MemberPermission
        get() = TODO("Not yet implemented")
    override val remark: String
        get() = TODO("Not yet implemented")
    override val specialTitle: String
        get() = TODO("Not yet implemented")

    override suspend fun mute(durationSeconds: Int) {
        TODO("Not yet implemented")
    }

    override fun nudge(): MemberNudge {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: String): MessageReceipt<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message): MessageReceipt<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(resource: ExternalResource): Image {
        TODO("Not yet implemented")
    }
}