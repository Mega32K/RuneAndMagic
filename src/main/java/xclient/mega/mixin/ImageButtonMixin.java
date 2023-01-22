package xclient.mega.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Main;
import xclient.mega.utils.Render2DUtil;

import java.awt.*;

@Mixin(ImageButton.class)
public abstract class ImageButtonMixin extends Button {
    @Shadow @Final private ResourceLocation resourceLocation;

    public ImageButtonMixin(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
    }

    @Inject(method = "renderButton", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack stack, int x, int y, float pt, CallbackInfo c) {
        if (resourceLocation.getNamespace().equals("ias")) {
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int base = new Color(60, 60, 60, 150).getRGB();
            int pressing = new Color(0, 0, 0, Main.base_timehelper.integer_time).getRGB();
            Render2DUtil.drawRect(stack, this.x, this.y, this.width, this.height, Render2DUtil.isHovered(x, y, this.x, this.y, this.width, this.height) ? pressing : base);
            this.renderBg(stack, minecraft, x, y);
            int j = getFGColor();
            drawCenteredString(stack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
            if (this.isHoveredOrFocused()) {
                this.renderToolTip(stack, x, y);
            }
            c.cancel();
        }
    }
}
