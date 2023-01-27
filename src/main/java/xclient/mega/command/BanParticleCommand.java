package xclient.mega.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.utils.CommandUtil;
import xclient.mega.Config;
import xclient.mega.MegaUtil;

@Mod.EventBusSubscriber
public class BanParticleCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        final LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = CommandUtil.create("particleBanning");
        CommandUtil.add(literalargumentbuilder, "ban", "type", ParticleArgument.particle(), (so) -> banParticle(so, ParticleArgument.getParticle(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unban", "type", ParticleArgument.particle(), (so) -> unbanParticle(so, ParticleArgument.getParticle(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unbanAll", BanParticleCommand::unbanAll);
        CommandUtil.add(literalargumentbuilder, "list", BanParticleCommand::printList);
        event.getDispatcher().register(literalargumentbuilder);
    }

    private static int banParticle(CommandContext<CommandSourceStack> source, ParticleOptions options) {
        ParticleType<?> type = options.getType();
        String s = MegaUtil.getParticleSimpleNameWithRegistryPath(type);
        if (!MegaUtil.get(type)) {
            MegaUtil.add(type);
            source.getSource().sendSuccess(new TextComponent(I18n.get("par_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(s)).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("par_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.PARTICLE_BANNED_LIST.save();
        return 0;
    }

    private static int unbanParticle(CommandContext<CommandSourceStack> source, ParticleOptions options) {
        ParticleType<?> type = options.getType();
        String s = MegaUtil.getParticleSimpleNameWithRegistryPath(type);
        if (MegaUtil.get(type)) {
            MegaUtil.remove(type);
            source.getSource().sendSuccess(new TextComponent(I18n.get("par_banning.command.unban") + s).withStyle(ChatFormatting.YELLOW), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("par_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(s).withStyle(ChatFormatting.RED), false);
        }
        Config.PARTICLE_BANNED_LIST.save();
        return 0;
    }

    private static int printList(CommandContext<CommandSourceStack> source) {
        source.getSource().sendSuccess(new TextComponent(MegaUtil.particle_banned.toString()), false);
        return 0;
    }

    private static int unbanAll(CommandContext<CommandSourceStack> source) {
        Config.PARTICLE_BANNED_LIST.get().clear();
        Config.PARTICLE_BANNED_LIST.save();
        source.getSource().sendSuccess(new TranslatableComponent("par_banning.command.unbanALl").withStyle(ChatFormatting.YELLOW), false);
        MegaUtil.particle_banned.clear();
        return 0;
    }
}
