package dev.jsinco.lumabotutils.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction

interface Command {
    fun execute(event: SlashCommandInteraction)

    fun getOptions(): List<CommandOptions>?

    fun getName(): String

    fun getDescription(): String

    fun getPermission(): Permission

    fun isGlobal(): Boolean
}