package dev.jsinco.lumabotutils

import dev.jsinco.lumabotutils.commands.Command
import dev.jsinco.lumabotutils.commands.CommandManager
import dev.jsinco.lumabotutils.listeners.EventManager
import dev.jsinco.lumabotutils.listeners.Listener
import net.dv8tion.jda.api.entities.Webhook
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

object Util {
    @JvmStatic
    fun registerCommandAndListener(instance: Any) {
        if (instance is Command) {
            CommandManager.registerCommand(instance)
        }
        if (instance is Listener) {
            EventManager.registerListener(instance)
        }
    }


}