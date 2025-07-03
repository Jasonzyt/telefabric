package com.jasonzyt.telefabric.command;

import com.jasonzyt.telefabric.Telefabric;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

public class TelefabricCommand {

    public static final TelefabricCommand INSTANCE = new TelefabricCommand();

    public int executeReloadCommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack src = context.getSource();
        boolean result = Telefabric.reloadConfig();
        if (!result) {
            src.sendFailure(Component.literal("Failed to reload config"));
            return 0;
        }
        src.sendSuccess(() -> Component.literal("Reloaded config").withStyle(ChatFormatting.GREEN), false);
        return Command.SINGLE_SUCCESS;
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(literal("telefabric")
                .then(literal("reload")
                        .requires(src -> src.hasPermission(4))
                        .executes(this::executeReloadCommand)
                )
        );
    }
}
