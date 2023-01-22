package xclient.mega.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.screen.YScreen;
import xclient.mega.event.RenderEvent;

import javax.annotation.Nullable;
import java.util.Locale;

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

    @Shadow @Mutable
    private float zoom;

    @Shadow public abstract void loadEffect(ResourceLocation p_109129_);

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void hurtEffect(PoseStack p_109118_, float p_109119_, CallbackInfo ci) {
        if (!Main.enableHurtEffect)
            ci.cancel();

    }

    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    public void render(float p_109094_, long p_109095_, boolean p_109096_) {
        zoom = Main.zoom;
        minecraft.getWindow().setTitle("");
        if (!this.minecraft.isWindowActive() && this.minecraft.options.pauseOnLostFocus && (!this.minecraft.options.touchscreen || !this.minecraft.mouseHandler.isRightPressed())) {
            if (Util.getMillis() - this.lastActiveTime > 500L) {
                this.minecraft.pauseGame(false);
            }
        } else {
            this.lastActiveTime = Util.getMillis();
        }

        if (!this.minecraft.noRender) {
            int i = (int) (this.minecraft.mouseHandler.xpos() * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth());
            int j = (int) (this.minecraft.mouseHandler.ypos() * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight());
            RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            if (p_109096_ && this.minecraft.level != null) {
                this.minecraft.getProfiler().push("level");
                this.renderLevel(p_109094_, p_109095_, new PoseStack());
                this.tryTakeScreenshotIfNeeded();
                this.minecraft.levelRenderer.doEntityOutline();
                if (this.postEffect != null && this.effectActive) {
                    RenderSystem.disableBlend();
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableTexture();
                    RenderSystem.resetTextureMatrix();
                    this.postEffect.process(p_109094_);
                }

                this.minecraft.getMainRenderTarget().bindWrite(true);
            }

            Window window = this.minecraft.getWindow();
            RenderSystem.clear(256, Minecraft.ON_OSX);
            Matrix4f matrix4f = Matrix4f.orthographic(0.0F, (float) ((double) window.getWidth() / window.getGuiScale()), 0.0F, (float) ((double) window.getHeight() / window.getGuiScale()), 1000.0F, net.minecraftforge.client.ForgeHooksClient.getGuiFarPlane());
            RenderSystem.setProjectionMatrix(matrix4f);
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.setIdentity();
            posestack.translate(0.0D, 0.0D, 1000F - net.minecraftforge.client.ForgeHooksClient.getGuiFarPlane());
            RenderSystem.applyModelViewMatrix();
            Lighting.setupFor3DItems();
            PoseStack posestack1 = new PoseStack();
            if (p_109096_ && this.minecraft.level != null) {
                this.minecraft.getProfiler().popPush("gui");
                if (this.minecraft.player != null) {
                    float f = Mth.lerp(p_109094_, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime);
                    if (f > 0.0F && this.minecraft.player.hasEffect(MobEffects.CONFUSION) && !Main.dner && this.minecraft.options.screenEffectScale < 1.0F) {
                        this.renderConfusionOverlay(f * (1.0F - this.minecraft.options.screenEffectScale));
                    }
                }

                if (!this.minecraft.options.hideGui || this.minecraft.screen != null) {
                    this.renderItemActivationAnimation(this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), p_109094_);
                    this.minecraft.gui.render(posestack1, p_109094_);
                    MinecraftForge.EVENT_BUS.post(new RenderEvent());
                    RenderSystem.clear(256, Minecraft.ON_OSX);
                }

                this.minecraft.getProfiler().pop();
            }

            if (this.minecraft.getOverlay() != null) {
                try {
                    this.minecraft.getOverlay().render(posestack1, i, j, this.minecraft.getDeltaFrameTime());
                } catch (Throwable throwable2) {
                    CrashReport crashreport = CrashReport.forThrowable(throwable2, "Rendering overlay");
                    CrashReportCategory crashreportcategory = crashreport.addCategory("Overlay render details");
                    crashreportcategory.setDetail("Overlay name", () -> this.minecraft.getOverlay().getClass().getCanonicalName());
                    throw new ReportedException(crashreport);
                }
            } else if (this.minecraft.screen != null) {
                try {
                    net.minecraftforge.client.ForgeHooksClient.drawScreen(this.minecraft.screen, posestack1, i, j, this.minecraft.getDeltaFrameTime());
                } catch (Throwable throwable1) {
                    CrashReport crashreport1 = CrashReport.forThrowable(throwable1, "Rendering screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.addCategory("Screen render details");
                    crashreportcategory1.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                    crashreportcategory1.setDetail("Mouse location", () -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%f, %f)", i, j, this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos()));
                    crashreportcategory1.setDetail("Screen size", () -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f", this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), this.minecraft.getWindow().getGuiScale()));
                    throw new ReportedException(crashreport1);
                }

                try {
                    if (this.minecraft.screen != null) {
                        this.minecraft.screen.handleDelayedNarration();
                    }
                } catch (Throwable throwable) {
                    CrashReport crashreport2 = CrashReport.forThrowable(throwable, "Narrating screen");
                    CrashReportCategory crashreportcategory2 = crashreport2.addCategory("Screen details");
                    crashreportcategory2.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                    throw new ReportedException(crashreport2);
                }
            }

        }
    }

    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    public void renderLevel(float p_109090_, long p_109091_, PoseStack p_109092_) {
        this.lightTexture().updateLightTexture(p_109090_);
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(this.minecraft.player);
        }

        this.pick(p_109090_);
        this.minecraft.getProfiler().push("center");
        boolean flag = this.shouldRenderBlockOutline();
        this.minecraft.getProfiler().popPush("camera");
        Camera camera = this.mainCamera;
        this.renderDistance = (float) (this.minecraft.options.getEffectiveRenderDistance() * 16);
        PoseStack posestack = new PoseStack();
        double d0 = this.getFov(camera, p_109090_, true);
        posestack.last().pose().multiply(this.getProjectionMatrix(d0));
        this.bobHurt(posestack, p_109090_);
        if (this.minecraft.options.bobView) {
            this.bobView(posestack, p_109090_);
        }

        float f = Mth.lerp(p_109090_, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime) * this.minecraft.options.screenEffectScale * this.minecraft.options.screenEffectScale;
        if (f > 0.0F) {
            int i = (this.minecraft.player.hasEffect(MobEffects.CONFUSION) && !Main.dner) ? 7 : 20;
            float f1 = 5.0F / (f * f + 5.0F) - f * 0.04F;
            f1 *= f1;
            Vector3f vector3f = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
            posestack.mulPose(vector3f.rotationDegrees(((float) this.tick + p_109090_) * (float) i));
            posestack.scale(1.0F / f1, 1.0F, 1.0F);
            float f2 = -((float) this.tick + p_109090_) * (float) i;
            posestack.mulPose(vector3f.rotationDegrees(f2));
        }

        Matrix4f matrix4f = posestack.last().pose();
        this.resetProjectionMatrix(matrix4f);
        camera.setup(this.minecraft.level, this.minecraft.getCameraEntity() == null ? this.minecraft.player : this.minecraft.getCameraEntity(), !this.minecraft.options.getCameraType().isFirstPerson(), this.minecraft.options.getCameraType().isMirrored(), p_109090_);

        net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup((GameRenderer) (Object) this, camera, p_109090_);
        camera.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
        p_109092_.mulPose(Vector3f.ZP.rotationDegrees(cameraSetup.getRoll()));

        p_109092_.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        p_109092_.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));
        Matrix3f matrix3f = p_109092_.last().normal().copy();
        if (matrix3f.invert()) {
            RenderSystem.setInverseViewRotationMatrix(matrix3f);
        }

        this.minecraft.levelRenderer.prepareCullFrustum(p_109092_, camera.getPosition(), this.getProjectionMatrix(Math.max(d0, this.minecraft.options.fov)));
        this.minecraft.levelRenderer.renderLevel(p_109092_, p_109090_, p_109091_, flag, camera, (GameRenderer) (Object) this, this.lightTexture, matrix4f);
        this.minecraft.getProfiler().popPush("forge_render_last");
        net.minecraftforge.client.ForgeHooksClient.dispatchRenderLast(this.minecraft.levelRenderer, p_109092_, p_109090_, matrix4f, p_109091_);
        this.minecraft.getProfiler().popPush("hand");
        if (this.renderHand) {
            RenderSystem.clear(256, Minecraft.ON_OSX);
            this.renderItemInHand(p_109092_, camera, p_109090_);
        }

        this.minecraft.getProfiler().pop();
    }

    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    private void renderItemInHand(PoseStack p_109121_, Camera p_109122_, float p_109123_) {
        if (!this.panoramicMode) {
            this.resetProjectionMatrix(this.getProjectionMatrix(this.getFov(p_109122_, p_109123_, false)));
            PoseStack.Pose posestack$pose = p_109121_.last();
            posestack$pose.pose().setIdentity();
            posestack$pose.normal().setIdentity();
            p_109121_.pushPose();
            this.bobHurt(p_109121_, p_109123_);
            if (this.minecraft.options.bobView) {
                this.bobView(p_109121_, p_109123_);
            }

            boolean flag = this.minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity) this.minecraft.getCameraEntity()).isSleeping();
            if (this.minecraft.options.getCameraType().isFirstPerson() && !flag && !this.minecraft.options.hideGui && (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || YScreen.display_players)) {
                this.lightTexture.turnOnLightLayer();
                this.itemInHandRenderer.renderHandsWithItems(p_109123_, p_109121_, this.renderBuffers.bufferSource(), this.minecraft.player, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, p_109123_));
                this.lightTexture.turnOffLightLayer();
            }

            p_109121_.popPose();
            if (this.minecraft.options.getCameraType().isFirstPerson() && !flag) {
                ScreenEffectRenderer.renderScreenEffect(this.minecraft, p_109121_);
                this.bobHurt(p_109121_, p_109123_);
            }

            if (this.minecraft.options.bobView) {
                this.bobView(p_109121_, p_109123_);
            }

        }
    }

    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    private boolean shouldRenderBlockOutline() {
        if (!this.renderBlockOutline) {
            return false;
        } else {
            Entity entity = this.minecraft.getCameraEntity();
            boolean flag = entity instanceof Player && !this.minecraft.options.hideGui;
            if (flag && !((Player) entity).getAbilities().mayBuild) {
                ItemStack itemstack = ((LivingEntity) entity).getMainHandItem();
                HitResult hitresult = this.minecraft.hitResult;
                if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
                    BlockState blockstate = this.minecraft.level.getBlockState(blockpos);
                    if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR || YScreen.display_players) {
                        flag = blockstate.getMenuProvider(this.minecraft.level, blockpos) != null;
                    } else {
                        BlockInWorld blockinworld = new BlockInWorld(this.minecraft.level, blockpos, false);
                        Registry<Block> registry = this.minecraft.level.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY);
                        flag = !itemstack.isEmpty() && (itemstack.hasAdventureModeBreakTagForBlock(registry, blockinworld) || itemstack.hasAdventureModePlaceTagForBlock(registry, blockinworld));
                    }
                }
            }

            return flag;
        }
    }

    /**
     * @author mega
     * @reason ?
     */
    @Overwrite
    public void pick(float p_109088_) {
        Entity entity = this.minecraft.getCameraEntity();
        if (entity != null) {
            if (this.minecraft.level != null) {
                this.minecraft.getProfiler().push("pick");
                this.minecraft.crosshairPickEntity = null;
                double d0 = this.minecraft.gameMode.getPickRange();
                this.minecraft.hitResult = entity.pick(d0, p_109088_, false);
                Vec3 vec3 = entity.getEyePosition(p_109088_);
                boolean flag = false;
                int i = 3;
                double d1 = d0;
                if (this.minecraft.gameMode.hasFarPickRange()) {
                    d1 = 6.0D;
                    d0 = d1;
                } else {
                    if (d0 > 3.0D) {
                        flag = true;
                    }

                    d0 = d0;
                }

                d1 *= d1;
                if (this.minecraft.hitResult != null) {
                    d1 = this.minecraft.hitResult.getLocation().distanceToSqr(vec3);
                }

                Vec3 vec31 = entity.getViewVector(1.0F);
                Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
                float f = 1.0F;
                AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_172770_) -> {
                    return !p_172770_.isSpectator() && p_172770_.isPickable();
                }, d1);
                if (entityhitresult != null) {
                    Entity entity1 = entityhitresult.getEntity();
                    Vec3 vec33 = entityhitresult.getLocation();
                    double d2 = vec3.distanceToSqr(vec33);
                    if (d2 < d1 || this.minecraft.hitResult == null) {
                        this.minecraft.hitResult = entityhitresult;
                        if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                            this.minecraft.crosshairPickEntity = entity1;
                        }
                    }
                }

                this.minecraft.getProfiler().pop();
            }
        }
    }

}
