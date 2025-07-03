package com.jasonzyt.telefabric.bot;

import com.jasonzyt.telefabric.Telefabric;
import com.jasonzyt.telefabric.bot.commands.ChatCommand;
import com.jasonzyt.telefabric.bot.commands.CmdCommand;
import com.jasonzyt.telefabric.bot.commands.GetIdCommand;
import com.jasonzyt.telefabric.config.Config;
import net.minecraft.server.MinecraftServer;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelefabricBot extends CommandLongPollingTelegramBot {
    private final Config config;
    private final MinecraftServer server;

    public TelefabricBot(Config config, MinecraftServer server) {
        super(new OkHttpTelegramClient(config.bot.token), true, () -> config.bot.username);
        this.config = config;
        this.server = server;
        if (config.features.bot_cmd_command.enabled) {
            register(new CmdCommand(config, server));
        }
        if (config.features.bot_get_id_command.enabled) {
            register(new GetIdCommand(config));
        }
        if  (config.features.bot_chat_command.enabled) {
            register(new ChatCommand(config, server));
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
