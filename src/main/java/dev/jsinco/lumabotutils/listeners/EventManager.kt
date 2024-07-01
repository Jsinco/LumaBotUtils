package dev.jsinco.lumabotutils.listeners

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventManager : ListenerAdapter() {

    companion object {
        private val listeners: MutableMap<ListenerType, MutableList<Listener>> = mutableMapOf()

        fun fire(type: ListenerType, event: Any?) {
            for (listener in listeners[type] ?: return) {
                listener.onEvent(type, event)
            }
        }

        @JvmStatic
        fun registerListener(listener: Listener) {
            for (type in listener.registerFor()) {
                if (listeners[type] == null) {
                    listeners[type] = mutableListOf()
                }
                listeners[type]?.add(listener)
            }
        }
    }


    fun onJDAReady() { // Custom event
        fire(ListenerType.JDA_READY, null)
    }

    override fun onReady(event: ReadyEvent) {
        println("test")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        fire(ListenerType.SLASH_COMMAND, event)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        fire(ListenerType.MESSAGE_RECEIVED, event)
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        fire(ListenerType.MODAL_INTERACTION, event)
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        fire(ListenerType.BUTTON_INTERACTION, event)
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        fire(ListenerType.GUILD_MEMBER_JOIN, event)
    }

}