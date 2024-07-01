package dev.jsinco.lumabotutils.modules

import dev.jsinco.lumabotutils.Main
import dev.jsinco.lumabotutils.listeners.Listener
import dev.jsinco.lumabotutils.listeners.ListenerType
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent

class AutoRole : Listener {



    override fun onEvent(type: ListenerType, event: Any?) {
        if (!Main.getSettings().getBoolean("autorole.enabled")) {
            return
        }

        val roleId = Main.getSettings().getString("autorole.role") ?: return

        when (type) {
            ListenerType.GUILD_MEMBER_JOIN -> {
                event as GuildMemberJoinEvent
                event.guild.addRoleToMember(event.member, event.guild.getRoleById(roleId) ?: return).queue()
            }

            ListenerType.JDA_READY -> {
                val guild = Main.getJda().getGuildById(Main.getSettings().getString("autorole.guild") ?: return) ?: return
                val role = guild.getRoleById(roleId) ?: return

                for (member in guild.members) {
                    if (member.user.isBot) continue
                    if (member.roles.contains(role)) continue
                    guild.addRoleToMember(member,  role).queue()
                }
            }

            else -> return
        }
    }

    override fun registerFor(): List<ListenerType> {
        return listOf(ListenerType.GUILD_MEMBER_JOIN, ListenerType.JDA_READY)
    }
}