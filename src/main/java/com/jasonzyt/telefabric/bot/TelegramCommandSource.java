package com.jasonzyt.telefabric.bot;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import java.util.UUID;

public class TelegramCommandSource implements CommandSource {

    private final StringBuilder feedback = new StringBuilder();
    private final TelefabricBot bot;
    private final long chatId;

    public TelegramCommandSource(TelefabricBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    @Override
    public void sendSystemMessage(Component message) {
        feedback.append(message.getString()).append("\n");
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
        if (!feedback.isEmpty()) {
            bot.sendMessage(chatId, feedback.toString());
            feedback.setLength(0);
        }
    }
}