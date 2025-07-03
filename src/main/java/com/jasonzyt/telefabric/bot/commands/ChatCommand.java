package com.jasonzyt.telefabric.bot.commands;

import com.jasonzyt.telefabric.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class ChatCommand extends BotCommand {

    private final Config config;
    private final MinecraftServer server;

    public ChatCommand(Config config, MinecraftServer server) {
        super("chat", "Send a message to the game");
        this.config = config;
        this.server = server;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] args) {
        if (!config.chats.contains(chat.getId())) {
            return;
        }

        if (args.length == 0) {
            return;
        }

        String message = String.join(" ", args);
        String senderName = user.getFirstName();
        if (user.getLastName() != null) {
            senderName += " " + user.getLastName();
        }

        String formattedMessage = config.features.bot_chat_command.format.replace("%name%", senderName).replace("%message%", message);
        server.sendSystemMessage(Component.literal(formattedMessage));
    }
}