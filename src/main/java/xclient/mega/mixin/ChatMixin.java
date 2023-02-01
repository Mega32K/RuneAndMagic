package xclient.mega.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;

@Mixin(ChatComponent.class)
public class ChatMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;I)V", at = @At("HEAD"), cancellable = true)
    public void add(Component p_93788_, int p_93789_, CallbackInfo ci) {
        String text = p_93788_.getString().replace("<"+minecraft.player.getDisplayName().getString()+">","").replace(" ", "");
        System.out.println(text);
        if (text.startsWith("-killaura_target ")) {
            String info = text.replace("-killaura_target ", "");
            System.out.println(info);
            ci.cancel();
        }
    }
}
