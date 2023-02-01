package xclient.mega.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.event.Render2DEvent;
import xclient.mega.utils.core.CameraCore;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private Camera mainCamera;

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void hurtEffect(PoseStack p_109118_, float p_109119_, CallbackInfo ci) {
        if (!Main.enableHurtEffect)
            ci.cancel();

    }

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    public void renderItemInHand(PoseStack p_109121_, Camera p_109122_, float p_109123_, CallbackInfo ci) {
        if (Main.enabledCameraGhost) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(float p_109094_, long p_109095_, boolean p_109096_, CallbackInfo ci) {
        if (Main.enabledCameraGhost && minecraft.player != null && minecraft.level != null) {
            if (minecraft.options.keyUp.isDown()) {
                CameraCore.add(minecraft.cameraEntity.getLookAngle().scale(.2D * Main.cameraGhostSpeed));
            }

            if (minecraft.options.keyDown.isDown()) {
                CameraCore.add(minecraft.cameraEntity.getLookAngle().scale(-.2D * Main.cameraGhostSpeed));
            }

            if (minecraft.options.keyLeft.isDown()) {
                Vector3f v = new Vector3f(.1F * Main.cameraGhostSpeed, 0, 0);
                v.transform(mainCamera.rotation());
                CameraCore.add(new Vec3(v));
            }

            if (minecraft.options.keyRight.isDown()) {
                Vector3f v = new Vector3f(-.1F * Main.cameraGhostSpeed, 0, 0);
                v.transform(mainCamera.rotation());
                CameraCore.add(new Vec3(v));
            }
            if (minecraft.options.keyJump.isDown()) {
                CameraCore.add(0, 0.1 * Main.cameraGhostSpeed, 0);
            }
            if (minecraft.options.keyShift.isDown()) {
                CameraCore.add(0, -0.1 * Main.cameraGhostSpeed, 0);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    private void post(float p_109094_, long p_109095_, boolean p_109096_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new Render2DEvent());
    }

}
