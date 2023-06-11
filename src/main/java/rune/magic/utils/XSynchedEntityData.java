package rune.magic.utils;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class XSynchedEntityData extends SynchedEntityData {
    public XSynchedEntityData(Entity p_135351_, SynchedEntityData data) {
        super(p_135351_);
        for (Field f : SynchedEntityData.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (!Modifier.isStatic(f.getModifiers()) && !f.getType().equals(Integer.TYPE)) {
                try {
                    f.set(this, f.get(data));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
