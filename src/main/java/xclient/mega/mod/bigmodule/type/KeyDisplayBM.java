package xclient.mega.mod.bigmodule.type;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.Main;
import xclient.mega.event.Render2DEvent;
import xclient.mega.mod.bigmodule.ActionBm;
import xclient.mega.mod.bigmodule.BigModuleBase;
import xclient.mega.utils.MouseHandler;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.TimeHelper;

import java.awt.*;

@Mod.EventBusSubscriber
public class KeyDisplayBM extends BigModuleBase implements ActionBm {
    public static TimeHelper L_SECONDS;
    public static TimeHelper R_SECONDS;
    public static int leftClicks;
    public static int rightClicks;

    public KeyDisplayBM() {
        super();
        asModule = false;
    }

    @SubscribeEvent
    public static void renderHud(Render2DEvent event) {
        if (L_SECONDS == null)
            L_SECONDS = new TimeHelper(0, 0);
        if (R_SECONDS == null)
            R_SECONDS = new TimeHelper(0, 0);
        if (L_SECONDS.aSecond()) {
            leftClicks = 0;
        }
        if (R_SECONDS.aSecond()) {
            rightClicks = 0;
        }
        PoseStack stack = new PoseStack();
        stack.scale(Main.key_scale, Main.key_scale, Main.key_scale);
        Main.KEY_DISPLAY_BM.render(stack, Main._x_, Main._y_);
        Minecraft.getInstance().getWindow().setTitle("");
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseInputEvent event) {
        if (Minecraft.getInstance().screen == null) {
            if (event.getButton() == 0) {
                leftClicks++;
                L_SECONDS.integer_time = 0;
            }
            if (event.getButton() == 1)
                rightClicks++;
            R_SECONDS.integer_time = 0;
        }
    }

    @Override
    public void render(PoseStack stack, int x, int y) {
        super.render(stack, x, y);
        if (Main.key_display) {
            Minecraft mc = Minecraft.getInstance();
            int background = new Color(255, 255, 255, 50).getRGB();
            int color = new Color(0, 0, 0, Main.base_timehelper.integer_time).getRGB();
            int color2 = new Color(Main.timeHelper.integer_time / 2, Main.base_timehelper.integer_time / 3, 130, Main.base_timehelper.integer_time).getRGB();
            Options options = mc.options;
            Render2DUtil.drawRectWithString(stack, x + 1 + 20, y, 20, 20, options.keyUp.isDown() ? color : background, " W", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x + 20 + 1, y + 20 + 1, 20, 20, options.keyDown.isDown() ? color : background, " S", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x, y + 20 + 1, 20, 20, options.keyLeft.isDown() ? color : background, " A", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x + 40 + 2, y + 20 + 1, 20, 20, options.keyRight.isDown() ? color : background, " D", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x, y + 40 + 2 + 2, 62, 15, options.keyJump.isDown() ? color : background, "    [ _ ] ", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x, y + 40 + 2 + 2 + 1 + 15, 30, 15, mc.options.keySprint.isDown() ? color : background, "Ctrl", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x + 30 + 1, y + 40 + 2 + 2 + 1 + 15, 31, 15, options.keyShift.isDown() ? color : background, "Shift", RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x + 2 + 62, y, 20, 60, color2, "Fps", RainbowFont.INS);
            Gui.drawCenteredString(stack, RainbowFont.INS, "  " + Minecraft.fps, x + 4 + 62, y + 35 + 30 / 3 + 1, 0);
            Render2DUtil.drawRectWithString(stack, x, y + 40 + 2 + 2 + 1 + 15 + 1 + 15, 62, 20, new Color(0, 0, 0, Math.min(MouseHandler.getLeft().getCPS() * 10, 255)).getRGB(), "LMB:" + MouseHandler.getLeft().getCPS(), RainbowFont.INS);
            Render2DUtil.drawRectWithString(stack, x, y + 40 + 2 + 2 + 1 + 15 + 1 + 15 + 21, 62, 20, new Color(0, 0, 0, Math.min(MouseHandler.getRight().getCPS() * 10, 255)).getRGB(), "RMB:" + MouseHandler.getRight().getCPS(), RainbowFont.INS);
        }
    }

    @Override
    public void click(int code) {
    }
}
