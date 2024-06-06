package dev.jsinco.lumabotutils.commands

import dev.jsinco.lumabotutils.listeners.Listener
import dev.jsinco.lumabotutils.listeners.ListenerType
import dev.jsinco.lumabotutils.Main
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CommandManager : Listener {

    companion object {
        val commands: MutableMap<String, Command> = mutableMapOf()

        @JvmStatic
        @Suppress("duplicatedcode")
        fun registerCommand(command: Command) {
            val jda = Main.getJda()
            commands[command.getName()] = command

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

    override fun onEvent(type: ListenerType, event: Any) {
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

}
