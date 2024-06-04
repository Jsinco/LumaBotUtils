package dev.jsinco.lumabotutils

import dev.jsinco.lumabotutils.commands.CommandManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Events : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = CommandManager.commands[event.name] ?: return
        val member = event.member ?: return
        if (!member.hasPermission(command.getPermission())) {
            event.reply("You do not have permission to use this command!").setEphemeral(true).queue()
            return
        }

        command.execute(event)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {

    }
}