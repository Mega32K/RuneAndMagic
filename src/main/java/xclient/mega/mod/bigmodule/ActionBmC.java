package xclient.mega.mod.bigmodule;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import xclient.mega.Main;
import xclient.mega.mod.Module;
import xclient.mega.screen.XScreen;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.RendererUtils;
import xclient.mega.utils.Vec2d;

import java.awt.*;

public abstract class ActionBmC extends BigModuleBase {
    public boolean pushed = false;

    @Override
    public void click(int code) {
        if (code == 0)
            pushed = !pushed;
    }

    public void renderAsModule(PoseStack stack, int x, int y, Font font, boolean isMouseOver) {
        int color = new Color(55, 71, 90, 150).getRGB();
        int color2 = new Color(0, 0, 0, Main.instance != null ? Main.base_timehelper.integer_time : 150).getRGB();
        pos = new Vec2d(x, y);
        Render2DUtil.drawRect(stack, x, y, (int) (width_asModule * 1.5F), height_asModule + 1, pushed ? (this.color != null ? this.color.getRGB() : color2) : color);
        font.drawShadow(stack, getName(), x, y, RendererUtils.WHITE);
        if (width_asModule == 0)
            width_asModule = font.width(getName());
        if (height_asModule == 0)
            height_asModule = font.lineHeight;
    }

    @Override
    public void render(PoseStack stack, int x, int y) {
        renderAsModule(stack, x, y, RainbowFont.INS, isInRange_asModule());
        int y_ = y;
        y_ += 11;
        if (pushed) {
            for (Module<?> module : Module.every) {
                if (module.sameBm(this) && module.getFather() == null) {
                    module.render(stack, x, y_, XScreen.isInRange(module, XScreen.mouseX, XScreen.mouseY));
                    y_ += 11;
                    if (y > Minecraft.getInstance().getWindow().getScreenHeight() - 50)
                        x += 130;
                }
            }
        }
    }
}
