package com.jasonzyt.telefabric;

import com.jasonzyt.telefabric.bot.TelefabricBot;
import com.jasonzyt.telefabric.config.Config;
import com.jasonzyt.telefabric.events.GameEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Telefabric implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Telefabric");
    private static MinecraftServer server;
    private TelefabricBot bot;
    private Config config;

    @Override
    public void onInitialize() {
        this.config = Config.load();

        if ("your_bot_token".equals(config.bot.token) || "your_bot_username".equals(config.bot.username)) {
            LOGGER.warn("Bot token or username is not configured in config/telefabric.yml");
            return;
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Telefabric.server = server;
            this.bot = new TelefabricBot(config, server);

            try(TelegramBotsLongPollingApplication botsApp = new TelegramBotsLongPollingApplication()) {
                botsApp.registerBot(config.bot.token, new TelefabricBot(config, server));
                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            registerCommands();
            registerEvents();
        });
    }

    private void registerCommands() {
        bot.registerCommand("hello", message -> {
            bot.sendMessage(message.getChatId(), "Hello, " + message.getFrom().getFirstName() + "!");
        });
    }

    private void registerEvents() {
        GameEventsHandler eventsHandler = new GameEventsHandler(config, bot);
        eventsHandler.register();
    }
}
