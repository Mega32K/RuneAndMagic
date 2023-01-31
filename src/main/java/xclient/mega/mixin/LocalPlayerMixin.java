package xclient.mega.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xclient.mega.Main;
import xclient.mega.utils.PU;
import xclient.mega.utils.core.AutoFightCore;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow
    @Final
    public ClientPacketListener connection;

    @Shadow
    @Final
    protected Minecraft minecraft;

    public LocalPlayerMixin(ClientLevel p_108548_, GameProfile p_108549_) {
        super(p_108548_, p_108549_);
    }

    @Override
    public void attack(Entity p_36347_) {
        if (Main.critical) {
            connection.send(new ServerboundMovePlayerPacket.Pos(getX(), getY() + 1, getZ(), false));
            fallDistance = 0.5F;
        }
        super.attack(p_36347_);
        if (Main.critical)
            connection.send(new ServerboundMovePlayerPacket.Pos(getX(), getY() + 1, getZ(), false));
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt_b(DamageSource p_108662_, float p_108663_, CallbackInfoReturnable<Boolean> c) {
        if (p_108662_.isProjectile() && Main.arrow_dodge) {
            double d1 = Math.random() * 5 - 2.5;
            double d2 = Math.random() * 5 - 2.5;
            if (d1 <= 0)
                d1 -= 2;
            if (d1 > 0)
                d1 += 2;
            if (d2 <= 0)
                d2 -= 2;
            if (d2 > 0)
                d2 += 2;
            Vec3 vec3 = new Vec3(d1, 0, d2);
            connection.send(new ServerboundMovePlayerPacket.Pos(getX() + vec3.x, getY() + vec3.y, getZ() + vec3.z, false));
            setPos(getX() + vec3.x, getY() + vec3.y, getZ() + vec3.z);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo c) {
        if (Main.auto_fight && AutoFightCore.Target != null)
            AutoFightCore.tick(this);
        if (Main.respawn && deathTime > 0) {
            connection.send(new ServerboundChatPacket("/back"));
            connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            connection.send(new ServerboundChatPacket("/back"));
            deathTime = 0;
        }
        if (Main.no_fall && fallDistance > 0.5F) connection.send(new ServerboundMovePlayerPacket.StatusOnly(true));
        if (Main.jumping) {
            minecraft.options.keyJump.setDown(true);
        }
        if (Main.dner) {
            clearFire();
        }
        if (Main.fly) {
            Abilities abilities = new Abilities();
            abilities.mayfly = true;
            getAbilities().mayfly = true;
            connection.send(new ServerboundPlayerAbilitiesPacket(abilities));
        }
        if (Main.air_jump) {
            if (minecraft.options.keyJump.consumeClick() && minecraft.options.keyJump.isDown()) {
                if (getDeltaMovement().y <= 0.05) {
                    if (minecraft.options.keyUp.isDown())
                        setDeltaMovement(getLookAngle().x * Main.air_jump_speed, getLookAngle().y * Main.air_jump_speed, getLookAngle().z * Main.air_jump_speed);
                    else
                        setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y + Main.air_jump_speed + 0.5F, getDeltaMovement().z);

                    PU pu = new PU(level, ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ());
                    pu.spawnHorizontalCircle(0.1, 1);
                }
            }
            if (minecraft.options.keyShift.consumeClick()) {
                if (level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, blockPosition()).getY() > 6)
                    setDeltaMovement(getDeltaMovement().add(0, -2, 0));
            }
        }
        Abilities abilities = getAbilities();
        abilities.setFlyingSpeed(Main.speed/2F);
        abilities.invulnerable = true;
        connection.send(new ServerboundPlayerAbilitiesPacket(abilities));
    }
}
