package xclient.mega.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.screen.YScreen;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.Textures;

import javax.annotation.Nullable;
import java.awt.*;

@Mixin(Gui.class)
public abstract class GuiMixin extends GuiComponent {
    @Shadow
    @Final
    @Mutable
    protected static ResourceLocation WIDGETS_LOCATION;
    @Shadow
    @Final
    protected static ResourceLocation PUMPKIN_BLUR_LOCATION;
    @Shadow
    @Final
    protected static ResourceLocation POWDER_SNOW_OUTLINE_LOCATION;
    @Shadow
    protected int screenWidth;
    @Shadow
    protected int screenHeight;
    @Shadow
    @Final
    protected Minecraft minecraft;
    @Shadow
    protected float scopeScale;
    @Shadow
    @Final
    protected SpectatorGui spectatorGui;
    @Shadow
    @Final
    protected BossHealthOverlay bossOverlay;
    @Shadow
    @Final
    protected DebugScreenOverlay debugScreen;
    @Shadow
    @Nullable
    protected Component overlayMessageString;
    @Shadow
    protected int overlayMessageTime;
    @Shadow
    protected boolean animateOverlayMessageColor;
    @Shadow
    @Nullable
    protected Component title;
    @Shadow
    protected int titleTime;
    @Shadow
    protected int titleFadeOutTime;
    @Shadow
    protected int titleFadeInTime;
    @Shadow
    protected int titleStayTime;
    @Shadow
    @Nullable
    protected Component subtitle;
    @Shadow
    @Final
    protected SubtitleOverlay subtitleOverlay;
    @Shadow
    @Final
    protected PlayerTabOverlay tabList;
    @Shadow
    @Final
    protected ChatComponent chat;
    @Shadow
    protected int tickCount;

    @Shadow
    public abstract Font getFont();

    @Shadow
    protected abstract void renderVignette(Entity p_93068_);

    @Shadow
    protected abstract void renderSpyglassOverlay(float p_168676_);

    @Shadow
    protected abstract void renderTextureOverlay(ResourceLocation p_168709_, float p_168710_);

    @Shadow
    protected abstract void renderPortalOverlay(float p_93008_);
    @Shadow
    protected abstract void renderCrosshair(PoseStack p_93081_);

    @Shadow
    protected abstract void renderPlayerHealth(PoseStack p_93084_);

    @Shadow
    protected abstract void renderVehicleHealth(PoseStack p_93087_);

    @Shadow
    public abstract void renderDemoOverlay(PoseStack p_93078_);

    @Shadow
    protected abstract void renderEffects(PoseStack p_93029_);

    @Shadow
    public abstract void renderJumpMeter(PoseStack p_93034_, int p_93035_);

    @Shadow
    public abstract void renderExperienceBar(PoseStack p_93072_, int p_93073_);

    @Shadow
    public abstract void renderSelectedItemName(PoseStack p_93070_);

    @Shadow
    protected abstract void drawBackdrop(PoseStack p_93040_, Font p_93041_, int p_93042_, int p_93043_, int p_93044_);

    @Shadow
    protected abstract void displayScoreboardSidebar(PoseStack p_93037_, Objective p_93038_);

    @Shadow
    protected abstract void renderSavingIndicator(PoseStack p_193835_);

    @Shadow protected abstract Player getCameraPlayer();
    @Shadow @Final protected ItemRenderer itemRenderer;

    @Inject(method = "render", at = @At("RETURN"))
    private void clinit(CallbackInfo ci) {
        WIDGETS_LOCATION = Textures.WIDGETS_LOCATION;
    }

    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    public void render(PoseStack p_93031_, float p_93032_) {
        WIDGETS_LOCATION = Textures.WIDGETS_LOCATION;
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        Font font = this.getFont();
        RenderSystem.enableBlend();
        if (Minecraft.useFancyGraphics()) {
            this.renderVignette(this.minecraft.getCameraEntity());
        } else {
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
        }

        float f = this.minecraft.getDeltaFrameTime();
        this.scopeScale = Mth.lerp(0.5F * f, this.scopeScale, 1.125F);
        if (this.minecraft.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.player.isScoping()) {
                this.renderSpyglassOverlay(this.scopeScale);
            } else {
                this.scopeScale = 0.5F;
                ItemStack itemstack = this.minecraft.player.getInventory().getArmor(3);
                if (itemstack.is(Blocks.CARVED_PUMPKIN.asItem())) {
                    this.renderTextureOverlay(PUMPKIN_BLUR_LOCATION, 1.0F);
                }
            }
        }

        if (this.minecraft.player.getTicksFrozen() > 0) {
            this.renderTextureOverlay(POWDER_SNOW_OUTLINE_LOCATION, this.minecraft.player.getPercentFrozen());
        }

