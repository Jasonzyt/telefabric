package com.jasonzyt.telefabric.events;

import com.jasonzyt.telefabric.events.callbacks.PlayerDeathCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;

import static com.jasonzyt.telefabric.Telefabric.CONFIG;
import static com.jasonzyt.telefabric.Telefabric.BOT;

public class GameEventsHandler {

    public static final GameEventsHandler INSTANCE = new GameEventsHandler();

    public void register() {
        // Chat Forwarding
        if (CONFIG.features.chat_forwarding.enabled) {
            ServerMessageEvents.CHAT_MESSAGE.register((message, sender, typeKey) -> {
                String formattedMessage = CONFIG.features.chat_forwarding.format
                        .replace("%player%", sender.getName().getString())
                        .replace("%message%", message.signedContent());
                broadcastMessage(formattedMessage);
            });
        }

        // Join/Leave Notifications
        if (CONFIG.features.join_leave_notifications.enabled) {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                String formattedMessage = CONFIG.features.join_leave_notifications.join_format
                        .replace("%player%", handler.getPlayer().getName().getString());
                broadcastMessage(formattedMessage);
            });

            ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
                String formattedMessage = CONFIG.features.join_leave_notifications.leave_format
                        .replace("%player%", handler.getPlayer().getName().getString());
                broadcastMessage(formattedMessage);
            });
        }

        // Death Notifications
        if (CONFIG.features.death_notifications.enabled) {
            PlayerDeathCallback.EVENT.register((player, damageSource) -> {
                Component deathMessage = damageSource.getLocalizedDeathMessage(player);
                String formattedMessage = CONFIG.features.death_notifications.format
                        .replace("%death_message%", deathMessage.getString());
                broadcastMessage(formattedMessage);
            });
        }
    }

    private void broadcastMessage(String text) {
        for (long chatId : CONFIG.chats) {
            BOT.sendMessage(chatId, text);
        }
    }
}