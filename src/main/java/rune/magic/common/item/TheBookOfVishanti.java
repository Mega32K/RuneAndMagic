package rune.magic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import rune.magic.Main;
import rune.magic.utils.ColorPutter;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class TheBookOfVishanti extends BundleItem {
    public static Random r = new Random();
    @SubscribeEvent
    public static void addInfo(RenderTooltipEvent.Color event) {
        if (event.getItemStack().getItem() == Register.VISHANTI.get()) {
            event.setBorderStart(Mth.hsvToRgb(r.nextFloat(), 0.75F, 0.5F));
            event.setBorderEnd(Mth.hsvToRgb(r.nextFloat(), 0.75F, 0.5F));
        }
    }
    @Override
    public Component getName(ItemStack p_41458_) {
        return new TextComponent(ColorPutter.rune(I18n.get("item.vishanti_book.name")));
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BLOCK;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        p_40673_.startUsingItem(p_40674_);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(new TextComponent(""));
        p_41423_.add(new TextComponent(I18n.get("item.vishanti_book.hover")).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return Mth.hsvToRgb((float) (System.currentTimeMillis() / 1000), 0.75F, 0.4F);
    }


    public TheBookOfVishanti() {
        super(new Properties().stacksTo(1).defaultDurability(100).durability(99).fireResistant().tab(Main.tab));
    }
}
