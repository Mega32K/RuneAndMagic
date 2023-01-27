package xclient.mega.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.utils.CommandUtil;
import xclient.mega.Config;
import xclient.mega.MegaUtil;

@Mod.EventBusSubscriber
public class BanMobEffectCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        final LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = CommandUtil.create("effectBanning");
        CommandUtil.add(literalargumentbuilder, "ban", "type", MobEffectArgument.effect(), (so) -> banParticle(so, MobEffectArgument.getEffect(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unban", "type", MobEffectArgument.effect(), (so) -> unbanParticle(so, MobEffectArgument.getEffect(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unbanAll", BanMobEffectCommand::unbanAll);
        CommandUtil.add(literalargumentbuilder, "list", BanMobEffectCommand::printList);
        event.getDispatcher().register(literalargumentbuilder);
    }

    private static int banParticle(CommandContext<CommandSourceStack> source, MobEffect effect) {
        String s = MegaUtil.getMobEffectSimpleNameWithRegistryPath(effect);
        if (!MegaUtil.getE(effect)) {
            MegaUtil.addE(effect);
            source.getSource().sendSuccess(new TextComponent(I18n.get("effect_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("effect_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.EFFECT_BANNED_LIST.save();
        return 0;
    }

    private static int unbanParticle(CommandContext<CommandSourceStack> source, MobEffect effect) {
        String s = MegaUtil.getMobEffectSimpleNameWithRegistryPath(effect);
        if (MegaUtil.getE(effect)) {
            MegaUtil.removeE(effect);
            source.getSource().sendSuccess(new TextComponent(I18n.get("effect_banning.command.unban") + s).withStyle(ChatFormatting.YELLOW), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("effect_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.EFFECT_BANNED_LIST.save();
        return 0;
    }

    private static int printList(CommandContext<CommandSourceStack> source) {
        source.getSource().sendSuccess(new TextComponent(MegaUtil.effect_banned_.toString()), false);
        return 0;
    }

    private static int unbanAll(CommandContext<CommandSourceStack> source) {
        Config.EFFECT_BANNED_LIST.get().clear();
        Config.EFFECT_BANNED_LIST.save();
        source.getSource().sendSuccess(new TranslatableComponent("effect_banning.command.unbanALl").withStyle(ChatFormatting.YELLOW), false);
        MegaUtil.effect_banned_.clear();
        return 0;
    }
}
