package rune.magic.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Random;

public class RotationUtil {
    public static void rotateEntityToTargetProjectile(Entity entity, Entity target) {
        double mX = entity.getDeltaMovement().x();
        double mY = entity.getDeltaMovement().y();
        double mZ = entity.getDeltaMovement().z();
        float p_37270_ = 30.0F;
        float p_37269_ = 1.0F;
        Vec3 arrowLoc = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 targetLoc = new Vec3(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ());
        Vec3 lookVec = targetLoc.subtract(arrowLoc);
        Vec3 arrowMotion = new Vec3(mX, mY, mZ);
        double theta = wrap180Radian(angleBetween(arrowMotion, lookVec));
        theta = clampAbs(theta, Math.PI / 2);
        Vec3 crossProduct = arrowMotion.cross(lookVec).normalize();
        Vec3 adjustedLookVec = transform(crossProduct, theta, arrowMotion);
        Random random = new Random();
        Vec3 vec3 = (new Vec3(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z)).normalize().add(random.nextGaussian() * (double) 0.0075F * (double) p_37270_, random.nextGaussian() * (double) 0.0075F * (double) p_37270_, random.nextGaussian() * (double) 0.0075F * (double) p_37270_).scale(p_37269_);
        double d0 = vec3.horizontalDistance();
        entity.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        entity.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
    }

    public static void rotationAtoB(Entity a, Entity b) {
        getYRotD(a, b.getX(), b.getZ()).ifPresent((p_181130_) -> {
            a.setYHeadRot(rotateTowards(a.getYHeadRot(), p_181130_, 180));
            a.setYBodyRot(a.getYHeadRot());
        });
        getXRotD(a, b.getX(), b.getEyeY(), b.getZ()).ifPresent((p_181128_) -> {
            a.setXRot(rotateTowards(a.getXRot(), p_181128_, 180));
        });
    }

    public static Optional<Float> getXRotD(Entity e, double wantedX, double wantedY, double wantedZ) {
        double d0 = wantedX - e.getX();
        double d1 = wantedY - e.getEyeY();
        double d2 = wantedZ - e.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > (double) 1.0E-5F) && !(Math.abs(d3) > (double) 1.0E-5F) ? Optional.empty() : Optional.of((float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
    }

    public static Optional<Float> getYRotD(Entity e, double wantedX, double wantedZ) {
        double d0 = wantedX - e.getX();
        double d1 = wantedZ - e.getZ();
        return !(Math.abs(d1) > (double) 1.0E-5F) && !(Math.abs(d0) > (double) 1.0E-5F) ? Optional.empty() : Optional.of((float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F);
    }

    public static float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -p_24959_, p_24959_);
        return p_24957_ + f1;
    }

    public static Vec3 transform(Vec3 axis, double angle, Vec3 normal) {
        //Trimmed down math of javax vecmath calculations, potentially should be rewritten at some point
        double m00 = 1;
        double m01 = 0;
        double m02 = 0;

        double m10 = 0;
        double m11 = 1;
        double m12 = 0;

        double m20 = 0;
        double m21 = 0;
        double m22 = 1;
        double mag = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        if (mag >= 1.0E-10) {
            mag = 1.0 / mag;
            double ax = axis.x * mag;
            double ay = axis.y * mag;
            double az = axis.z * mag;

            double sinTheta = Math.sin(angle);
            double cosTheta = Math.cos(angle);
            double t = 1.0 - cosTheta;

            double xz = ax * az;
            double xy = ax * ay;
            double yz = ay * az;

            m00 = t * ax * ax + cosTheta;
            m01 = t * xy - sinTheta * az;
            m02 = t * xz + sinTheta * ay;

            m10 = t * xy + sinTheta * az;
            m11 = t * ay * ay + cosTheta;
            m12 = t * yz - sinTheta * ax;

            m20 = t * xz - sinTheta * ay;
            m21 = t * yz + sinTheta * ax;
            m22 = t * az * az + cosTheta;
        }
        return new Vec3(m00 * normal.x + m01 * normal.y + m02 * normal.z,
                m10 * normal.x + m11 * normal.y + m12 * normal.z,
                m20 * normal.x + m21 * normal.y + m22 * normal.z);
    }

    public static double angleBetween(Vec3 v1, Vec3 v2) {
        double vDot = v1.dot(v2) / (v1.length() * v2.length());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return Math.acos(vDot);
    }

    public static double wrap180Radian(double radian) {
        radian %= 2 * Math.PI;
        while (radian >= Math.PI) {
            radian -= 2 * Math.PI;
        }
        while (radian < -Math.PI) {
            radian += 2 * Math.PI;
        }
        return radian;
    }

    public static double clampAbs(double param, double maxMagnitude) {
        if (Math.abs(param) > maxMagnitude) {
            //System.out.println("CLAMPED");
            if (param < 0) {
                param = -Math.abs(maxMagnitude);
            } else {
                param = Math.abs(maxMagnitude);
            }
        }
        return param;
    }
}
