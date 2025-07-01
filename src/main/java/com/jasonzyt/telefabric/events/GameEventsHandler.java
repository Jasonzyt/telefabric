package com.jasonzyt.telefabric.events;

import com.jasonzyt.telefabric.bot.TelefabricBot;
import com.jasonzyt.telefabric.bot.TelefabricBot;
import com.jasonzyt.telefabric.config.Config;
import com.jasonzyt.telefabric.events.callbacks.PlayerDeathCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;

public class GameEventsHandler {

    private final Config config;
    private final TelefabricBot bot;

    public GameEventsHandler(Config config, TelefabricBot bot) {
        this.config = config;
        this.bot = bot;
    }

    public void register() {
        // Chat Forwarding
        if (config.features.chat_forwarding.enabled) {
            ServerMessageEvents.CHAT_MESSAGE.register((message, sender, typeKey) -> {
                String formattedMessage = config.features.chat_forwarding.format
                        .replace("%player%", sender.getName().getString())
                        .replace("%message%", message.signedContent());
                broadcastMessage(formattedMessage);
            });
        }

        // Join/Leave Notifications
        if (config.features.join_leave_notifications.enabled) {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                String formattedMessage = config.features.join_leave_notifications.join_format
                        .replace("%player%", handler.getPlayer().getName().getString());
                broadcastMessage(formattedMessage);
            });

            ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
                String formattedMessage = config.features.join_leave_notifications.leave_format
                        .replace("%player%", handler.getPlayer().getName().getString());
                broadcastMessage(formattedMessage);
            });
        }

        // Death Notifications
        if (config.features.death_notifications.enabled) {
            PlayerDeathCallback.EVENT.register((player, damageSource) -> {
                Component deathMessage = damageSource.getLocalizedDeathMessage(player);
                String formattedMessage = config.features.death_notifications.format
                        .replace("%death_message%", deathMessage.getString());
                broadcastMessage(formattedMessage);
            });
        }
    }

    private void broadcastMessage(String text) {
        for (long chatId : config.chats) {
            bot.sendMessage(chatId, text);
        }
    }
}