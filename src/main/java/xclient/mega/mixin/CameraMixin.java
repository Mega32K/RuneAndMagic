package xclient.mega.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xclient.mega.utils.CameraCore;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private Vec3 position;

    /**
     * @author mega
     */
    @Overwrite
    public Vec3 getPosition() {
        return this.position.add(CameraCore.POS);
    }
}
