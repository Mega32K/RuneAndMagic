package xclient.mega.mod.message;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xclient.mega.event.Render2DEvent;
import xclient.mega.mod.Module;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.TimeHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {
    public static List<Message> messages = new ArrayList<>();
    public final float MAX_EJECTING_TIME = 50F;
    public final float MAX_RENDERING_TIME = 300F;
    public final float RESUME_TIME = 50F;

    public Module<?> target;
    public Color color;

    public float ejectingTime = 0;
    public boolean ejecting = false;

    public float renderingTime = 0;
    public boolean rendering = false;

    public float resumeTime = 0;
    public boolean resume = false;
    protected Message(Module<?> target) {
        this.target = target;
        MinecraftForge.EVENT_BUS.register(this);
        messages.add(this);
    }

    public void eject(PoseStack stack) {
        ejecting = true;
        rendering = true;
        renderingTime = 0;
    }

    public void renderCore(PoseStack stack) {
        color = new Color(100, 100, 100, 200);
        Minecraft mc = Minecraft.getInstance();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        //int mouseX = (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth());
        //int mouseY = (int)(mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight());
        int width = 6 + target.font.width(target.getInfo())*2;
        int height = target.font.lineHeight*2 + 3;
        int y = screenHeight - (height + (messages.indexOf(this) == 1 ? 0 : ((messages.indexOf(this)-1)*(height+3))));
        int x = (int) (width*(ejectingTime/MAX_EJECTING_TIME)) - width;
        int line = (int) (width * (ejectingTime/MAX_EJECTING_TIME));
        if (renderingTime >= MAX_RENDERING_TIME-82) {
            x = (int) (width * (1-(resumeTime/RESUME_TIME))) - width;
        }
        Render2DUtil.drawRect(stack, x , y, width, height, color.getRGB());
        Render2DUtil.drawRect(stack, x , y+height-2, line, 2, Color.GREEN.getRGB());
        Minecraft.getInstance().font.drawShadow(stack, target.getInfo(), x+1, y, 0xFFFFFFFF);
    }

    public void renderMessage(PoseStack stack) {
        if (ejecting)
            if (ejectingTime < MAX_EJECTING_TIME)
                ejectingTime++;
        if (rendering || resume) {
            if (renderingTime < MAX_RENDERING_TIME)
                renderingTime++;
            renderCore(stack);
        }
        if (renderingTime >= MAX_RENDERING_TIME-20) {
            resume = true;
        }
        if (resume) {
            if (resumeTime < RESUME_TIME)
                resumeTime++;
            if (resumeTime == RESUME_TIME) {
                ejecting = rendering = resume = false;
                renderingTime = ejectingTime = resumeTime = 0;
                MinecraftForge.EVENT_BUS.unregister(this);
                messages.remove(this);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void renderSelf(Render2DEvent event) {
        renderMessage(new PoseStack());
    }

    public static Message sendMessage(Module<?> target) {
        Message message = new Message(target);
        message.eject(new PoseStack());
        return message;
    }
}
