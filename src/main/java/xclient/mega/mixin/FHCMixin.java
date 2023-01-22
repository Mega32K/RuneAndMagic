package xclient.mega.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.utils.XSynchedEntityData;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class FHCMixin {

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private static void drawScreen(Screen screen, PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (screen instanceof DeathScreen && Main.respawn) {
            LocalPlayer player = Minecraft.getInstance().player;
            SynchedEntityData date = new XSynchedEntityData(player, player.getEntityData());
            date.set(LivingEntity.DATA_HEALTH_ID, 20F);
            player.connection.handleSetEntityData(new ClientboundSetEntityDataPacket(player.getId(), date, false));

            Minecraft.getInstance().screen = null;
            screen = null;
            Minecraft.getInstance().getSoundManager().resume();
            Minecraft.getInstance().mouseHandler.grabMouse();
            ci.cancel();
        }
    }
}
