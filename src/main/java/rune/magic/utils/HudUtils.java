package rune.magic.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.List;

public class HudUtils {
    public static final int BACKWARDS = -1;
    public static final int FORWARDS = 1;
    public static final int CURSOR_INSERT_WIDTH = 1;
    public static final int CURSOR_INSERT_COLOR = -3092272;
    public static final String CURSOR_APPEND_CHARACTER = "_";
    public static final int DEFAULT_TEXT_COLOR = 14737632;
    public static final int BORDER_COLOR_FOCUSED = -1;
    public static final int BORDER_COLOR = -6250336;
    public static final int BACKGROUND_COLOR = -16777216;
    public static Minecraft mc = Minecraft.getInstance();
    public static int MOUSE_X = (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth());
    public static int MOUSE_Y = (int) (mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight());

    public static void drawProgressBar(PoseStack stack, int x, int y, int width, int height, float percent, float alpha) {
        int i = Mth.ceil((float) (width - 2) * percent);
        int k = new Color(47, 94, 133, 150).getRGB();
        Render2DUtil.drawRect(stack, x + 2, y + 2, x + i, y + height - 2, k);
        Render2DUtil.drawRect(stack, x + 1, y, x + width - 1, y + 1, k);
        Render2DUtil.drawRect(stack, x + 1, y + height, x + width - 1, y + height - 1, k);
        Render2DUtil.drawRect(stack, x, y, x + 1, y + height, k);
        Render2DUtil.drawRect(stack, x + width, y, x + width - 1, y + height, k);
    }

    public static void fill(PoseStack p_96183_, int i, int i1, int i2, int i3, int k) {
        GuiComponent.fill(p_96183_, i, i1, i2, i3, k);
    }

    public static void fillGradient(PoseStack p_168741_, int p_168742_, int p_168743_, int p_168744_, int p_168745_, int p_168746_, int p_168747_, int p_168748_) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        fillGradient(p_168741_.last().pose(), bufferbuilder, p_168742_, p_168743_, p_168744_, p_168745_, p_168748_, p_168746_, p_168747_);
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void fillEnd(PoseStack p_168741_, int p_168742_, int p_168743_, int p_168744_, int p_168745_, int p_168746_, int p_168747_, int p_168748_) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeEndPortalShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        fillGradient(p_168741_.last().pose(), bufferbuilder, p_168742_, p_168743_, p_168744_, p_168745_, p_168748_, p_168746_, p_168747_);
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void fillGradient(Matrix4f p_93124_, VertexConsumer p_93125_, int p_93126_, int p_93127_, int p_93128_, int p_93129_, int p_93130_, int p_93131_, int p_93132_) {
        float f = (float) (p_93131_ >> 24 & 255) / 255.0F;
        float f1 = (float) (p_93131_ >> 16 & 255) / 255.0F;
        float f2 = (float) (p_93131_ >> 8 & 255) / 255.0F;
        float f3 = (float) (p_93131_ & 255) / 255.0F;
        float f4 = (float) (p_93132_ >> 24 & 255) / 255.0F;
        float f5 = (float) (p_93132_ >> 16 & 255) / 255.0F;
        float f6 = (float) (p_93132_ >> 8 & 255) / 255.0F;
        float f7 = (float) (p_93132_ & 255) / 255.0F;
        p_93125_.vertex(p_93124_, (float) p_93128_, (float) p_93127_, (float) p_93130_).color(f1, f2, f3, f).endVertex();
        p_93125_.vertex(p_93124_, (float) p_93126_, (float) p_93127_, (float) p_93130_).color(f1, f2, f3, f).endVertex();
        p_93125_.vertex(p_93124_, (float) p_93126_, (float) p_93129_, (float) p_93130_).color(f5, f6, f7, f4).endVertex();
        p_93125_.vertex(p_93124_, (float) p_93128_, (float) p_93129_, (float) p_93130_).color(f5, f6, f7, f4).endVertex();
    }

    public static void renderTooltip(PoseStack p_96618_, List<Component> p_96619_, int p_96620_, int p_96621_, Font font) {
        renderTooltipInternal(p_96618_, p_96619_, p_96620_, p_96621_, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), font);
    }

    public static void drawBackground(PoseStack stack, int x, int y, int width, int height, int color, int color2) {
        fill(stack, x - 1, y - 1, x + width + 1, y + height + 1, color);
        fill(stack, x, y, x + width, y + height, color2);
    }

    private static void renderTooltipInternal(PoseStack p_169384_, List<Component> p_169385_, int x, int y, int screen_width, int screen_height, Font font) {
        if (!p_169385_.isEmpty()) {
            int i = 0;
            int j = p_169385_.size() == 1 ? -2 : 0;

            for (Component component : p_169385_) {
                int k = font.width(component);
                if (k > i) {
                    i = k;
                }

                j += 5;
            }

            int j2 = x + 12;
            int k2 = y - 12;
            if (j2 + i > screen_width) {
                j2 -= 28 + i;
            }

            if (k2 + j + 6 > screen_height) {
                k2 = screen_height - j - 6;
            }

            p_169384_.pushPose();
            float f = Minecraft.getInstance().getItemRenderer().blitOffset;
            Minecraft.getInstance().getItemRenderer().blitOffset = 400.0F;
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f matrix4f = p_169384_.last().pose();
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 4, j2 + i + 3, k2 - 3, 400, 0xf0100010, 0xf0100010);
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 3, j2 + i + 3, k2 + j + 4, 400, 0xf0100010, 0xf0100010);
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 + j + 3, 400, 0xf0100010, 0xf0100010);
            fillGradient(matrix4f, bufferbuilder, j2 - 4, k2 - 3, j2 - 3, k2 + j + 3, 400, 0xf0100010, 0xf0100010);
            fillGradient(matrix4f, bufferbuilder, j2 + i + 3, k2 - 3, j2 + i + 4, k2 + j + 3, 400, 0xf0100010, 0xf0100010);
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + j + 3 - 1, 400, 0x505000FF, 0x5028007f);
            fillGradient(matrix4f, bufferbuilder, j2 + i + 2, k2 - 3 + 1, j2 + i + 3, k2 + j + 3 - 1, 400, 0x505000FF, 0x5028007f);
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 - 3 + 1, 400, 0x505000FF, 0x505000FF);
            fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 2, j2 + i + 3, k2 + j + 3, 400, 0x5028007f, 0x5028007f);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            bufferbuilder.end();
            BufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            p_169384_.translate(0.0D, 0.0D, 400.0D);
            int l1 = k2;

            for (int i2 = 0; i2 < p_169385_.size(); ++i2) {
                Component clienttooltipcomponent1 = p_169385_.get(i2);
                font.drawShadow(p_169384_, clienttooltipcomponent1.getString(), j2, l1, 0xFFFFFFF);
                l1 += 5 + (i2 == 0 ? 2 : 0);
            }

            multibuffersource$buffersource.endBatch();
            p_169384_.popPose();

            Minecraft.getInstance().getItemRenderer().blitOffset = f;
        }
    }
}
