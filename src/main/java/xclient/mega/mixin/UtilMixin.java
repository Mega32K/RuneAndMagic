package xclient.mega.mixin;

import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xclient.mega.Main;

@Mixin(Util.class)
public class UtilMixin {
    /**
     * @author mega
     * @reason change the add speed
     */
    @Overwrite
    public static long getMillis() {
        return Main.millis;
    }
}
