package rune.magic.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PU2 {
    public double rotate = 0D;
    public double addRotate = 0.5D;
    public double r;
    public double R = 4;
    public double RR = 4;
    public double y = 0;
    public int speed = 4;
    public boolean usePos = false;
    public Vec3 pos;
    public Player player;
    public ParticleOptions options;

    public PU2(double r, double Mr, double rr, Player player, ParticleOptions options) {
        this.r = r;
        this.R = Mr;
        this.RR = rr;
        this.player = player;
        this.options = options;
    }

    public PU2 setY(double y) {
        this.y = y;
        return this;
    }

    public PU2 pos(Vec3 v) {
        this.pos = v;
        usePos = true;
        return this;
    }

    public PU2 rotationSpeed(int s) {
        this.speed = s;
        return this;
    }

    public void tick(int members) {
        {
            double x = player.getX();
            y = y != 0 ? y : player.getY();
            double z = player.getZ();
            if (usePos) {
                x = pos.x;
                y = pos.y;
                z = pos.z;
            }
            r -= 0.015D;
            if (r < -R)
                r = R;
            RR -= 0.015D;
            if (RR < -R)
                RR = R;
            double value;
            for (int t = 0; t < speed; t++) {
                rotate += addRotate;
                if (rotate == 360)
                    rotate = 0;
            }
            value = 0;
            for (int i = 0; i < members; i++) {
                double x_ = MegaUtil.circleX(x, r * 3 + 4, rotate + value);
                double z_ = MegaUtil.circleY(z, r * 3 + 4, rotate + value);
                player.level.addParticle(options, x_, y, z_, 0, 0, 0);
                value += 360D / members;
            }
        }
    }
}
