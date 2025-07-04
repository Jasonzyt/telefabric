package com.jasonzyt.telefabric.bot.commands;

import net.minecraft.network.chat.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.jasonzyt.telefabric.Telefabric.CONFIG;
import static com.jasonzyt.telefabric.Telefabric.SERVER;

public class ChatCommand extends BotCommand {

    public ChatCommand() {
        super("chat", "Send a message to the game");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] args) {
        if (!CONFIG.chats.contains(chat.getId())) {
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

        String formattedMessage = CONFIG.features.bot_chat_command.format.replace("%name%", senderName).replace("%message%", message);
        SERVER.getPlayerList().broadcastSystemMessage(Component.literal(formattedMessage), false);
        SERVER.sendSystemMessage(Component.literal(formattedMessage));
    }
}