        float f2 = Mth.lerp(p_93032_, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime);
        if (f2 > 0.0F && !this.minecraft.player.hasEffect(MobEffects.CONFUSION)) {
            this.renderPortalOverlay(f2);
        }

        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR || YScreen.display_players) {
            this.spectatorGui.renderHotbar(p_93031_);
        } else if (!this.minecraft.options.hideGui) {
            this.renderHotbar(p_93032_, p_93031_);
        }

        if (!this.minecraft.options.hideGui) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
            RenderSystem.enableBlend();
            this.renderCrosshair(p_93031_);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.defaultBlendFunc();
            this.minecraft.getProfiler().push("bossHealth");
            this.bossOverlay.render(p_93031_);
            this.minecraft.getProfiler().pop();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
            if (this.minecraft.gameMode.canHurtPlayer()) {
                this.renderPlayerHealth(p_93031_);
            }

            this.renderVehicleHealth(p_93031_);
            RenderSystem.disableBlend();
            int i = this.screenWidth / 2 - 91;
            if (this.minecraft.player.isRidingJumpable()) {
                this.renderJumpMeter(p_93031_, i);
            } else if (this.minecraft.gameMode.hasExperience()) {
                this.renderExperienceBar(p_93031_, i);
            }

            if (this.minecraft.options.heldItemTooltips && (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || YScreen.display_players)) {
                this.renderSelectedItemName(p_93031_);
            } else if (this.minecraft.player.isSpectator()) {
                this.spectatorGui.renderTooltip(p_93031_);
            }
        }

        if (this.minecraft.player.getSleepTimer() > 0) {
            this.minecraft.getProfiler().push("sleep");
            RenderSystem.disableDepthTest();
            float f3 = (float) this.minecraft.player.getSleepTimer();
            float f1 = f3 / 100.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F - (f3 - 100.0F) / 10.0F;
            }

            int j = (int) (220.0F * f1) << 24 | 1052704;
            fill(p_93031_, 0, 0, this.screenWidth, this.screenHeight, j);
            RenderSystem.enableDepthTest();
            this.minecraft.getProfiler().pop();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (this.minecraft.isDemo()) {
            this.renderDemoOverlay(p_93031_);
        }

        this.renderEffects(p_93031_);
        if (this.minecraft.options.renderDebug) {
            this.debugScreen.render(p_93031_);
        }

        if (!this.minecraft.options.hideGui) {
            if (this.overlayMessageString != null && this.overlayMessageTime > 0) {
                this.minecraft.getProfiler().push("overlayMessage");
                float f4 = (float) this.overlayMessageTime - p_93032_;
                int i1 = (int) (f4 * 255.0F / 20.0F);
                if (i1 > 255) {
                    i1 = 255;
                }

                if (i1 > 8) {
                    p_93031_.pushPose();
                    p_93031_.translate(this.screenWidth / 2, this.screenHeight - 68, 0.0D);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int k1 = 16777215;
                    if (this.animateOverlayMessageColor) {
                        k1 = Mth.hsvToRgb(f4 / 50.0F, 0.7F, 0.6F) & 16777215;
                    }

                    int k = i1 << 24 & -16777216;
                    int l = font.width(this.overlayMessageString);
                    this.drawBackdrop(p_93031_, font, -4, l, 16777215 | k);
                    font.draw(p_93031_, this.overlayMessageString, (float) (-l / 2), -4.0F, k1 | k);
                    RenderSystem.disableBlend();
                    p_93031_.popPose();
                }

                this.minecraft.getProfiler().pop();
            }

            if (this.title != null && this.titleTime > 0) {
                this.minecraft.getProfiler().push("titleAndSubtitle");
                float f5 = (float) this.titleTime - p_93032_;
                int j1 = 255;
                if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
                    float f6 = (float) (this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime) - f5;
                    j1 = (int) (f6 * 255.0F / (float) this.titleFadeInTime);
                }

                if (this.titleTime <= this.titleFadeOutTime) {
                    j1 = (int) (f5 * 255.0F / (float) this.titleFadeOutTime);
                }

                j1 = Mth.clamp(j1, 0, 255);
                if (j1 > 8) {
                    p_93031_.pushPose();
                    p_93031_.translate(this.screenWidth / 2, this.screenHeight / 2, 0.0D);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    p_93031_.pushPose();
                    p_93031_.scale(4.0F, 4.0F, 4.0F);
                    int l1 = j1 << 24 & -16777216;
                    int i2 = font.width(this.title);
                    this.drawBackdrop(p_93031_, font, -10, i2, 16777215 | l1);
                    font.drawShadow(p_93031_, this.title, (float) (-i2 / 2), -10.0F, 16777215 | l1);
                    p_93031_.popPose();
                    if (this.subtitle != null) {
                        p_93031_.pushPose();
                        p_93031_.scale(2.0F, 2.0F, 2.0F);
                        int k2 = font.width(this.subtitle);
                        this.drawBackdrop(p_93031_, font, 5, k2, 16777215 | l1);
                        font.drawShadow(p_93031_, this.subtitle, (float) (-k2 / 2), 5.0F, 16777215 | l1);
                        p_93031_.popPose();
                    }

                    RenderSystem.disableBlend();
                    p_93031_.popPose();
                }

                this.minecraft.getProfiler().pop();
            }

            this.subtitleOverlay.render(p_93031_);
            Scoreboard scoreboard = this.minecraft.level.getScoreboard();
            Objective objective = null;
            PlayerTeam playerteam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
            if (playerteam != null) {
                int j2 = playerteam.getColor().getId();
                if (j2 >= 0) {
                    objective = scoreboard.getDisplayObjective(3 + j2);
                }
            }

            Objective objective1 = objective != null ? objective : scoreboard.getDisplayObjective(1);
            if (objective1 != null) {
                this.displayScoreboardSidebar(p_93031_, objective1);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            p_93031_.pushPose();
            p_93031_.translate(0.0D, this.screenHeight - 48, 0.0D);
            this.minecraft.getProfiler().push("chat");
            this.chat.render(p_93031_, this.tickCount);
            this.minecraft.getProfiler().pop();
            p_93031_.popPose();
            objective1 = scoreboard.getDisplayObjective(0);
            if (!this.minecraft.options.keyPlayerList.isDown() || this.minecraft.isLocalServer() && this.minecraft.player.connection.getOnlinePlayers().size() <= 1 && objective1 == null) {
                this.tabList.setVisible(false);
            } else {
                this.tabList.setVisible(true);
                this.tabList.render(p_93031_, this.screenWidth, scoreboard, objective1);
            }

            this.renderSavingIndicator(p_93031_);
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (minecraft.player != null && minecraft.player.hasEffect(MobEffects.CONFUSION) && Main.dner) {
            minecraft.player.removeEffect(MobEffects.CONFUSION);
        }
    }
    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    private void renderSlot(int p_168678_, int p_168679_, float p_168680_, Player p_168681_, ItemStack p_168682_, int p_168683_) {
        if (!p_168682_.isEmpty()) {
            PoseStack posestack = RenderSystem.getModelViewStack();
            float f = (float)p_168682_.getPopTime() - p_168680_;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                posestack.pushPose();
                posestack.translate((double)(p_168678_ + 8), (double)(p_168679_ + 12), 0.0D);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate((double)(-(p_168678_ + 8)), (double)(-(p_168679_ + 12)), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            this.itemRenderer.renderAndDecorateItem(p_168681_, p_168682_, p_168678_, p_168679_, p_168683_);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                posestack.popPose();
                RenderSystem.applyModelViewMatrix();
            }

            this.itemRenderer.renderGuiItemDecorations(this.minecraft.font, p_168682_, p_168678_, p_168679_);
        }
    }
    /**
     * @author mega
     * @reason dner
     */
    @Overwrite
    protected void renderHotbar(float p_93010_, PoseStack p_93011_) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            ItemStack itemstack = player.getOffhandItem();
            HumanoidArm humanoidarm = player.getMainArm().getOpposite();
            int i = this.screenWidth / 2;
            int j = this.getBlitOffset();
            this.setBlitOffset(-90);
            Color hotbarColor = new Color(0, 0, 0, 120);
            Render2DUtil.drawRect(p_93011_, i - 91, this.screenHeight - 22,182, 22, hotbarColor.getRGB());
            Render2DUtil.drawRect(p_93011_, i - 91 + player.getInventory().selected * 20, this.screenHeight - 22, 22, 22, Color.PINK.getRGB());
            if (!itemstack.isEmpty()) {
                if (humanoidarm == HumanoidArm.LEFT) {
                    Render2DUtil.drawRect(p_93011_, i - 91 - 29, this.screenHeight - 23,26, 22, hotbarColor.getRGB());
                } else {
                    Render2DUtil.drawRect(p_93011_, i + 91, this.screenHeight - 23, 29, 24, hotbarColor.getRGB());
                }
            }

            this.setBlitOffset(j);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int i1 = 1;

            for(int j1 = 0; j1 < 9; ++j1) {
                int k1 = i - 90 + j1 * 20 + 2;
                int l1 = this.screenHeight - 16 - 3;
                this.renderSlot(k1, l1, p_93010_, player, player.getInventory().items.get(j1), i1++);
            }

            if (!itemstack.isEmpty()) {
                int j2 = this.screenHeight - 16 - 3;
                if (humanoidarm == HumanoidArm.LEFT) {
                    this.renderSlot(i - 91 - 26, j2, p_93010_, player, itemstack, i1++);
                } else {
                    this.renderSlot(i + 91 + 10, j2, p_93010_, player, itemstack, i1++);
                }
            }

            if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
                float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int k2 = this.screenHeight - 20;
                    int l2 = i + 91 + 6;
                    if (humanoidarm == HumanoidArm.RIGHT) {
                        l2 = i - 91 - 22;
                    }

                    RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
                    int i2 = (int)(f * 19.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.blit(p_93011_, l2, k2, 0, 94, 18, 18);
                    this.blit(p_93011_, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2);
                }
            }

            RenderSystem.disableBlend();
        }
    }

}
