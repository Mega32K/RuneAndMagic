package xclient.mega.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.mod.bigmodule.type.RenderBm;
import xclient.mega.utils.XSynchedEntityData;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {

    public AbstractClientPlayerMixin(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientLevel p_108548_, GameProfile p_108549_, CallbackInfo ci) {
        RenderBm.players.add(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (Main.renderPlayerOutline) {
            MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING, 3, 1);
            addEffect(instance);
        }
    }

    @Override
    protected void tickDeath() {
        if (((Object) this) instanceof LocalPlayer player) {
            if (Main.respawn) {
                SynchedEntityData date = new XSynchedEntityData(this, getEntityData());
                date.set(LivingEntity.DATA_HEALTH_ID, 20F);
                player.connection.handleSetEntityData(new ClientboundSetEntityDataPacket(getId(), date, false));
            }
        }
        super.tickDeath();
    }
}
