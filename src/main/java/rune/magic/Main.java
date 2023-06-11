package rune.magic;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rune.magic.common.item.Register;

import java.util.Random;

@Mod(Main.ID)
public class Main {
    public static final String ID = "rune_magic";
    public static String version = "V1.0";
    public static CreativeModeTab tab;

    public static class VishantiSound {
        static Random random = new Random();
        public static int ambientSoundTime;
            public static void play(Entity entity) {
                if (random.nextInt(1000) < ambientSoundTime++) {
                    ambientSoundTime-=80;
                    entity.playSound(SoundEvents.WITHER_AMBIENT, 1.0F, ( random.nextFloat() - random.nextFloat()) * 0.2F + .6F);
                }
            }

    }
    public Main() {
        Register.register();
        rune.magic.common.entity.Register.register();
        tab = new CreativeModeTab("") {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Register.VISHANTI.get());
            }

            @Override
            public Component getDisplayName() {
                return new TextComponent(I18n.get("tab.ram.name")).withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC, ChatFormatting.BOLD);
            }
        };
        MinecraftForge.EVENT_BUS.register(this);
    }
}
