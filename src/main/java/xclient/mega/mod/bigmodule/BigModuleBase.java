package xclient.mega.mod.bigmodule;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraftforge.common.MinecraftForge;
import xclient.mega.Main;
import xclient.mega.screen.XScreen;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.RendererUtils;
import xclient.mega.utils.TimeHelper;
import xclient.mega.utils.Vec2d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BigModuleBase implements ActionBm {
    public static List<BigModuleBase> every = new ArrayList<>();
    public boolean asModule = true;
    public boolean isPressing;
    public int width_asModule;
    public int height_asModule;
    public Color color;
    public String name;
    public Vec2d pos;
    public TimeHelper timeHelper = new TimeHelper(20, 150);

    public BigModuleBase() {
        this("");
    }

    public BigModuleBase(String n) {
        name = n;
        every.add(this);
        pos = new Vec2d(0, every.size() * 10 + 5);
    }

    public static void clear() {
        every.forEach(bm -> MinecraftForge.EVENT_BUS.unregister(bm.timeHelper));
        every.clear();
    }

    public void setPos(int x, int y) {
        this.pos = new Vec2d(x, y);
    }

    public Vec2d getPos() {
        return pos;
    }

    public void release(int mx, int my) {
        isPressing = false;
    }

    public void startPress(int mx, int my) {
        isPressing = true;
    }

    public boolean isInRange(int mx, int my, Vec2d start, Vec2d end) {
        int sx = start.x;
        int sy = start.y;
        int ex = end.x;
        int ey = end.y;
        return mx >= sx && my >= sy && mx < ex && my < ey;
    }

    public boolean isInRange_asModule() {
        int sx = pos.x;
        int sy = pos.y;
        int ex = sx + width_asModule;
        int ey = sy + height_asModule;
        return asModule && XScreen.mouseX >= sx && XScreen.mouseY >= sy && XScreen.mouseX < ex && XScreen.mouseY < ey;
    }

    public BigModuleBase setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getName() {
        return name;
    }

    public BigModuleBase setName(String name) {
        this.name = name;
        return this;
    }

    public void renderAsModule(PoseStack stack, int x, int y, Font font, boolean isMouseOver) {
        int color = new Color(55, 71, 90, 150).getRGB();
        int color2 = new Color(0, 0, 0, Main.instance != null ? Main.base_timehelper.integer_time : 150).getRGB();
        pos = new Vec2d(x, y);
        Render2DUtil.drawRect(stack, x, y, (int) (width_asModule * 1.5F), height_asModule + 1, isMouseOver ? (this.color != null ? this.color.getRGB() : color2) : color);
        font.drawShadow(stack, getName(), x, y, RendererUtils.WHITE);
        if (width_asModule == 0)
            width_asModule = font.width(getName());
        if (height_asModule == 0)
            height_asModule = font.lineHeight;
    }

    public void render(PoseStack stack, int x, int y) {
        update(this);
    }
}
