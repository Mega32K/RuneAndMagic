package xclient.mega.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.mod.IScreenClick;
import xclient.mega.Main;
import xclient.mega.event.Render2DEvent;
import xclient.mega.mod.Module;
import xclient.mega.mod.bigmodule.type.RenderBm;
import xclient.mega.utils.MegaUtil;
import xclient.mega.utils.RainbowFont;

@Mod.EventBusSubscriber
public class YScreen extends Screen implements IScreenClick {
    public static boolean display_players;
    public static Module<?> RETURN_LOCAL;

    public YScreen() {
        super(new TextComponent("Y Screen"));
    }

    public static void draw(PoseStack stack, Font font, String s, int x, int y, int color) {
        drawString(stack, font, s, x, y, color);
    }

    public static String pos(Vec3 vec3) {
        String x = String.format("%.2f", vec3.x);
        String y = String.format("%.2f", vec3.y);
        String z = String.format("%.2f", vec3.z);
        return "(" + x + "," + y + "," + z + ")";
    }

    @SubscribeEvent
    public static void renderPlayerInfo(Render2DEvent event) {
        if (display_players && !(Minecraft.getInstance().screen instanceof IScreenClick)) {
            Minecraft mc = Minecraft.getInstance();
            PoseStack stack = new PoseStack();
            Font font = RainbowFont.INS;
            if (display_players && mc.cameraEntity == null) {
                mc.cameraEntity = MegaUtil.randomFrom(RenderBm.players.toArray(new Player[0]));
            }
            if (mc.cameraEntity instanceof Player player) {
                draw(stack, font, "Player:" + player.getName().getString(), 0, 0, 0);
                draw(stack, mc.font, "Armor" + player.getAttribute(Attributes.ARMOR).getBaseValue(), 0, 9, 0xFFFFFFFF);
                draw(stack, mc.font, "Health" + player.getHealth() + "/" + player.getMaxHealth(), 0, 18, 0xFFFFFFFF);
                draw(stack, mc.font, "Pos" + pos(player.position()), 0, 27, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
        minecraft = Minecraft.getInstance();
        Main.setModules();
        addRenderableWidget(new Button(10, 190, 80, 20, new TextComponent("Player Cameras"), (b) -> display_players = !display_players));
        if (minecraft.level != null) {
            Main.setPlayerCamera();
        }
        super.init();
    }

    @Override
    public void render(PoseStack stack, int mx, int my, float pt) {
        if (display_players) {
            int x = 0, y = 0;
            for (Module<Component> module : Main.PLAYER_CAMERA) {
                if (module.getFather() == null)
                    module.render(stack, x, y, XScreen.isInRange(module, mx, my));
                y += 11;
                if (y >= 110) {
                    x += 130;
                    y = 0;
                }
            }
            RETURN_LOCAL.render(stack, width - RETURN_LOCAL.width, 0, XScreen.isInRange(RETURN_LOCAL, mx, my));
        }
        super.render(stack, mx, my, pt);
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_94697_) {
        for (Module<?> m : Main.PLAYER_CAMERA) {
            if (XScreen.isInRange(m, (int) x, (int) y))
                m.left();
        }
        return super.mouseClicked(x, y, p_94697_);
    }
}
