package xclient.mega.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xclient.mega.BmMain;
import xclient.mega.mod.IScreenClick;
import xclient.mega.Main;
import xclient.mega.MegaUtil;
import xclient.mega.mod.Module;
import xclient.mega.mod.bigmodule.ActionBmC;
import xclient.mega.mod.bigmodule.BigModuleBase;
import xclient.mega.mod.message.Message;
import xclient.mega.utils.ButtonUtils;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.Vec2d;

import java.awt.*;

@Mod.EventBusSubscriber
public class XScreen extends Screen implements IScreenClick {

    public static int mouseX;
    public static int mouseY;
    public static boolean z;
    public boolean isRenderingConfig;
    public Module<?> configModule;
    public static Module<?> EMPTY;

    public XScreen() {
        super(new TextComponent(""));
        EMPTY = new Module<>("", null, false, null){
            public void left() {
                if (left != null) {
                    left.run(this);
                } else System.out.println(getName() + " left module is NULL!");
            }

            public void right() {
                if (right != null) {
                    right.run(this);
                } else System.out.println(getName() + " right module is NULL!");
            }
        }.unaddToList().setFather(null);
        Module.every.remove(EMPTY);
    }

    public static boolean isInRange(Module<?> module, int x, int y) {
        return x >= module.x && y >= module.y && x < module.x + module.width && y < module.y + module.height;
    }

    @SubscribeEvent
    public static void loggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        MegaUtil.writeXCLIENT();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_94697_) {
        click(x, y, p_94697_);
        MegaUtil.writeXCLIENT();
        return super.mouseClicked(x, y, p_94697_);
    }

    @Override
    public void init(Minecraft p_96607_, int p_96608_, int p_96609_) {
        super.init(p_96607_, p_96608_, p_96609_);
    }

    public void click(double x, double y, int code) {
        x = mouseX;
        y = mouseY;
        if (isRenderingConfig && configModule != null) {
            for (Module<?> child : configModule.children) {
                for (Module<?> module : Module.every) {
                    if (module.getName().equals(child.getName())) {
                        if (XScreen.isInRange(child, (int) x, (int) y)) {
                            if (code == 0) {
                                child.left();
                                EMPTY.left();
                                for (Module<?> mm : Module.every) {
                                    if (mm.sameModule(configModule))
                                        configModule = mm;
                                }
                            }
                            if (code == 1) {
                                child.right();
                                EMPTY.right();
                                for (Module<?> mm : Module.every) {
                                    if (mm.sameModule(configModule))
                                        configModule = mm;
                                }
                            }
                        }
                    }
                }
            }
        } else
            for (BigModuleBase bm : BmMain.CREATED) {
                if (bm.isInRange_asModule())
                    bm.click(code);
                for (Module<?> module : Module.every) {
                    if (module.getFather_Bm() instanceof ActionBmC actionBmC && actionBmC.pushed && module.getFather_Bm().getName().equals(bm.getName()))
                        if (XScreen.isInRange(module, (int) x, (int) y)) {
                            if (code == 0) {
                                module.left();
                            }
                            if (code == 1) {
                                if (module.hasChildren) {
                                    isRenderingConfig = true;
                                    configModule = module;
                                }
                                module.right();
                            }
                        }
                }
            }
        Main.setModules();
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        for (BigModuleBase bm : BigModuleBase.every) {
            if (bm.isInRange_asModule())
                bm.release((int) p_94722_, (int) p_94723_);
        }
        Main.KEY_DISPLAY_BM.release((int) p_94722_, (int) p_94723_);
        z = false;
        return super.mouseReleased(p_94722_, p_94723_, p_94724_);
    }

    @Override
    public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
        if (!z && Main.KEY_DISPLAY_BM.isInRange((int) p_94699_, (int) p_94700_, new Vec2d(Main._x_, Main._y_), new Vec2d(Main._x_ + 63, Main._y_ + 61))) {
            Main.KEY_DISPLAY_BM.startPress((int) p_94699_, (int) p_94700_);
            z = true;
        }
        for (BigModuleBase bm : BigModuleBase.every) {
            if (bm.isInRange_asModule() || bm.isPressing) {
                if (!z) {
                    bm.isPressing = true;
                    z = true;
                }
                bm.update(bm);
            }
        }
        return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
    }

    @Override
    public void render(PoseStack stack, int mx, int my, float pt) {
        mouseX = mx;
        mouseY = my;
        if (!isRenderingConfig) {
            Main.CLIENT.render(stack, 0, 0, isInRange(Main.CLIENT, mx, my));
            for (BigModuleBase bm : BigModuleBase.every) {
                if (bm.asModule) {
                    bm.render(stack, bm.pos.x, bm.pos.y);
                }
            }
        } else {
            minecraft = Minecraft.getInstance();
            drawCenteredString(stack, RainbowFont.INS, configModule.getName() + " Config", minecraft.getWindow().getGuiScaledWidth() / 2 - 5, 0, 0xFFFFFFFF);
            int x = 10, y = 10;
            Render2DUtil.drawRect(stack, x, y - 3, 200, 120, new Color(0, 0, 0, 60).getRGB());
            x += 8;
            for (Module<?> module : Module.every) {
                for (Module<?> m : configModule.children) {
                    if (m.getName().equals(module.getName())) {
                        m.render(stack, x, y, XScreen.isInRange(m, mx, my));
                        y += 11;
                        if (y >= Minecraft.getInstance().getWindow().getScreenHeight() - 50)
                            x += 130;
                    }
                }
            }
            if (configModule.sameModule(Main.SUPER_KILL_AURA))
                minecraft.font.drawShadow(stack, I18n.get("killaura_info"), x, y+100, Color.DARK_GRAY.getRGB());
        }
        super.render(stack, mx, my, pt);
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        Main.key_scale += p_94688_ / 20F;
        MegaUtil.writeXCLIENT();
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public void renderBackground(PoseStack p_96557_) {
        super.renderBackground(p_96557_);
    }

    @Override
    public Component getTitle() {
        return new TextComponent("The X Screen").withStyle(ChatFormatting.BLUE);
    }
    @Override
    public void init() {
        MegaUtil.read();
        minecraft = Minecraft.getInstance();
        Main.setModules();
        super.init();
    }

    @Override
    public void onClose() {
        if (isRenderingConfig) {
            isRenderingConfig = false;
            configModule = null;
            return;
        }
        super.onClose();
    }
}
