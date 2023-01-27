package xclient.mega.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xclient.mega.Main;
import xclient.mega.utils.Render2DUtil;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {
    @Shadow
    @Final
    private StoringChunkProgressListener progressListener;

    protected LevelLoadingScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Shadow
    protected abstract String getFormattedProgress();

    /**
     * @author mega
     * @reason ?
     */
    @Overwrite
    public void render(PoseStack p_96145_, int p_96146_, int p_96147_, float p_96148_) {
        int j = this.width / 2;
        int k = this.height / 2 - 30;
        int i = this.minecraft.getWindow().getGuiScaledWidth();
        double d1 = Math.min((double) this.minecraft.getWindow().getGuiScaledWidth() * 0.75D, this.minecraft.getWindow().getGuiScaledHeight()) * 0.25D;
        double d0 = d1 * 4.0D;
        int j1 = (int) (d0 * 0.5D);
        Render2DUtil.renderBackground();
        int k1 = (int) ((double) this.minecraft.getWindow().getGuiScaledHeight() * 0.8325D);
        Render2DUtil.drawProgressBar(p_96145_, i / 2 - j1, k1 - 5, i / 2 + j1, k1 + 5, Mth.clamp(this.progressListener.getProgress(), 0, 100) / 100F, Main.base_timehelper.integer_time / 255F);
        drawCenteredString(p_96145_, this.font, this.getFormattedProgress(), j, k - 9 / 2 - 30, 16777215);
    }
}
