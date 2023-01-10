package xclient.mega.mixin;

import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface IButton {
    @Accessor("onPress")
    @Mutable
    void setOnPress(Button.OnPress press);
}
