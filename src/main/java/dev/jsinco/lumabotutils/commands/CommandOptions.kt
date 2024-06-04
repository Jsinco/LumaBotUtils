package dev.jsinco.lumabotutils.commands

import net.dv8tion.jda.api.interactions.commands.OptionType

data class CommandOptions(
    val optionType: OptionType,
    val name: String,
    val description: String,
    @get:JvmName("isRequired") val required: Boolean
)