package rune.magic.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.function.Function;

public class RainbowFont extends Font {
    public static TimeHelper t;
    public static RainbowFont Rune = new RainbowFont((s) -> Minecraft.getInstance().fontManager.fontSets.getOrDefault(Minecraft.getInstance().fontManager.renames.getOrDefault(s, s), Minecraft.getInstance().fontManager.missingFontSet)) {
        @Override
        public int drawShadow(PoseStack stack, String text, float x, float y, int color) {
            if (text.contains(I18n.get("item.vishanti_book.name"))) {
                return super.drawShadow(stack, text, x, y, color) ;
            }
            return Minecraft.getInstance().font.drawShadow(stack, text, x, y, color);
        }
    };
    public static RainbowFont NORMAL = new RainbowFont((p_95014_) -> Minecraft.getInstance().fontManager.fontSets.getOrDefault(Minecraft.getInstance().fontManager.renames.getOrDefault(p_95014_, p_95014_), Minecraft.getInstance().fontManager.missingFontSet)) {
        public int drawShadow(PoseStack stack, String text, float x, float y, int color) {
            //float colorrStep = (float)rangeRemap(Math.sin(((float)milliTime() / 1200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
            float posX = x;
            int col = 191981;
            for (int i = 0; i < text.length(); i++) {
                int c = col * i;
                posX = super.drawShadow(stack, String.valueOf(text.charAt(i)), posX, y, c);
            }
            return (int) posX;
        }
    };

    public static RainbowFont INS = new RainbowFont((p_95014_) -> Minecraft.getInstance().fontManager.fontSets.getOrDefault(Minecraft.getInstance().fontManager.renames.getOrDefault(p_95014_, p_95014_), Minecraft.getInstance().fontManager.missingFontSet));

    public RainbowFont(Function<ResourceLocation, FontSet> p_92717_) {
        super(p_92717_);
    }

    public static long milliTime() {
        return System.nanoTime() / 1000000L;
    }

    public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }

    public int drawShadow(PoseStack stack, String text, float x, float y, int color) {
        float colorr = (float) milliTime() / 700.0F % 1.0F;
        float colorrStep = (float) rangeRemap(Math.sin(((float) milliTime() / 1200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        float posX = x;
        for (int i = 0; i < text.length(); i++) {
            int c = color & 0xFF000000 | MathHelper.hsvToRGB(colorr, 0.75F, 0.4F);
            posX = super.drawShadow(stack, String.valueOf(text.charAt(i)), posX, y, c);
            colorr += colorrStep;
            colorr %= 1.0F;
        }
        return (int) posX;
    }
}
