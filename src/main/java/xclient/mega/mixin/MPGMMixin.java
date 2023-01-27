package xclient.mega.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xclient.mega.Main;

@Mixin(MultiPlayerGameMode.class)
public abstract class MPGMMixin {
    @Shadow
    private boolean isDestroying;

    @Shadow
    private BlockPos destroyBlockPos;

    @Shadow
    private ItemStack destroyingItem;

    @Shadow
    private float destroyProgress;

    @Shadow
    private float destroyTicks;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract boolean destroyBlock(BlockPos p_105268_);

    @Inject(method = "getPickRange", at = @At("RETURN"), cancellable = true)
    private void getPickRange(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValueF() + Main.reach_distance);
    }

    @Inject(method = "startDestroyBlock", at = @At("RETURN"))
    public void start(BlockPos p_105270_, Direction p_105271_, CallbackInfoReturnable<Boolean> cir) {

    }
}
