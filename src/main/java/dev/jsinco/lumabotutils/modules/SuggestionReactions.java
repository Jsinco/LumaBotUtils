package dev.jsinco.lumabotutils.modules;

import dev.jsinco.lumabotutils.listeners.Listener;
import dev.jsinco.lumabotutils.listeners.ListenerType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuggestionReactions implements Listener {
    @Override
    public void onEvent(@NotNull ListenerType type, @NotNull Object event1) {
        if (!(event1 instanceof MessageReceivedEvent event)) return;
        if (event.getChannel() instanceof PrivateChannel || event.getChannel().getIdLong() != 1188329205973401621L || event.getAuthor() != event.getJDA().getSelfUser()) return;
        final Message message = event.getMessage();
        message.addReaction(Emoji.fromUnicode("U+2705")).queue();
        message.addReaction(Emoji.fromUnicode("U+274C")).queue();
    }


    @NotNull
    @Override
    public List<ListenerType> registerFor() {
        return List.of(ListenerType.MESSAGE_RECEIVED);
    }
}
