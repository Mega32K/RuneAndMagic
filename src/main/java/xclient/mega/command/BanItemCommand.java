package xclient.mega.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.utils.CommandUtil;
import xclient.mega.Config;
import xclient.mega.MegaUtil;

@Mod.EventBusSubscriber
public class BanItemCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        final LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = CommandUtil.create("itemBanning");
        CommandUtil.add(literalargumentbuilder, "ban", "name", ItemArgument.item(), (so) -> banItemName(so, ItemArgument.getItem(so, "name")));
        CommandUtil.add(literalargumentbuilder, "unban", "name", ItemArgument.item(), (so) -> unbanItemName(so, ItemArgument.getItem(so, "name")));
        CommandUtil.add(literalargumentbuilder, "banClass", "name", ItemArgument.item(), (so) -> banItemClass(so, ItemArgument.getItem(so, "name")));
        CommandUtil.add(literalargumentbuilder, "unbanClass", "name", ItemArgument.item(), (so) -> unbanItemClass(so, ItemArgument.getItem(so, "name")));
        CommandUtil.add(literalargumentbuilder, "unbanAll", BanItemCommand::unbanAll);
        CommandUtil.add(literalargumentbuilder, "list", BanItemCommand::printList);
        CommandUtil.add(literalargumentbuilder, "empty", "logic", BoolArgumentType.bool(), (so) -> empty(so, BoolArgumentType.getBool(so, "logic")));
        event.getDispatcher().register(literalargumentbuilder);
    }

    private static int empty(CommandContext<CommandSourceStack> source, boolean logic) {
        source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.empty").append(String.valueOf(logic)).withStyle(ChatFormatting.RED), false);
        Config.ITEM_BANNED_EMPTY.set(logic);
        Config.ITEM_BANNED_EMPTY.save();
        return 0;
    }

    private static int banItemName(CommandContext<CommandSourceStack> source, ItemInput input) {
        String s = input.getItem().getName(new ItemStack(input.getItem())).getString();
        if (!Config.ITEM_BANNED_LIST.get().contains(s)) {
            Config.ITEM_BANNED_LIST.get().add(s);
            MegaUtil.addI(input.getItem());
            source.getSource().sendSuccess(new TextComponent(I18n.get("item_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.ITEM_BANNED_LIST.save();
        return 0;
    }

    private static int unbanItemName(CommandContext<CommandSourceStack> source, ItemInput input) {
        String s = input.getItem().getName(new ItemStack(input.getItem())).getString();
        if (Config.ITEM_BANNED_LIST.get().contains(s)) {
            Config.ITEM_BANNED_LIST.get().remove(s);
            Config.ITEM_BANNED_LIST.save();
            source.getSource().sendSuccess(new TextComponent(I18n.get("item_banning.command.unban")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        return 0;
    }

    private static int banItemClass(CommandContext<CommandSourceStack> source, ItemInput input) {
        String s = MegaUtil.getItemSimpleNameWithRegistryPath(input.getItem());
        if (!Config.ITEM_CLASS_BANNED_LIST.get().contains(s)) {
            Config.ITEM_CLASS_BANNED_LIST.get().add(s);

            source.getSource().sendSuccess(new TextComponent(I18n.get("item_banning.command.Cbanned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.ITEM_CLASS_BANNED_LIST.save();
        return 0;
    }

    private static int unbanItemClass(CommandContext<CommandSourceStack> source, ItemInput input) {
        String s = MegaUtil.getItemSimpleNameWithRegistryPath(input.getItem());
        if (Config.ITEM_CLASS_BANNED_LIST.get().contains(s)) {
            Config.ITEM_CLASS_BANNED_LIST.get().remove(s);
            Config.ITEM_CLASS_BANNED_LIST.save();
            source.getSource().sendSuccess(new TextComponent(I18n.get("item_banning.command.Cunban")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        return 0;
    }

    private static int printList(CommandContext<CommandSourceStack> source) {
        source.getSource().sendSuccess(new TextComponent(MegaUtil.item_name_banned.toString()), false);
        source.getSource().sendSuccess(new TextComponent(MegaUtil.item_keyname_banned.toString()), false);
        return 0;
    }

    private static int unbanAll(CommandContext<CommandSourceStack> source) {
        Config.ITEM_BANNED_LIST.get().clear();
        Config.ITEM_CLASS_BANNED_LIST.get().clear();
        Config.ITEM_BANNED_LIST.save();
        Config.ITEM_CLASS_BANNED_LIST.save();
        source.getSource().sendSuccess(new TranslatableComponent("item_banning.command.unbanALl").withStyle(ChatFormatting.YELLOW), false);
        MegaUtil.item_name_banned.clear();
        MegaUtil.item_keyname_banned.clear();
        return 0;
    }
}
