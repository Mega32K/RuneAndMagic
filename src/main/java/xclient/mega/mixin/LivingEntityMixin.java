package xclient.mega.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xclient.mega.Main;
import xclient.mega.MegaUtil;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    protected boolean jumping;
    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow
    protected abstract void onEffectRemoved(MobEffectInstance p_21126_);

    @Inject(method = "tickEffects", at = @At("HEAD"))
    public void tickEntities(CallbackInfo ci) {
        Iterator<MobEffect> iterator = this.activeEffects.keySet().iterator();
        try {
            while (iterator.hasNext()) {
                MobEffect mobeffect = iterator.next();
                MobEffectInstance mobeffectinstance = this.activeEffects.get(mobeffect);
                if (MegaUtil.getE(mobeffect)) {
                    this.onEffectRemoved(mobeffectinstance);
                    iterator.remove();
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    public void addEffect(MobEffectInstance p_147208_, Entity p_147209_, CallbackInfoReturnable<Boolean> cir) {
        if (MegaUtil.getE(p_147208_.getEffect()))
            cir.setReturnValue(false);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (Main.jumping) {
            if (((Object) this) instanceof LocalPlayer)
                jumping = true;
        }
        if (MegaUtil.getT(getType())) {
            this.level.broadcastEntityEvent(this, (byte) 60);
            this.remove(RemovalReason.UNLOADED_TO_CHUNK);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("RETURN"), cancellable = true)
    public void tick2(CallbackInfo ci) {
        if (Main.jumping) {
            if (((Object) this) instanceof LocalPlayer)
                jumping = true;
        }
        if (MegaUtil.getT(getType())) {
            this.level.broadcastEntityEvent(this, (byte) 60);
            this.remove(RemovalReason.UNLOADED_TO_CHUNK);
            ci.cancel();
        }
    }
}
