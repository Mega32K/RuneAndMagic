package rune.magic.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rune.magic.Main;
import rune.magic.common.item.TheBookOfVishanti;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract ItemStack getItem();

    @Shadow private int age;

    @Shadow private int health;

    @Shadow @Final private static int LIFETIME;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (getItem().getItem() instanceof TheBookOfVishanti) {
            if (LIFETIME - age <= 1)
                age = 0;
            health = 64;
            Main.VishantiSound.play(this);
        }
    }

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    public void fireImmune(CallbackInfoReturnable<Boolean> cir){
        if (getItem().getItem() instanceof TheBookOfVishanti)  {
            cir.setReturnValue(true);
        }
    }
}
