package dev.jsinco.lumabotutils.listeners

import dev.jsinco.lumabotutils.commands.Command
import dev.jsinco.lumabotutils.commands.CommandOptions
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import java.io.IOException
import java.net.URL
import java.util.Scanner
import java.util.function.Consumer

class WarnTBPAnnoyingMembers : Listener, Command {


    override fun onEvent(type: ListenerType, event: Any?) {
        when (type) {
            ListenerType.MESSAGE_RECEIVED -> {
                event as MessageReceivedEvent
                if (event.author.isBot) return
                warnForUsingOutdatedBreweryX(event)
            }

            else -> return
        }
    }

    override fun registerFor(): List<ListenerType> {
        return listOf(ListenerType.MESSAGE_RECEIVED)
    }

    fun warnForUsingOutdatedBreweryX(event: MessageReceivedEvent) {
        val raw = event.message.contentRaw
        if (!raw.contains("WARN") && !raw.contains("ERROR") && !raw.contains("INFO")) return

        val versionFromMessage = if (raw.contains("BreweryX v")) {
            raw.substringAfter("BreweryX v").substringBefore(" ")
        } else {
            return
        }

        if (breweryXVersion == null) {
            updateBreweryXVersion()
        }

        if (parseVersion(breweryXVersion ?: return) <= parseVersion(versionFromMessage)) return
        event.message.reply("Your BreweryX version is outdated! **Do not** report issues for outdated BreweryX versions. Always make reports using the latest version available. Your version: $versionFromMessage, latest version: $breweryXVersion").queue()
    }


    companion object {
        private const val RESOURCE_ID = 114777
        @Volatile
        var breweryXVersion:String? = null


        fun query(consumer: Consumer<String>) {
            Thread().run {
                try {
                    URL("https://api.spigotmc.org/legacy/update.php?resource=$RESOURCE_ID/~").openStream()
                        .use { stream ->
                            Scanner(stream).use { scann ->
                                if (scann.hasNext()) {
                                    consumer.accept(scann.next())
                                }
                            }
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun parseVersion(version: String): Int {
            val sb = StringBuilder()
            for (c in version.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c)
                }
            }
            return sb.toString().toIntOrNull() ?: 0
        }

        fun updateBreweryXVersion(callBack: Runnable? = null) {
            query { version: String ->
                if (breweryXVersion != null) {
                    if (parseVersion(breweryXVersion!!) < parseVersion(version)) {
                        breweryXVersion = version
                    }
                } else {
                    breweryXVersion = version
                }
                callBack?.run()
            }
        }
    }

    override fun execute(event: SlashCommandInteraction) {
        event.deferReply().queue()
        updateBreweryXVersion {
            event.hook.sendMessage("Got BreweryX's latest version. (v$breweryXVersion)").queue()
        }
    }

    override fun getOptions(): List<CommandOptions>? {
        return null
    }

    override fun getName(): String {
        return "querybreweryxlatest"
    }

    override fun getDescription(): String {
        return "Query the latest version of BreweryX"
    }

    override fun getPermission(): Permission {
        return Permission.ADMINISTRATOR
    }

    override fun isGlobal(): Boolean {
        return false
    }

}