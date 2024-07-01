package dev.jsinco.lumabotutils.commands

import dev.jsinco.lumabotutils.listeners.Listener
import dev.jsinco.lumabotutils.listeners.ListenerType
import dev.jsinco.lumabotutils.Main
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.TimerTask

class CommandManager : Listener, TimerTask() {

    companion object {
        val commands: MutableMap<String, Command> = mutableMapOf()

        @JvmStatic

        fun registerCommand(command: Command) {
            commands[command.getName()] = command
            upsertCommand(command)
        }

        @Suppress("duplicatedcode")
        private fun upsertCommand(command: Command) {
            val jda = Main.getJda()

            if (command.isGlobal()) {
                val commandCreateAction = jda.upsertCommand(command.getName(), command.getDescription())
                if (command.getOptions() != null) {
                    for (option in command.getOptions()!!) {
                        commandCreateAction.addOption(option.optionType, option.name, option.description, option.required)
                    }
                }
                commandCreateAction.queue()
            } else {
                for (guild in jda.guilds) {
                    val commandCreateAction = guild.upsertCommand(command.getName(), command.getDescription())
                    if (command.getOptions() != null) {
                        for (option in command.getOptions()!!) {
                            commandCreateAction.addOption(option.optionType, option.name, option.description, option.required)
                        }
                    }
                    commandCreateAction.queue()
                }
            }
        }
    }

    override fun onEvent(type: ListenerType, event: Any?) {
        if (type != ListenerType.SLASH_COMMAND) return // Should never happen anyway
        event as SlashCommandInteractionEvent

        val command = commands[event.name] ?: return
        val member = event.member ?: return
        if (!member.hasPermission(command.getPermission())) {
            event.reply("You do not have permission to use this command!").setEphemeral(true).queue()
            return
        }

        command.execute(event)
    }

    override fun registerFor(): List<ListenerType> {
        return listOf(ListenerType.SLASH_COMMAND)
    }

    override fun run() {
        // re-register commands in case they've been removed for some reason
        for (command in commands.values) {
            upsertCommand(command)
        }
    }

}
