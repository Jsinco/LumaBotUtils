package dev.jsinco.lumabotutils.modules

import dev.jsinco.lumabotutils.Main
import dev.jsinco.lumabotutils.commands.Command
import dev.jsinco.lumabotutils.commands.CommandOptions
import dev.jsinco.lumabotutils.listeners.Listener
import dev.jsinco.lumabotutils.listeners.ListenerType
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction

class Suggestions : Command, Listener {

    private val suggestionsChannelId = Main.getSettings().getString("suggestions.channel") ?: ""
    override fun execute(event: SlashCommandInteraction) {
        val suggestion = event.getOption("suggestion")?.asString ?: return
        sendSuggestionEmbed(event.user, suggestion)
        event.reply("Your suggestion has been posted").setEphemeral(true).queue()
    }

    override fun getOptions(): List<CommandOptions>? {
        return listOf(CommandOptions(OptionType.STRING,
            "suggestion",
            "Enter suggestion",
            true))
    }

    override fun getName(): String {
        return "suggest"
    }

    override fun getDescription(): String {
        return "Make a new suggestion"
    }

    override fun getPermission(): Permission {
        return Permission.MESSAGE_SEND
    }

    override fun isGlobal(): Boolean {
        return false
    }

    override fun onEvent(type: ListenerType, event: Any?) {
        event as MessageReceivedEvent
        if (!event.message.contentRaw.startsWith("!suggest ")) return

        sendSuggestionEmbed(event.author, event.message.contentRaw.removePrefix("!suggest "))
    }

    override fun registerFor(): List<ListenerType> {
        return listOf(ListenerType.MESSAGE_RECEIVED)
    }

    private fun sendSuggestionEmbed(user: User, txt: String) {
        val channel = Main.getJda().getTextChannelById(suggestionsChannelId) ?: return

        val embedBuilder = EmbedBuilder()
        embedBuilder.setTitle("**Suggestion from ${user.effectiveName}**")
        embedBuilder.setDescription(txt)
        embedBuilder.setColor(16029942)
        embedBuilder.setThumbnail(user.effectiveAvatarUrl)
        val message = channel.sendMessageEmbeds(embedBuilder.build()).complete()
        message.addReaction(Emoji.fromUnicode("U+2705")).queue()
        message.addReaction(Emoji.fromUnicode("U+274C")).queue()
    }
}