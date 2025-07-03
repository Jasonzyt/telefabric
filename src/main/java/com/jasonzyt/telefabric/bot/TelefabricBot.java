package com.jasonzyt.telefabric.bot;

import com.jasonzyt.telefabric.Telefabric;
import com.jasonzyt.telefabric.bot.commands.ChatCommand;
import com.jasonzyt.telefabric.bot.commands.CmdCommand;
import com.jasonzyt.telefabric.bot.commands.GetIdCommand;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.jasonzyt.telefabric.Telefabric.CONFIG;

public class TelefabricBot extends CommandLongPollingTelegramBot {

    public TelefabricBot() {
        super(new OkHttpTelegramClient(CONFIG.bot.token), true, () -> CONFIG.bot.username);
        if (CONFIG.features.bot_cmd_command.enabled) {
            register(new CmdCommand());
        }
        if (CONFIG.features.bot_get_id_command.enabled) {
            register(new GetIdCommand());
        }
        if  (CONFIG.features.bot_chat_command.enabled) {
            register(new ChatCommand());
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        // pass
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            Telefabric.LOGGER.error("Failed to send message", e);
        }
    }
}
