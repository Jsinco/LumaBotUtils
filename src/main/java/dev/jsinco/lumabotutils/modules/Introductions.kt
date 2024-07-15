package dev.jsinco.lumabotutils.modules

import club.minnced.discord.webhook.external.JDAWebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedField
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import dev.jsinco.lumabotutils.Main
import dev.jsinco.lumabotutils.WebhookUtil
import dev.jsinco.lumabotutils.commands.Command
import dev.jsinco.lumabotutils.commands.CommandOptions
import dev.jsinco.lumabotutils.listeners.Listener
import dev.jsinco.lumabotutils.listeners.ListenerType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal


class Introductions : Command, Listener {

    // Name/Nick(s)
    // Favorite food(s)
    // Interests/hobbies
    override fun execute(event: SlashCommandInteraction) {
        event.replyModal(getIntroModal()).queue()
    }

    override fun getOptions(): List<CommandOptions> {
        return emptyList()
    }

    override fun getName(): String {
        return "intro"
    }

    override fun getDescription(): String {
        return "Introduce yourself to the server!"
    }

    override fun getPermission(): Permission {
        return Permission.MESSAGE_SEND
    }

    override fun isGlobal(): Boolean {
        return false
    }



    override fun onEvent(type: ListenerType, event: Any?) {

        when (type) {
            ListenerType.MODAL_INTERACTION -> handleModalInteraction(event as ModalInteractionEvent)

            ListenerType.MESSAGE_RECEIVED -> {
                event as MessageReceivedEvent
                val str = event.message.contentRaw
                if (str == "!intro" || str == "/intro") {
                    event.message.reply("Click here to start an introduction.").addActionRow(
                        Button.primary("intro", "Start")
                    ).queue()
                }
            }

            ListenerType.BUTTON_INTERACTION -> {
                event as ButtonInteractionEvent
                if (event.componentId != "intro") return
                event.replyModal(getIntroModal()).queue()
            }

            else -> return
        }


        //val saves: JsonSavingSchema = Main.getSaves()
        //saves.set("introduction-messages.${event.user.id}", msgId)
        //saves.save()
    }

    override fun registerFor(): List<ListenerType> {
        return listOf(ListenerType.MODAL_INTERACTION, ListenerType.MESSAGE_RECEIVED, ListenerType.BUTTON_INTERACTION)
    }



    private fun handleModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != "introduction") return
        val channel: TextChannel? = (Main.getSettings().getString("introductions.channel")?.let {
            event.jda.getGuildChannelById(
                it
            )
        } as TextChannel?)

        if (channel == null) {
            event.reply("Not enabled.").queue()
            return
        }

        event.reply("Your introduction has been posted!").queue()

        val introData = IntroData(
            event.user,
            event.getValue("names")?.asString!!,
            event.getValue("fav-food")?.asString,
            event.getValue("hobbies")?.asString,
            event.getValue("about-me")?.asString!!,
            event.getValue("minecraft-username")?.asString,
        )

        val url = WebhookUtil.getWebhook(channel).url
        JDAWebhookClient.withUrl(url).use { client ->
            val message = WebhookMessageBuilder()
                .setUsername("Introductions ✨")
                .setAvatarUrl("https://iili.io/JpYWJBs.png")
                .addEmbeds(getIntroEmbed(introData).build())
                .build()
            client.send(message)
            client.close()
        }

    }

    private fun getIntroModal(): Modal {
        val names = TextInput.create("names", "What should we call you?", TextInputStyle.SHORT)
            .setPlaceholder("Usernames or nicknames")
            .setRequiredRange(1, 200)
            .build()
        val favFoods = TextInput.create("fav-food", "Your favorite foods?", TextInputStyle.SHORT)
            .setPlaceholder("Favorite foods, drinks, etc.")
            .setRequiredRange(1, 200)
            .setRequired(false)
            .build()
        val hobbies = TextInput.create("hobbies", "What do you like to do?", TextInputStyle.SHORT)
            .setPlaceholder("Interests, hobbies, etc.")
            .setRequiredRange(1, 200)
            .setRequired(false)
            .build()
        val aboutMe = TextInput.create("about-me", "About Me", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Now tell us about yourself!")
            .setRequiredRange(5, 4000)
            .build()
        val minecraftUsername = TextInput.create("minecraft-username", "Minecraft Username", TextInputStyle.SHORT)
            .setPlaceholder("Your Minecraft username?")
            .setRequired(false)
            .build()


        return Modal.create("introduction", "Let's fill this out...")
            .addComponents(ActionRow.of(names), ActionRow.of(favFoods), ActionRow.of(hobbies), ActionRow.of(aboutMe), ActionRow.of(minecraftUsername))
            .build()
    }

    private fun getIntroEmbed(intro: IntroData): WebhookEmbedBuilder {
        val embedBuilder = WebhookEmbedBuilder()

        embedBuilder.setTitle(WebhookEmbed.EmbedTitle("Say hello to ${intro.user.effectiveName}! :wave:", null))
        embedBuilder.setColor(16029942)
        embedBuilder.setThumbnailUrl(intro.user.avatarUrl)
        embedBuilder.setDescription("\n\n**About me**\n${intro.aboutMe}\n\n")
        embedBuilder.addField(EmbedField(true, "**You can call me...**", intro.names))
        if (intro.favFoods != null) embedBuilder.addField(EmbedField(true, "**I like to eat...**", intro.favFoods))
        if (intro.hobbies != null) embedBuilder.addField(EmbedField(true, "**My interests & hobbies...**", intro.hobbies))
        if (intro.minecraftUsername != null) {
            embedBuilder.setFooter(WebhookEmbed.EmbedFooter("My username is ${intro.minecraftUsername} in-game!", "https://minotar.net/helm/${intro.minecraftUsername}"))
        }

        return embedBuilder
    }
}

private data class IntroData(
    val user: User,
    val names: String,
    val favFoods: String?,
    val hobbies: String?,
    val aboutMe: String,
    val minecraftUsername: String?,
)