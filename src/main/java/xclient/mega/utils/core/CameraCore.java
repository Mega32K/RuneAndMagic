package xclient.mega.utils.core;

import net.minecraft.world.phys.Vec3;

public class CameraCore {
    public static Vec3 POS = new Vec3(0 ,0 ,0);

    public static Vec3 add(double d1, double d2, double d3) {
        return add(new Vec3(d1, d2, d3));
    }

    public static Vec3 add(Vec3 v) {
        POS = new Vec3(POS.x + v.x, POS.y + v.y, POS.z + v.z);
        return POS;
    }
}
