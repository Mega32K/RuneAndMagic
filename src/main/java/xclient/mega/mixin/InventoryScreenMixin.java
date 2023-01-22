package xclient.mega.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xclient.mega.screen.YScreen;
import xclient.mega.utils.RainbowFont;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {

    @Shadow
    private float xMouse;

    @Shadow
    private float yMouse;

    public InventoryScreenMixin(InventoryMenu p_98701_, Inventory p_98702_, Component p_98703_) {
        super(p_98701_, p_98702_, p_98703_);
    }

    @Shadow
    public static void renderEntityInInventory(int p_98851_, int p_98852_, int p_98853_, float p_98854_, float p_98855_, LivingEntity p_98856_) {
    }

    /**
     * @author mega
     * @reason display camera Entity
     */
    @Overwrite
    protected void renderBg(PoseStack p_98870_, float p_98871_, int p_98872_, int p_98873_) {
        LivingEntity real = this.minecraft.player != minecraft.cameraEntity ? (minecraft.cameraEntity instanceof LivingEntity livingEntity ? livingEntity : minecraft.player) : minecraft.player;
        if (YScreen.display_players)
            drawString(p_98870_, RainbowFont.INS, real.getDisplayName().getString() + "\u7684\u80cc\u5305", 100, 15, 0xFFFFFFFF);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(p_98870_, i, j, 0, 0, this.imageWidth, this.imageHeight);
        renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, real);
    }
}
