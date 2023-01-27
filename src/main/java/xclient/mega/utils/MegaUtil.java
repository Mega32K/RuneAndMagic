package xclient.mega.utils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MegaUtil {

    public static Random random = new Random();

    public static List<Entity> getEntitiesToWatch(int level, Player player) {
        if (player == null)
            return null;
        List<Entity> entities = new ArrayList<>();
        for (int dis = 0; dis < level * 2; dis += 2) {
            AABB aabb = player.getBoundingBox().inflate(2, 2, 2);
            Vec3 vec = player.getLookAngle();
            vec = vec.normalize();
            aabb = aabb.move(vec.x * dis, vec.y * dis, vec.z * dis);
            List<Entity> list = player.level.getEntities(player, aabb);
            entities.addAll(list);
        }
        return entities;
    }

    public static List<Entity> getEntitiesToWatch_withoutScaled(int level, Player player) {
        if (player == null)
            return null;
        List<Entity> entities = new ArrayList<>();
        for (int dis = 0; dis < level * 2; dis += 2) {
            AABB aabb = player.getBoundingBox();
            aabb = new AABB(aabb.minX, aabb.minY + 1D, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
            Vec3 vec = player.getLookAngle();
            vec = vec.normalize();
            aabb = aabb.move(vec.x * dis, vec.y * dis, vec.z * dis);
            List<Entity> list = player.level.getEntities(player, aabb);
            entities.addAll(list);
        }
        return entities;
    }

    public static Entity getEntityToWatch(int level, Player player) {
        if (player == null)
            return null;
        Entity entity = null;
        for (int dis = 0; dis < level * 2; dis += 2) {
            AABB aabb = player.getBoundingBox();
            Vec3 vec = player.getLookAngle();
            vec = vec.normalize();
            aabb = aabb.move(vec.x * dis, vec.y * dis, vec.z * dis);
            List<Entity> list = player.level.getEntities(player, aabb);
            float distance = level;
            for (Entity curEntity : list) {
                float curDist = curEntity.distanceTo(player);
                if (curDist < distance) {
                    entity = curEntity;
                    distance = curDist;
                }
            }
            if (entity != null)
                break;
        }
        return entity;
    }

    public static <T> T randomFrom(T... ts) {
        return ts[random.nextInt(ts.length)];
    }

    public static void really_sendOpenInv(LocalPlayer player, Player target) {
        player.connection.send(new ServerboundPlayerCommandPacket(target, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
    }

    public static double circleX(double x, double r, double a) {
        return x + r * Math.cos(Math.toRadians(a));
    }

    public static double circleY(double y, double r, double a) {
        return y + r * Math.sin(Math.toRadians(a));
    }
}