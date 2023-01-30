package xclient.mega.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.event.Render2DEvent;
import xclient.mega.utils.core.CameraCore;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    public ItemInHandRenderer itemInHandRenderer;
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private long lastActiveTime;
    @Shadow
    private boolean effectActive;

    @Shadow
    @Nullable
    private PostChain postEffect;
    @Shadow
    @Final
    private Camera mainCamera;
    @Shadow
    private float renderDistance;
    @Shadow
    private int tick;
    @Shadow
    @Final
    private LightTexture lightTexture;
    @Shadow
    private boolean renderHand;
    @Shadow
    private boolean panoramicMode;
    @Shadow
    @Final
    private RenderBuffers renderBuffers;
    @Shadow
    private boolean renderBlockOutline;
    @Shadow
    @Mutable
    private float zoom;

    @Shadow
    protected abstract void tryTakeScreenshotIfNeeded();

    @Shadow
    protected abstract void renderConfusionOverlay(float p_109146_);

    @Shadow
    protected abstract void renderItemActivationAnimation(int p_109101_, int p_109102_, float p_109103_);

    @Shadow
    public abstract LightTexture lightTexture();

    @Shadow
    protected abstract double getFov(Camera p_109142_, float p_109143_, boolean p_109144_);

    @Shadow
    public abstract Matrix4f getProjectionMatrix(double p_172717_);

    @Shadow
    protected abstract void bobHurt(PoseStack p_109118_, float p_109119_);

    @Shadow
    protected abstract void bobView(PoseStack p_109139_, float p_109140_);

    @Shadow
    public abstract void resetProjectionMatrix(Matrix4f p_109112_);

    @Shadow
    public abstract void loadEffect(ResourceLocation p_109129_);

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
                CameraCore.add(minecraft.player.getLookAngle().scale(.2D * Main.cameraGhostSpeed));
            }

            if (minecraft.options.keyDown.isDown()) {
                CameraCore.add(minecraft.player.getLookAngle().scale(-.2D * Main.cameraGhostSpeed));
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
