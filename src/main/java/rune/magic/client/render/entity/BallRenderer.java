package rune.magic.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import rune.magic.common.entity.BallEntity;
import rune.magic.utils.RendererUtils;
import rune.magic.utils.TimeHelper;

import java.awt.*;

public class BallRenderer extends EntityRenderer<BallEntity> {
    public static TimeHelper hel;
    private static final ResourceLocation PORTAL_TEXTURE = new ResourceLocation("rune_magic", "textures/entity/vortex.png");

    public BallRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(BallEntity p_114482_) {
        return PORTAL_TEXTURE;
    }

    public void render(BallEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        hel = TimeHelper.create(hel, 0, 300);
        Quaternion cameraRotation = this.entityRenderDispatcher.cameraOrientation();
        Quaternion portalRotation = new Quaternion(0.0F, cameraRotation.j(), 0.0F, cameraRotation.r());
        float spawnPct = (hel.integer_time < 20) ? (Math.min(hel.integer_time, 20) / 20.0F) : ((hel.integer_time > 180) ? (1.0F - Math.min(hel.integer_time - 180, 20) / 20.0F) : 1.0F);
        float portalSpinDegrees = (hel.integer_time * 3 % 360);
        float verticalOffset = 1.0F;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, verticalOffset, 0.0D);
        matrixStackIn.scale(spawnPct+1, spawnPct+1, spawnPct+1);
        matrixStackIn.mulPose(portalRotation);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(portalSpinDegrees));
        matrixStackIn.translate(0.0D, -0.25D, 0.0D);
        Color col = Color.WHITE;
        Color col2 = new Color(20, 200, 255, 150);
        RendererUtils.dragonDeathLight(spawnPct * 9.4f, matrixStackIn, col, col2, s -> s.scale(1,1,1));
        matrixStackIn.popPose();
    }


}
