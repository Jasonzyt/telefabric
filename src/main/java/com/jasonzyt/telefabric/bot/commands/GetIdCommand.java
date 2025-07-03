package com.jasonzyt.telefabric.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class GetIdCommand extends BotCommand {
    public GetIdCommand() {
        super("getid", "Get the chat id and user id");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] strings) {
        try {
            SendMessage message = SendMessage.builder().chatId(chat.getId()).text("Chat Id: " + chat.getId() + '\n' + "User Id: " + user
                    .getId()).build();
            telegramClient.execute(message);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
