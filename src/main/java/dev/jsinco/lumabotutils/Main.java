package dev.jsinco.lumabotutils;

import dev.jsinco.abstractjavafilelib.schemas.SnakeYamlConfig;
import dev.jsinco.lumabotutils.commands.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private static JDA jda;
    public static SnakeYamlConfig settings;

    public static void main(String[] args) {
        settings = new SnakeYamlConfig("config.yml");
        final String token = settings.getString("bot-token");

        if (token == null || token.isBlank()) {
            System.err.println("You must set bot token in settings.yml!");
            System.exit(1);
        }

        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setAutoReconnect(true).build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jda.addEventListener(new CommandManager());
    }

    public static JDA getJda() {
        return jda;
    }

    public static SnakeYamlConfig getSettings() {
        return settings;
    }
}