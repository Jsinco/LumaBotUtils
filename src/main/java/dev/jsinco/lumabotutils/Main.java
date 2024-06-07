package dev.jsinco.lumabotutils;

import dev.jsinco.abstractjavafilelib.schemas.JsonSavingSchema;
import dev.jsinco.abstractjavafilelib.schemas.SnakeYamlConfig;
import dev.jsinco.lumabotutils.commands.CommandManager;
import dev.jsinco.lumabotutils.listeners.EventManager;
import dev.jsinco.lumabotutils.modules.Introductions;
import dev.jsinco.lumabotutils.modules.Suggestions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Timer;

public class Main {

    private static JDA jda;
    private static SnakeYamlConfig settings;
    private static JsonSavingSchema saves;

    public static JDA getJda() { return jda; }
    public static SnakeYamlConfig getSettings() { return settings; }
    public static JsonSavingSchema getSaves() { return saves; }

    public static void main(String[] args) {
        //FileLibSettings.set(new File("C:\\Users\\jonah\\Downloads\\temp"));

        settings = new SnakeYamlConfig("settings.yml");
        saves = new JsonSavingSchema("saves.json");
        final String token = settings.getString("bot-token");

        if (token == null || token.isBlank()) {
            System.err.println("You must set bot token in settings.yml!");
            System.exit(1);
        }

        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setAutoReconnect(true).build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jda.addEventListener(new EventManager());

        final CommandManager cmdManager = new CommandManager();
        EventManager.registerListener(cmdManager);


        // Start timer runnable
        final Timer timer = new Timer();
        timer.schedule(cmdManager, 0L, 300000L);


        Util.registerCommandAndListener(new Suggestions());
        Util.registerCommandAndListener(new Introductions());
    }
}