package xclient.mega.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.utils.CommandUtil;
import xclient.mega.Config;
import xclient.mega.MegaUtil;

@Mod.EventBusSubscriber
public class BanEntityCommand {
    public static final SuggestionProvider<CommandSourceStack> ALL = SuggestionProviders.register(new ResourceLocation("all"), (p_212438_, p_212439_) -> {
        return SharedSuggestionProvider.suggestResource(Registry.ENTITY_TYPE.stream(), p_212439_, EntityType::getKey, (p_212436_) -> {
            return new TranslatableComponent(Util.makeDescriptionId("entity", EntityType.getKey(p_212436_)));
        });
    });

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        final LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = CommandUtil.create("entityBanning");
        CommandUtil.add(literalargumentbuilder, "ban", "type", EntitySummonArgument.id(), ALL, (so) -> banEntityType(so, EntitySummonArgument.getSummonableEntity(so, "type")));
        CommandUtil.add(literalargumentbuilder, "unban", "type", EntitySummonArgument.id(), ALL, (so) -> unbanEntityType(so, EntitySummonArgument.getSummonableEntity(so, "type")));
        CommandUtil.add(literalargumentbuilder, "banResourceLocation", "resource_location", ResourceLocationArgument.id(), ALL, (so) -> banEntityType(so, ResourceLocationArgument.getId(so, "resource_location")));
        CommandUtil.add(literalargumentbuilder, "unbanResourceLocation", "resource_location", ResourceLocationArgument.id(), ALL, (so) -> unbanEntityType(so, ResourceLocationArgument.getId(so, "resource_location")));
        CommandUtil.add(literalargumentbuilder, "unbanAll", BanEntityCommand::unbanAll);
        CommandUtil.add(literalargumentbuilder, "list", BanEntityCommand::printList);
        event.getDispatcher().register(literalargumentbuilder);
    }

    private static int banEntityTypeRL(CommandContext<CommandSourceStack> source, ResourceLocation resourceLocation) {
        if (!MegaUtil.getT(resourceLocation)) {
            MegaUtil.addT(resourceLocation);
            source.getSource().sendSuccess(new TextComponent(I18n.get("entity_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation))).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("entity_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.RED), false);
        }
        Config.ENTITY_BANNED_LIST.save();
        return 0;
    }

    private static int unbanEntityRL(CommandContext<CommandSourceStack> source, ResourceLocation resourceLocation) {
        if (!MegaUtil.getT(resourceLocation)) {
            source.getSource().sendSuccess(new TranslatableComponent("entity_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.RED), false);
        }
        if (MegaUtil.getT(resourceLocation)) {
            MegaUtil.removeT(resourceLocation);
            source.getSource().sendSuccess(new TextComponent(I18n.get("entity_banning.command.unban") + MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.YELLOW), false);
        }
        Config.ENTITY_BANNED_LIST.save();
        return 0;
    }

    private static int banEntityType(CommandContext<CommandSourceStack> source, ResourceLocation resourceLocation) {
        if (!MegaUtil.getT(resourceLocation)) {
            MegaUtil.addT(resourceLocation);
            source.getSource().sendSuccess(new TextComponent(I18n.get("entity_banning.command.banned")).withStyle(ChatFormatting.YELLOW).append("").withStyle(ChatFormatting.RESET).append(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation))).withStyle(ChatFormatting.RED), false);
        } else {
            source.getSource().sendSuccess(new TranslatableComponent("entity_banning.command.had").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.RED), false);
        }
        Config.ENTITY_BANNED_LIST.save();
        return 0;
    }

    private static int unbanEntityType(CommandContext<CommandSourceStack> source, ResourceLocation resourceLocation) {
        if (!MegaUtil.getT(resourceLocation)) {
            source.getSource().sendSuccess(new TranslatableComponent("entity_banning.command.not_found").withStyle(ChatFormatting.RED), false);
            source.getSource().sendSuccess(new TextComponent(MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.RED), false);
        }
        if (MegaUtil.getT(resourceLocation)) {
            MegaUtil.removeT(resourceLocation);
            source.getSource().sendSuccess(new TextComponent(I18n.get("entity_banning.command.unban") + MegaUtil.getEntityTypeResourceLocationSimpleNameWithRegistryPath(resourceLocation)).withStyle(ChatFormatting.YELLOW), false);
        }
        Config.ENTITY_BANNED_LIST.save();
        return 0;
    }

    private static int printList(CommandContext<CommandSourceStack> source) {
        source.getSource().sendSuccess(new TextComponent(Config.ENTITY_BANNED_LIST.get().toString()), false);
        return 0;
    }

    private static int unbanAll(CommandContext<CommandSourceStack> source) {
        Config.ENTITY_BANNED_LIST.get().clear();
        Config.ENTITY_BANNED_LIST.save();
        source.getSource().sendSuccess(new TranslatableComponent("entity_banning.command.unbanALl").withStyle(ChatFormatting.YELLOW), false);
        MegaUtil.entity_banned_.clear();
        return 0;
    }
}
