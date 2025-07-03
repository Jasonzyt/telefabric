package com.jasonzyt.telefabric.bot;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramCommandSource implements CommandSource {

    private final SendMessage.SendMessageBuilder<?, ?> feedback = SendMessage.builder();
    private final TelegramClient client;

    public TelegramCommandSource(TelegramClient client, long chatId) {
        this.client = client;
        feedback.chatId(chatId);
    }

    @Override
    public void sendSystemMessage(Component message) {
        feedback.text(message.getString() + '\n');
    }

    @Override
    public boolean acceptsSuccess() {
        return true;
    }

    @Override
    public boolean acceptsFailure() {
        return true;
    }

    @Override
    public boolean shouldInformAdmins() {
        return true;
    }

    public void flush() {
        try {
            client.execute(feedback.build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}