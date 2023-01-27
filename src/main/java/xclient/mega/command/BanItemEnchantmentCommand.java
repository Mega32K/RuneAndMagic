package xclient.mega.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.utils.CommandUtil;
import xclient.mega.Config;
import xclient.mega.MegaUtil;

@Mod.EventBusSubscriber
public class BanItemEnchantmentCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        final LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = CommandUtil.create("enchantmentBanning");
        CommandUtil.add(literalargumentbuilder, "ban", "type", ItemEnchantmentArgument.enchantment(), (so) -> banEnchantment(so, ItemEnchantmentArgument.getEnchantment(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unban", "type", ItemEnchantmentArgument.enchantment(), (so) -> unbanEnchantment(so, ItemEnchantmentArgument.getEnchantment(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unbanAll", BanItemEnchantmentCommand::unbanAll);
        CommandUtil.add(literalargumentbuilder, "list", BanItemEnchantmentCommand::printList);
        event.getDispatcher().register(literalargumentbuilder);
    }

    private static int banEnchantment(CommandContext<CommandSourceStack> source, Enchantment enchantment) {
        String s = MegaUtil.getEnchantmentSimpleNameWithRegistryPath(enchantment);
        if (!MegaUtil.get(enchantment)) {
            MegaUtil.add(enchantment);
            source.getSource().sendSuccess(new TextComponent(I18n.get("enc_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("enc_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
        return 0;
    }

    private static int unbanEnchantment(CommandContext<CommandSourceStack> source, Enchantment enchantment) {
        String s = MegaUtil.getEnchantmentSimpleNameWithRegistryPath(enchantment);
        if (MegaUtil.get(enchantment)) {
            MegaUtil.remove(enchantment);
            source.getSource().sendSuccess(new TextComponent(I18n.get("enc_banning.command.unban") + s).withStyle(ChatFormatting.YELLOW), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("enc_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
        return 0;
    }

    private static int printList(CommandContext<CommandSourceStack> source) {
        source.getSource().sendSuccess(new TextComponent(MegaUtil.effect_banned_.toString()), false);
        return 0;
    }

    private static int unbanAll(CommandContext<CommandSourceStack> source) {
        Config.ITEM_ENCHANTMENT_BANNED_LIST.get().clear();
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
        source.getSource().sendSuccess(new TranslatableComponent("enc_banning.command.unbanALl").withStyle(ChatFormatting.YELLOW), false);
        MegaUtil.enchantment_banned.clear();
        return 0;
    }
}
