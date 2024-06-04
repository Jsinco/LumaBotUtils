package dev.jsinco.lumabotutils.commands

import dev.jsinco.lumabotutils.Main

object CommandManager{

    val commands: MutableMap<String, Command> = mutableMapOf()

    @JvmStatic
    fun registerCommand(command: Command) {
        val jda = Main.getJda()
        commands[command.getName()] = command

        if (command.isGlobal()) {
            val commandCreateAction = jda.upsertCommand(command.getName(), command.getDescription())
            for ((optionType, name, description, required) in command.getOptions()) {
                commandCreateAction.addOption(optionType, name, description, required)
            }
            commandCreateAction.queue()
        } else {
            for (guild in jda.guilds) {
                val commandCreateAction = guild.upsertCommand(command.getName(), command.getDescription())
                for ((optionType, name, description, required) in command.getOptions()) {
                    commandCreateAction.addOption(optionType, name, description, required)
                }
                commandCreateAction.queue()
            }
        }

    }

}
