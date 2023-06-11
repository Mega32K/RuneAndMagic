package rune.magic.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rune.magic.common.item.TheBookOfVishanti;
import rune.magic.utils.RendererUtils;
import rune.magic.utils.TimeHelper;

import java.awt.*;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRenderMixin {
    @Shadow
    public abstract Vec3 getRenderOffset(Entity par1, float par2);

    @Inject(method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    public <T extends ItemFrame> void render(T p_115076_, float p_115077_, float p_115078_, PoseStack p_115079_, MultiBufferSource p_115080_, int p_115081_, CallbackInfo ci) {
        if (p_115076_.getItem().getItem() instanceof TheBookOfVishanti) {
            if (RendererUtils.h == null)
                RendererUtils.h = new TimeHelper(0d, 2000d);
            PoseStack stack = p_115079_;
            Color col = Color.WHITE;
            Color col2 = new Color(20, 200, 255, 150);
            stack.pushPose();
            Direction direction = p_115076_.getDirection();
            Vec3 vec3 = this.getRenderOffset(p_115076_, p_115078_);
            stack.translate(-vec3.x(), -vec3.y(), -vec3.z());
            stack.translate((double) direction.getStepX() * 0.46875D, (double) direction.getStepY() * 0.46875D, (double) direction.getStepZ() * 0.46875D);
            stack.mulPose(Vector3f.XP.rotationDegrees(p_115076_.getXRot()));
            stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_115076_.getYRot()));
            p_115079_.mulPose(Vector3f.ZP.rotationDegrees((float) p_115076_.getRotation() * 360.0F / 8.0F));
            stack.translate(0, 0, 0.5);
            stack.scale(1, 1, 0);
            stack.scale(0.1f, 0.1f, 0.1f);
            RendererUtils.dragonDeathLight((float) RendererUtils.h.double_time / 1000F, stack, col, col2, (t) -> t.translate(0, 0, 0));
            stack.popPose();
        }
    }
}
