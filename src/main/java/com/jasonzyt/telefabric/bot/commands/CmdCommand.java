package com.jasonzyt.telefabric.bot.commands;

import com.jasonzyt.telefabric.bot.TelegramCommandSource;
import com.jasonzyt.telefabric.config.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;

import static com.jasonzyt.telefabric.Telefabric.CONFIG;
import static com.jasonzyt.telefabric.Telefabric.SERVER;

public class CmdCommand extends BotCommand {

    public CmdCommand() {
        super("cmd", "Execute Minecraft commands");
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] args) {
        if (!CONFIG.chats.contains(chat.getId()) || args.length == 0) {
            return;
        }

        String mcCommand = args[0];
        Config.Command commandConfig = CONFIG.features.bot_cmd_command.commands.get(mcCommand);
        var builder = SendMessage.builder().chatId(chat.getId());

        if (commandConfig == null) {
            builder.text("The command '" + mcCommand + "' is not configured or not allowed.");
        } else {
            long userId = user.getId();
            if (commandConfig.permission.equalsIgnoreCase("users") && !commandConfig.allowed_users.contains(userId)) {
                builder.text("You do not have permission to use the '" + mcCommand + "' command.");
            } else {
                String fullMcCommand = String.join(" ", args);
                executeMinecraftCommand(fullMcCommand, telegramClient, chat.getId());
            }
        }

        try {
            telegramClient.execute(builder.build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeMinecraftCommand(String command, TelegramClient client, long chatId) {
        TelegramCommandSource telegramSource = new TelegramCommandSource(client, chatId);
        CommandSourceStack commandSourceStack = new CommandSourceStack(
                telegramSource,
                Vec3.atCenterOf(SERVER.overworld().getSharedSpawnPos()), // Position
                Vec2.ZERO, // Rotation
                SERVER.overworld(), // World
                4, // Permission level
                "TelefabricBot", // Name
                Component.literal("TelefabricBot"), // Display name
                SERVER,
                null // Entity
        );

        var parseResults = SERVER.getCommands().getDispatcher().parse(command, commandSourceStack);
        SERVER.getCommands().performCommand(parseResults, command);
        telegramSource.flush();
    }
}
