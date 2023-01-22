package xclient.mega.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import xclient.mega.Main;

import java.awt.*;

public class Render2DUtil {
    public static void drawRect(PoseStack stack, int x, int y, int width, int height, int color) {
        Gui.fill(stack, x, y, x + width, y + height, color);
    }

    public static void drawRectWithString(PoseStack stack, int x, int y, int width, int height, int color, String s, Font font) {
        Gui.fill(stack, x, y, x + width, y + height, color);
        Gui.drawString(stack, font, s, x, y + height / 3, 0xFFFFFFFF);

    }

    public static void renderBackground() {
        drawRect(new PoseStack(), -1, -1, Minecraft.getInstance().getWindow().getGuiScaledWidth()+1, Minecraft.getInstance().getWindow().getGuiScaledHeight()+1, new Color(30, 30 ,30, Main.base_timehelper.integer_time).getRGB());
    }

    public static boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static void drawImage(PoseStack stack, ResourceLocation resourceLocation, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, resourceLocation);
        Gui.blit(stack, x, y, 0, 0, width, height, width, height);
    }

    public static void drawImage(PoseStack stack, String name, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, new ResourceLocation("xclient:textures" + name));
        Gui.blit(stack, x, y, 0, 0, width, height, width, height);
    }

    public static void drawProgressBar(PoseStack p_96183_, int p_96184_, int p_96185_, int p_96186_, int p_96187_, float p_96188_, float progress) {
        int i = Mth.ceil((float)(p_96186_ - p_96184_ - 2) * progress);
        int j = Math.round(p_96188_ * 255.0F);
        int k = FastColor.ARGB32.color(j, 180, 180, 180);
        GuiComponent.fill(p_96183_, p_96184_ + 2, p_96185_ + 2, p_96184_ + i, p_96187_ - 2, k);
        GuiComponent.fill(p_96183_, p_96184_ + 1, p_96185_, p_96186_ - 1, p_96185_ + 1, k);
        GuiComponent.fill(p_96183_, p_96184_ + 1, p_96187_, p_96186_ - 1, p_96187_ - 1, k);
        GuiComponent.fill(p_96183_, p_96184_, p_96185_, p_96184_ + 1, p_96187_, k);
        GuiComponent.fill(p_96183_, p_96186_, p_96185_, p_96186_ - 1, p_96187_, k);
    }
}
