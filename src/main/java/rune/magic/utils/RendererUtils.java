package rune.magic.utils;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import rune.magic.utils.core.PSLogic;

import java.awt.*;
import java.util.Random;

@Mod.EventBusSubscriber
public class RendererUtils {
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

    public static TimeHelper h;
    public static TimeHelper timeHelper;
    public static Minecraft mc = Minecraft.getInstance();

    public static double getRandom(double min, double max) {
        return Math.random() * (max - min) - (max - min) / 2;
    }

    public static double getRandom(double length) {
        return Math.random() * length - length / 2;
    }

    public static void drawItemOnScreen(int x, int y, ItemStack itemStack) {
        drawItemOnScreen(x, y, itemStack, new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), 1);
    }

    public static void drawItemOnScreen(int x, int y, ItemStack itemStack, Vector4f color) {
        drawItemOnScreen(x, y, itemStack, color, 1);
    }

    public static void drawItemOnScreen(int x, int y, ItemStack itemStack, Vector4f color, int scale) {
        drawItemOnScreen(mc.getItemRenderer(), x, y, itemStack, color, scale);
    }

    public static void drawItemOnScreen(ItemRenderer renderer, int x, int y, ItemStack itemStack, Vector4f color, int scale) {
        renderGuiItem(itemStack, x, y, renderer, scale, color);
    }

    public static void renderGuiItem(ItemStack p_115124_, int p_115125_, int p_115126_, ItemRenderer renderer, int scale, Vector4f color) {
        renderGuiItem(p_115124_, p_115125_, p_115126_, renderer.getModel(p_115124_, null, null, 0), renderer, scale, color);
    }

    public static void renderGuiItem(ItemStack p_115128_, int p_115129_, int p_115130_, BakedModel p_115131_, ItemRenderer renderer, int scale, Vector4f color) {
        mc.textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(color.x(), color.y(), color.z(), color.w());
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) p_115129_ * scale, (double) p_115130_ * scale, 100.0F * scale);
        posestack.translate(8.0D * scale, 8.0D * scale, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !p_115131_.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        renderer.render(p_115128_, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, p_115131_);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void vertex01(VertexConsumer p_114220_, Matrix4f p_114221_, int p_114222_, Color c) {
        p_114220_.vertex(p_114221_, 0.0F, 0.0F, 0.0F).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
    }

    private static void vertex2(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_, Color c) {
        p_114215_.vertex(p_114216_, -HALF_SQRT_3 * p_114218_, p_114217_, -0.5F * p_114218_).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
    }

    private static void vertex3(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_, Color c) {
        p_114224_.vertex(p_114225_, HALF_SQRT_3 * p_114227_, p_114226_, -0.5F * p_114227_).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
    }

    private static void vertex4(VertexConsumer p_114229_, Matrix4f p_114230_, float p_114231_, float p_114232_, Color c) {
        p_114229_.vertex(p_114230_, 0.0F, p_114231_, 1.0F * p_114232_).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
    }

    public static void dragonDeathLight(float time, PoseStack stack, Color point, Color end, PSLogic l) {
        float f5 = time;
        float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        Random random = new Random(432L);
        MultiBufferSource.BufferSource p_114212_ = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer vertexconsumer2 = p_114212_.getBuffer(RenderType.lightning());


        // stack.scale(0.01f,0.01f,0.01f);
        for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
            l.todo(stack);
            float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            int j = (int) (255.0F * (1.0F - f7));
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex2(vertexconsumer2, matrix4f, f3, f4, end);
            vertex3(vertexconsumer2, matrix4f, f3, f4, end);
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex3(vertexconsumer2, matrix4f, f3, f4, end);
            vertex4(vertexconsumer2, matrix4f, f3, f4, end);
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex4(vertexconsumer2, matrix4f, f3, f4, end);
            vertex2(vertexconsumer2, matrix4f, f3, f4, end);
        }
        for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
            l.todo(stack);
            float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            int j = (int) (255.0F * (1.0F - f7));
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex2(vertexconsumer2, matrix4f, f3, f4, end);
            vertex3(vertexconsumer2, matrix4f, f3, f4, end);
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex3(vertexconsumer2, matrix4f, f3, f4, end);
            vertex4(vertexconsumer2, matrix4f, f3, f4, end);
            vertex01(vertexconsumer2, matrix4f, j, point);
            vertex4(vertexconsumer2, matrix4f, f3, f4, end);
            vertex2(vertexconsumer2, matrix4f, f3, f4, end);
        }
    }

    public static void renderSphere(PoseStack matrix, MultiBufferSource buf, float radius, int gradation, int lx, int ly, float r, float g, float b, float a, RenderType type, float percentage) {
        float PI = 3.141592F;
        VertexConsumer bb = buf.getBuffer(type);
        Matrix4f m = matrix.last().pose();
        float alpha;
        for (alpha = 0.0F; alpha < 3.141592F; alpha += 3.141592F / gradation) {
            float beta;
            for (beta = 0.0F; beta < 6.31459997177124D * percentage; beta += 3.141592F / gradation) {
                float x = (float)(radius * Math.cos(beta) * Math.sin(alpha));
                float y = (float)(radius * Math.sin(beta) * Math.sin(alpha));
                float z = (float)(radius * Math.cos(alpha));
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).uv2(lx, ly).endVertex();
                x = (float)(radius * Math.cos(beta) * Math.sin((alpha + 3.141592F / gradation)));
                y = (float)(radius * Math.sin(beta) * Math.sin((alpha + 3.141592F / gradation)));
                z = (float)(radius * Math.cos((alpha + 3.141592F / gradation)));
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).uv2(lx, ly).endVertex();
            }
        }
    }

    public static void renderSphere(PoseStack matrix, MultiBufferSource buf, float radius, int gradation, int lx, int ly, float r, float g, float b, float a, RenderType type) {
        renderSphere(matrix, buf, radius, gradation, lx, ly, r, g, b, a, type, 1.0F);
    }

    public static void drawText3d(PoseStack matrix, String name, Vec3 point, Camera camera) {
        float scaleToGui = 0.025f;

        Minecraft client = Minecraft.getInstance();
        float tickDelta = client.getDeltaFrameTime();

        double x = Mth.lerp(tickDelta, point.x(), point.x());
        double y = Mth.lerp(tickDelta, point.y(), point.y());
        double z = Mth.lerp(tickDelta, point.z(), point.z());

        Vec3 camPos = camera.getPosition();
        double camX = camPos.x;
        double camY = camPos.y;
        double camZ = camPos.z;

        matrix.pushPose();
        matrix.translate(x - camX, y - camY, z - camZ);
        matrix.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
        matrix.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        drawDamageNumber(matrix, name, 0, 0, 10);

        RenderSystem.disableBlend();

        matrix.popPose();
    }

    public static void drawDamageNumber(PoseStack matrix, String dmg, double x, double y,
                                        float width) {
        Minecraft minecraft = Minecraft.getInstance();
        int sw = minecraft.font.width(dmg);
        int color = 0;
        RainbowFont.INS.drawShadow(matrix, dmg, (int) (x + (width / 2) - sw), (int) y + 5, color);
    }

    public static void setup() {
        timeHelper = TimeHelper.create(timeHelper, 20, 170);
    }


}
