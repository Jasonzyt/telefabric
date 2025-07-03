package com.jasonzyt.telefabric;

import com.jasonzyt.telefabric.bot.TelefabricBot;
import com.jasonzyt.telefabric.command.TelefabricCommand;
import com.jasonzyt.telefabric.config.Config;
import com.jasonzyt.telefabric.events.GameEventsHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Telefabric implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Telefabric");
    public static MinecraftServer SERVER;
    public static Config CONFIG;
    public static TelefabricBot BOT;

    @Override
    public void onInitialize() {
        CONFIG = Config.load();


        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Telefabric.SERVER = server;
            if ("your_bot_token".equals(CONFIG.bot.token) || "your_bot_username".equals(CONFIG.bot.username)) {
                LOGGER.warn("Bot token or username is not configured in config/telefabric.yml");
                LOGGER.warn("Bot won't be started");
            } else {
                startBot();
            }
            registerEvents();
        });
        CommandRegistrationCallback.EVENT.register(TelefabricCommand.INSTANCE::register);
    }

    public static void startBot() {
        BOT = new TelefabricBot();
        Thread botThread = new Thread(() -> {
            try (TelegramBotsLongPollingApplication botsApp = new TelegramBotsLongPollingApplication()) {
                botsApp.registerBot(CONFIG.bot.token, BOT);
                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        botThread.start();
    }

    public static boolean reloadConfig() {
        try {
            CONFIG = Config.load();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void registerEvents() {
        GameEventsHandler eventsHandler = new GameEventsHandler();
        eventsHandler.register();
    }
}
