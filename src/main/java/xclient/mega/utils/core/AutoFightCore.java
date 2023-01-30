package xclient.mega.utils.core;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ShieldItem;
import xclient.mega.Main;
import xclient.mega.utils.MegaUtil;
import xclient.mega.utils.RotationUtil;

import javax.annotation.Nullable;

public class AutoFightCore {
    @Nullable
    public static Entity Target = null;

    public static void tick(Player base) {
        if (Target == null)
            return;
        if (Target.isRemoved() || !(Target instanceof LivingEntity l))
            return;
        if (l.deathTime > 0)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode == null)
            return;
        if (Main.auto_fight_teleportation_tracking)
            base.moveTo(Target.position());
        if (Main.auto_fight_normal_tracking)
            mc.options.keyUp.setDown(true);
        if (Main.auto_fight_rotation)
            for (int i=0;i<10;i++)
                RotationUtil.rotationAtoB(base, Target);
        if (Main.auto_fight_attack_delay) {
            if (base.tickCount % 7 == 0)
                mc.gameMode.attack(base, Target);
        } else mc.gameMode.attack(base, Target);
        if (Main.auto_fight_auto_shield) {
            for (Entity e : base.level.getEntitiesOfClass(Entity.class, base.getBoundingBox().inflate(8))) {
                if (e == null)
                    break;
                if (e instanceof Projectile) {
                    for (int i=0;i<10;i++)
                        RotationUtil.rotationAtoB(base, e);
                    if (base.getOffhandItem().getItem() instanceof ShieldItem)
                        mc.options.keyRight.setDown(true);
                    if (base.getMainHandItem().getItem() instanceof ShieldItem)
                        mc.options.keyRight.setDown(true);
                }
            }
        }
    }
}
