package dev.jsinco.lumabotutils

import net.dv8tion.jda.api.entities.Webhook
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

object WebhookUtil {


    init {

    }

    fun getWebhook(channel: TextChannel): Webhook {
        val webhooks = channel.retrieveWebhooks().complete().filter {
            it.owner == channel.jda.selfUser
        }

        if (webhooks.size > 1) {
            for (webhook in webhooks.drop(1)) {
                webhook.delete().queue()
            }
        }

        return if (webhooks.isEmpty()) {
            createWebhook(channel)
        } else {
            webhooks[0]
        }
    }

    fun createWebhook(channel: TextChannel): Webhook {
        return channel.createWebhook("LumaBotUtils " + channel.id).complete()
    }
}