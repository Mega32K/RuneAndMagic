package xclient.mega.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BowItem;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.MegaUtil;
import xclient.mega.event.GameUpdateEvent;
import xclient.mega.utils.TimeHelper;

import javax.annotation.Nullable;
import java.io.InputStream;

@Mixin(value = Minecraft.class, priority = 999)
public abstract class MinecraftMixin {
    @Shadow
    public Window window;
    @Shadow
    public Options options;
    @Shadow
    @Nullable
    public LocalPlayer player;
    @Shadow
    public Gui gui;
    @Shadow
    public LevelRenderer levelRenderer;
    @Shadow
    public GameRenderer gameRenderer;
    @Shadow
    @Nullable
    public Screen screen;
    @Shadow
    @Nullable
    public TutorialToast socialInteractionsToast;
    @Shadow
    public Tutorial tutorial;
    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Shadow
    @Nullable
    public Overlay overlay;
    @Shadow
    public int rightClickDelay;
    @Shadow
    public int missTime;
    @Shadow
    public MouseHandler mouseHandler;
    @Shadow
    @Nullable
    public Entity cameraEntity;

    @Shadow
    public abstract Window getWindow();

    @Shadow
    @Nullable
    public abstract Entity getCameraEntity();

    @Shadow
    protected abstract boolean isMultiplayerServer();

    @Shadow
    public abstract void setScreen(@org.jetbrains.annotations.Nullable Screen p_91153_);

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

    @Shadow
    protected abstract void openChatScreen(String p_91327_);

    @Shadow
    protected abstract boolean startAttack();

    @Shadow
    protected abstract void startUseItem();

    @Shadow
    protected abstract void pickBlock();

    @Shadow
    protected abstract void continueAttack(boolean p_91387_);

    @Shadow
    public abstract void run();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        Main.base_timehelper = Main.timeHelper = null;
        Main.base_timehelper = TimeHelper.create(null, 20, 160);
        Main.timeHelper = TimeHelper.create(null, 10, 170);
        getWindow().setTitle("");
        InputStream inputstream = Main.class.getResourceAsStream("/assets/x_client/textures/icon.png");
        if (inputstream != null) {
            this.window.setIcon(inputstream, inputstream);
        }
    }


    @Inject(method = "run", at = @At("HEAD"))
    private void run(CallbackInfo ci) {
        getWindow().setTitle("");
        MinecraftForge.EVENT_BUS.post(new GameUpdateEvent());
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void close(CallbackInfo ci) {
        MegaUtil.writeXCLIENT();
    }

    /**
     * @author mega
     * @reason inject KeyInventory
     */
    @Overwrite
    @SuppressWarnings("ALL")
    private void handleKeybinds() {
        for (; this.options.keyTogglePerspective.consumeClick(); this.levelRenderer.needsUpdate()) {
            CameraType cameratype = this.options.getCameraType();
            this.options.setCameraType(this.options.getCameraType().cycle());
            if (cameratype.isFirstPerson() != this.options.getCameraType().isFirstPerson()) {
                this.gameRenderer.checkEntityPostEffect(this.options.getCameraType().isFirstPerson() ? this.getCameraEntity() : null);
            }
        }

        while (this.options.keySmoothCamera.consumeClick()) {
            this.options.smoothCamera = !this.options.smoothCamera;
        }

        for (int i = 0; i < 9; ++i) {
            boolean flag = this.options.keySaveHotbarActivator.isDown();
            boolean flag1 = this.options.keyLoadHotbarActivator.isDown();
            if (this.options.keyHotbarSlots[i].consumeClick()) {
                if (this.player.isSpectator()) {
                    this.gui.getSpectatorGui().onHotbarSelected(i);
                } else if (!this.player.isCreative() || this.screen != null || !flag1 && !flag) {
                    this.player.getInventory().selected = i;
                } else {
                    CreativeModeInventoryScreen.handleHotbarLoadOrSave(Minecraft.getInstance(), i, flag1, flag);
                }
            }
        }

        while (this.options.keySocialInteractions.consumeClick()) {
            if (!this.isMultiplayerServer()) {
                this.player.displayClientMessage(Minecraft.SOCIAL_INTERACTIONS_NOT_AVAILABLE, true);
                NarratorChatListener.INSTANCE.sayNow(Minecraft.SOCIAL_INTERACTIONS_NOT_AVAILABLE);
            } else {
                if (this.socialInteractionsToast != null) {
                    this.tutorial.removeTimedToast(this.socialInteractionsToast);
                    this.socialInteractionsToast = null;
                }

                this.setScreen(new SocialInteractionsScreen());
            }
        }

        while (this.options.keyInventory.consumeClick() && player != null) {
            if (this.gameMode.isServerControlledInventory()) {
                xclient.mega.utils.MegaUtil.really_sendOpenInv(player, (cameraEntity instanceof AbstractClientPlayer p ? p : player));
            } else {
                this.tutorial.onOpenInventory();
                this.setScreen(new InventoryScreen((cameraEntity instanceof AbstractClientPlayer p ? p : player)));
            }
        }

        while (this.options.keyAdvancements.consumeClick()) {
            this.setScreen(new AdvancementsScreen(this.player.connection.getAdvancements()));
        }

        while (this.options.keySwapOffhand.consumeClick()) {
            if (!this.player.isSpectator()) {
                this.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
            }
        }

        while (this.options.keyDrop.consumeClick()) {
            if (!this.player.isSpectator() && this.player.drop(Screen.hasControlDown())) {
                this.player.swing(InteractionHand.MAIN_HAND);
            }
        }

        while (this.options.keyChat.consumeClick()) {
            this.openChatScreen("");
        }

        if (this.screen == null && this.overlay == null && this.options.keyCommand.consumeClick()) {
            this.openChatScreen("/");
        }

        boolean flag2 = false;
        if (this.player.isUsingItem()) {
            if (!this.options.keyUse.isDown() || (player.getTicksUsingItem() >= 18 && player.getUseItem().getItem() instanceof BowItem && Main.auto_release)) {
                this.gameMode.releaseUsingItem(this.player);
            }

            while (this.options.keyAttack.consumeClick()) {
            }

            while (this.options.keyUse.consumeClick()) {
            }

            while (this.options.keyPickItem.consumeClick()) {
            }
        } else {
            while (this.options.keyAttack.consumeClick()) {
                flag2 |= this.startAttack();
                missTime = 0;
            }

            while (this.options.keyUse.consumeClick()) {
                this.startUseItem();
            }

            while (this.options.keyPickItem.consumeClick()) {
                this.pickBlock();
            }
        }

        if (this.options.keyUse.isDown() && this.rightClickDelay == 0 && !this.player.isUsingItem()) {
            this.startUseItem();
        }

        this.continueAttack(this.screen == null && !flag2 && this.options.keyAttack.isDown() && this.mouseHandler.isMouseGrabbed());
    }

    /**
     * @author mega
     * @reason ?
     */
    @Overwrite
    public boolean shouldEntityAppearGlowing(Entity p_91315_) {
        if (p_91315_ instanceof AbstractClientPlayer pla) {
            if (Main.renderPlayerOutline && pla != player)
                return true;
        }
        return p_91315_.isCurrentlyGlowing() || this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isDown() && p_91315_.getType() == EntityType.PLAYER;
    }

}
