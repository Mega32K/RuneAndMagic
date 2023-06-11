package rune.magic.mixin;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rune.magic.common.item.TheBookOfVishanti;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Shadow public HumanoidModel.ArmPose rightArmPose;

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "poseRightArm", at = @At("RETURN") )
    public<T extends LivingEntity> void poseR(T p_102876_, CallbackInfo ci) {
        if (rightArmPose == HumanoidModel.ArmPose.BLOCK && p_102876_.getUseItem().getItem() instanceof TheBookOfVishanti) {
            this.rightArm.xRot = this.rightArm.xRot * 0.1F - 1.0F;
            this.rightArm.yRot = (-(float)Math.PI / 16F);
        }
    }

    @Inject(method = "poseLeftArm", at = @At("RETURN") )
    public<T extends LivingEntity> void poseL(T p_102876_, CallbackInfo ci) {
        if (rightArmPose == HumanoidModel.ArmPose.BLOCK && p_102876_.getUseItem().getItem() instanceof TheBookOfVishanti) {
            this.rightArm.xRot = this.rightArm.xRot * 0.1F - 1.0F;
            this.rightArm.yRot = (-(float)Math.PI / 16F);
        }
    }
}
