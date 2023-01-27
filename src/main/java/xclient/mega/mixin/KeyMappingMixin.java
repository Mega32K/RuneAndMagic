package xclient.mega.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xclient.mega.Main;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow @Final private String name;

    @Inject(method = "consumeClick", at = @At("HEAD"), cancellable = true)
    private void consumeClick(CallbackInfoReturnable<Boolean> cir) {
        if (Main.enabledCameraGhost)
            if (name.equals(Minecraft.getInstance().options.keyUp.getName()) || name.equals(Minecraft.getInstance().options.keyRight.getName()) || name.equals(Minecraft.getInstance().options.keyLeft.getName()) || name.equals(Minecraft.getInstance().options.keyDown.getName()) || name.equals(Minecraft.getInstance().options.keyJump.getName())) {
                cir.setReturnValue(false);
            }
    }
}
