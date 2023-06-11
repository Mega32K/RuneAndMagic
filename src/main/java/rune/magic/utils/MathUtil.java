package rune.magic.utils;

import net.minecraft.world.phys.Vec3;

public class MathUtil {
    public static double distance(Vec3 vec3, Vec3 vec32) {
        return Math.sqrt(square(vec3.x - vec32.x) + square(vec3.y - vec32.y) + square(vec3.z - vec32.z));
    }

    public static double square(double d) {
        return d * d;
    }

    public static double range(Vec2d range) {
        return Math.random() * range.x + range.y;
    }
}