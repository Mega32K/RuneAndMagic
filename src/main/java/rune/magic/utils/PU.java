package rune.magic.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PU {
    public static ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
    public Level level;
    public ParticleOptions particleOptions;
    public double x;
    public double y;
    public double z;
    public Player p;

    public PU(Level level, ParticleOptions particleOptions, double x, double y, double z) {
        this.level = level;
        this.particleOptions = particleOptions;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PU(Level level, ParticleOptions particleOptions, Player player) {
        this(level, particleOptions, player.position());
        p = player;
    }

    public PU(Level level, ParticleOptions particleOptions, Vec3 position) {
        this(level, particleOptions, position.x, position.y, position.z);
    }

    public PU(Level level, ParticleOptions particleOptions) {
        this(level, particleOptions, 0D, 0D, 0D);
    }

    public PU y(double y) {
        this.y = y;
        return this;
    }

    public void spawnVerticalCircle(double detail, double r, boolean front) {
        if (front) {
            for (double i = 0.0D; i < 360.0D; i += 1.0D / detail) {
                double x_ = MegaUtil.circleX(x, r, i);
                double z_ = MegaUtil.circleY(z, r, i);
                level.addParticle(particleOptions, z, z_, x_, 0, 0, 0);
            }
        } else {
            for (double i = 0.0D; i < 360.0D; i += 1.0D / detail) {
                double x_ = MegaUtil.circleX(x, r, i);
                double z_ = MegaUtil.circleY(z, r, i);
                level.addParticle(particleOptions, x_, z_, x_, 0, 0, 0);
            }
        }
    }


    public void spawnHorizontalCircle(double detail, double r) {
        for (double i = 0.0D; i < 360.0D; i += 1.0D / detail) {
            double x_ = MegaUtil.circleX(x, r, i);
            double z_ = MegaUtil.circleY(z, r, i);
            level.addParticle(particleOptions, x_, y, z_, 0, 0, 0);
        }
    }

    public void spawnHorizontalCircle(double detail, double r, Vec3 motion) {
        for (double i = 0.0D; i < 360.0D; i += 1.0D / detail) {
            double x_ = MegaUtil.circleX(x, r, i);
            double z_ = MegaUtil.circleY(z, r, i);
            level.addParticle(particleOptions, x_, y, z_, motion.x, motion.y, motion.z);
        }
    }

    public void spawnLine(Vec3 start, Vec3 end) {
        spawnLine(start, end, 1, new Vec2d(0, 0));
    }

    public void spawnLine(Vec3 start, Vec3 end, double l) {
        spawnLine(start, end, l, new Vec2d(0, 0));
    }

    public void spawnLine(Vec3 start, Vec3 end, double l, Vec2d range) {
        double x1 = start.x;
        double y1 = start.y;
        double z1 = start.z;
        double x2 = end.x;
        double y2 = end.y;
        double z2 = end.z;
        int times = (int) (MathUtil.distance(start, end)) * 10;
        double xLength = (x2 - x1) / times * l;
        double yLength = (y2 - y1) / times * l;
        double zLength = (z2 - z1) / times * l;
        double x = x1;
        double y = y1;
        double z = z1;
        for (int i = 0; i < times; i++) {

            x += xLength;
            y += yLength;
            z += zLength;
            level.addParticle(particleOptions, x, y, z, MathUtil.range(range), MathUtil.range(range), MathUtil.range(range));
        }
    }
}
