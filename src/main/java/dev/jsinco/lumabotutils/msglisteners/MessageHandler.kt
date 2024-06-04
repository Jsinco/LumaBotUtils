package dev.jsinco.lumabotutils.msglisteners

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface MessageHandler {

    fun onMsg(event: MessageReceivedEvent)
}