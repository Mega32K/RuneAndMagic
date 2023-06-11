package rune.magic.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandUtil {

    public static LiteralArgumentBuilder<CommandSourceStack> add(LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, String l1, String l2, ArgumentType<?> argumentType, SuggestionProvider<CommandSourceStack> suggets, Command<CommandSourceStack> command) {
        return literalArgumentBuilder.then(Commands.literal(l1).then(Commands.argument(l2, argumentType).suggests(suggets).executes(command)));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> add(LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, String l1, String l2, ArgumentType<?> argumentType, Command<CommandSourceStack> command) {
        return literalArgumentBuilder.then(Commands.literal(l1).then(Commands.argument(l2, argumentType).executes(command)));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> add(LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, String l1, Command<CommandSourceStack> command) {
        return literalArgumentBuilder.then(Commands.literal(l1).executes(command));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(String name) {
        return Commands.literal(name);
    }
}
