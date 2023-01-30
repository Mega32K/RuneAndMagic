package xclient.mega.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xclient.mega.Config;
import xclient.mega.button.ModuleButton;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.Textures;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow
    @Final
    @Mutable
    public static Component COPYRIGHT_TEXT;
    @Shadow
    @Final
    private static ResourceLocation ACCESSIBILITY_TEXTURE;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Nullable
    private String splash;
    @Shadow
    private NotificationModUpdateScreen modUpdateNotification;
    @Shadow
    private Screen realmsNotificationsScreen;
    @Shadow
    private Button resetDemoButton;

    protected TitleScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Shadow
    protected abstract boolean realmsNotificationsEnabled();

    @Shadow
    protected abstract boolean checkDemoWorldPresence();

    @Shadow
    protected abstract void confirmDemo(boolean p_96778_);

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        Textures.background = Config.background.get();
        addRenderableWidget(new ModuleButton(this.width - 20, this.height - 20, 20, 20, new TextComponent("Q"), (b) -> {
            if (Textures.background < 3)
                Textures.background++;
            else if (Textures.background == 3)
                Textures.background = 1;
            Config.background.set(Textures.background);
            Config.COMMON_CONFIG.save();
        }));
    }

    /**
     * @author meha
     * @reason x-client
     */
    @Overwrite
    private void createNormalMenuOptions(int p_96764_, int p_96765_) {
        this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, p_96764_, 98, 20, new TranslatableComponent("menu.singleplayer"), (p_96776_) -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));
        boolean flag = this.minecraft.allowsMultiplayer();
        Button.OnTooltip button$ontooltip = flag ? Button.NO_TOOLTIP : new Button.OnTooltip() {
            private final Component text = new TranslatableComponent("title.multiplayer.disabled");

            public void onTooltip(Button p_169458_, PoseStack p_169459_, int p_169460_, int p_169461_) {
                if (!p_169458_.active) {
                    ((TitleScreen) (Object) this).renderTooltip(p_169459_, minecraft.font.split(this.text, Math.max(width / 2 - 43, 170)), p_169460_, p_169461_);
                }

            }

            public void narrateTooltip(Consumer<Component> p_169456_) {
                p_169456_.accept(this.text);
            }
        };
        (this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, p_96764_ + p_96765_ * 1, 98, 20, new TranslatableComponent("menu.multiplayer"), (p_210872_) -> {
            Screen screen = this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
            this.minecraft.setScreen(screen);
        }, button$ontooltip))).active = flag;

    }

    /**
     * @author meha
     * @reason x-client
     */
    @Overwrite
    private void createDemoMenuOptions(int p_96773_, int p_96774_) {
        boolean flag = this.checkDemoWorldPresence();
        this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, p_96773_, 98, 20, new TranslatableComponent("menu.playdemo"), (p_211786_) -> {
            if (flag) {
                this.minecraft.loadLevel("Demo_World");
            } else {
                RegistryAccess registryaccess = RegistryAccess.BUILTIN.get();
                this.minecraft.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, registryaccess, WorldGenSettings.demoSettings(registryaccess));
            }

        }));
        this.resetDemoButton = this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, p_96773_ + p_96774_ * 1, 98, 20, new TranslatableComponent("menu.resetdemo"), (p_211783_) -> {
            LevelStorageSource levelstoragesource = this.minecraft.getLevelSource();

            try {
                LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = levelstoragesource.createAccess("Demo_World");

                try {
                    LevelSummary levelsummary = levelstoragesource$levelstorageaccess.getSummary();
                    if (levelsummary != null) {
                        this.minecraft.setScreen(new ConfirmScreen(this::confirmDemo, new TranslatableComponent("selectWorld.deleteQuestion"), new TranslatableComponent("selectWorld.deleteWarning", levelsummary.getLevelName()), new TranslatableComponent("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
                    }
                } catch (Throwable throwable1) {
                    if (levelstoragesource$levelstorageaccess != null) {
                        try {
                            levelstoragesource$levelstorageaccess.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                    }

                    throw throwable1;
                }

                if (levelstoragesource$levelstorageaccess != null) {
                    levelstoragesource$levelstorageaccess.close();
                }
            } catch (IOException ioexception) {
                SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
                LOGGER.warn("Failed to access demo world", ioexception);
            }

        }));
        this.resetDemoButton.active = flag;
    }

    /**
     * @author meha
     * @reason x-client
     */
    @Overwrite
    public void init() {
        COPYRIGHT_TEXT = new TextComponent("MegaDarkness's website,click here");
        int i = RainbowFont.INS.width(COPYRIGHT_TEXT);
        int j = this.width - i + 30;
        int l = this.height / 4 + 30;
        ModuleButton modButton = null;
        if (this.minecraft.isDemo()) {
            this.createDemoMenuOptions(l, 24);
        } else {
            this.createNormalMenuOptions(l, 24);
            modButton = this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, l + 24 * 2, 98, 20, new TranslatableComponent("fml.menu.mods"), button -> {
                this.minecraft.setScreen(new net.minecraftforge.client.gui.ModListScreen(this));
            }));
        }
        modUpdateNotification = net.minecraftforge.client.gui.NotificationModUpdateScreen.init((TitleScreen) (Object) this, modButton);

        this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, l + 48 + 25, 98, 20, new TranslatableComponent("menu.options"), (p_96788_) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));
        this.addRenderableWidget(new ModuleButton(this.width / 2 - 100, l + 48 + 50, 98, 20, new TranslatableComponent("menu.quit"), (p_96786_) -> {
            this.minecraft.stop();
        }));
        this.addRenderableWidget(new PlainTextButton(j, this.height - 10, i, 10, COPYRIGHT_TEXT, (p_211790_) -> {
            minecraft.doRunTask(() -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://space.bilibili.com/456398523"));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        }, RainbowFont.INS));
        this.minecraft.setConnectedToRealms(true);
        if (this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen == null) {
            this.realmsNotificationsScreen = new RealmsNotificationsScreen();
        }

        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.init(this.minecraft, this.width, this.height);
        }
    }


    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void render(PoseStack p_96739_, int p_96740_, int p_96741_, float p_96742_, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation("x_client:textures/background" + Textures.background + ".png"));
        minecraft = Minecraft.getInstance();
        blit(p_96739_, -p_96740_, (int) (-p_96741_ / 1.5F), 0, 0, width * 2, height * 2, width * 2, height * 2);
        for (Widget widget : this.renderables) {
            widget.render(p_96739_, p_96740_, p_96741_, p_96742_);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        p_96739_.pushPose();
        p_96739_.scale(0.4F, 0.4F, 0.4F);
        Render2DUtil.drawImage(p_96739_, Textures.LOGO, this.width / 3 + 110, this.height / 4 - 10, (int) (width * 1.2F), height / 2);
        p_96739_.popPose();
        ci.cancel();
    }

    public void init(Minecraft mc, int x, int y) {
        super.init(mc, x, y);
    }
}
