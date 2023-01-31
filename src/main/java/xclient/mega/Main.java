package xclient.mega;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.glfw.GLFW;
import xclient.mega.event.Render2DEvent;
import xclient.mega.mod.Module;
import xclient.mega.mod.ModuleManager;
import xclient.mega.mod.ModuleValue;
import xclient.mega.mod.bigmodule.BigModuleBase;
import xclient.mega.mod.bigmodule.type.KeyDisplayBM;
import xclient.mega.screen.XScreen;
import xclient.mega.screen.YScreen;
import xclient.mega.utils.RainbowFont;
import xclient.mega.utils.RendererUtils;
import xclient.mega.utils.RotationUtil;
import xclient.mega.utils.TimeHelper;
import xclient.mega.utils.core.AutoFightCore;
import xclient.mega.utils.core.CameraCore;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod(Main.ID)
public class Main {
    public static final String ID = "x_client";
    public static String version = "V1.2.4";
    public static boolean hasRead;
    public static String YES = "\u221a";

    public static Saver<Integer> GAMMA;
    public static TimeHelper base_timehelper;

    public static boolean enable_display_info = false;
    public static int _x_ = 3, _y_ = 70;
    public static float key_scale = 1.0F;
    public static float zoom = 1.0F;

    @ModuleValue
    public static float killaura_range = 3.8F;
    @ModuleValue
    public static boolean killaura_attackPlayer = true;
    @ModuleValue
    public static boolean killaura_rotation = true;

    public static Set<Player> renderPlayers = new HashSet<>();
    @ModuleValue
    public static boolean auto_attack = false;
    @ModuleValue
    public static boolean enableHurtEffect = true;
    @ModuleValue
    public static boolean superKillAura = false;
    @ModuleValue
    public static float reach_distance = 0;
    @ModuleValue
    public static boolean no_fall;
    @ModuleValue
    public static boolean respawn;
    @ModuleValue
    public static boolean fly;
    @ModuleValue
    public static boolean sprint;
    @ModuleValue
    public static boolean renderPlayerOutline;
    @ModuleValue
    public static boolean full_bright;
    @ModuleValue
    public static boolean dner;
    @ModuleValue
    public static boolean quickly_place;
    @ModuleValue
    public static boolean key_display;
    @ModuleValue
    public static boolean quickly_bow;
    @ModuleValue
    public static boolean jumping;
    @ModuleValue
    public static boolean critical;
    @ModuleValue
    public static boolean air_jump;
    @ModuleValue
    public static boolean auto_release;
    @ModuleValue
    public static boolean arrow_dodge;
    @ModuleValue
    public static float air_jump_speed = 1;
    @ModuleValue
    public static float time_speed = 20.0F;
    @ModuleValue
    public static boolean enabledCameraGhost;
    @ModuleValue
    public static float cameraGhostSpeed = 1.0F;
    @ModuleValue
    public static boolean auto_fight = false;
    @ModuleValue
    public static boolean auto_fight_teleportation_tracking = false;
    @ModuleValue
    public static boolean auto_fight_normal_tracking = true;
    @ModuleValue
    public static boolean auto_fight_rotation = true;
    @ModuleValue
    public static boolean auto_fight_attack_delay = false;
    @ModuleValue
    public static boolean auto_fight_auto_shield = true;
    @ModuleValue
    public static float speed = 0.1F;

    public static double millisD = 0D;
    public static long millis = 0;
    public static ScheduledExecutorService service;

    public static Module<?> CLIENT;
    public static Module<Boolean> AUTO_ATTACK;
    public static Module<Boolean> ENABLE_HURT_EFFECT;
    public static Module<Boolean> SUPER_KILL_AURA;
    public static Module<Float> REACH;
    public static Module<Boolean> FLY;
    public static Module<Boolean> NO_FALL;
    public static Module<Boolean> RESPAWN;
    public static Module<Boolean> SPRINT;
    public static Module<Boolean> RENDER_OUTLINE;
    public static Module<Boolean> FULL_BRIGHT;
    public static Module<Boolean> DISABLE_NEGATIVE_EFFECT_RENDERER;
    public static Module<Boolean> QUICKLY_PLACE;
    public static Module<Boolean> KEY_DISPLAY;
    public static Module<Boolean> QUICKLY_BOW;
    public static Module<Boolean> JUMPING;
    public static Module<Boolean> CRITICAL;
    public static Module<Boolean> AUTO_RELEASE;
    public static Module<Boolean> ARROW_DODGE;
    public static Module<Float> TIMER;
    public static Module<Float> SPEED;

    public static Module<Boolean> AUTO_FIGHT;
    public static Module<Boolean> AUTO_FIGHT$TELEPORTATION_TRACKING;
    public static Module<Boolean> AUTO_FIGHT$NORMAL_TRACKING;
    public static Module<Boolean> AUTO_FIGHT$ROTATION;
    public static Module<Boolean> AUTO_FIGHT$ATTACK_DELAY;
    public static Module<Boolean> AUTO_FIGHT$AUTO_SHIELD;

    public static Module<Boolean> GHOST;
    public static Module<Float> GHOST$SPEED;

    public static Module<Float> SUPER_KILL_AURA$RANGE;
    public static Module<Boolean> SUPER_KILL_AURA$ATTACK_PLAYER;
    public static Module<Boolean> SUPER_KILL_AURA$ROTATION;

    public static Module<Boolean> AIR_JUMP;
    public static Module<Float> AIR_JUMP$SPEED;

    public static List<Module<Component>> PLAYER_CAMERA;

    public static BigModuleBase KEY_DISPLAY_BM;

    public static KeyMapping DISPLAY_INFO = new KeyMapping("key.info",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "key.category.x_client");

    public static KeyMapping OPEN = new KeyMapping("key.message",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "key.category.x_client");

    public static KeyMapping OPEN2 = new KeyMapping("key.y",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "key.category.x_client");
    public static KeyMapping CHOOSE_TARGET = new KeyMapping("key.choose",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_T,
            "key.category.x_client");
    public static Main instance = null;
    public static TimeHelper timeHelper;

    static void update() {
        float p = time_speed / 20.0F;
        millisD = p + millisD;
        millis = (long) millisD;
    }

    public static void create() {
        if (service == null)
            service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(Main::update, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    public Main() {
        create();
        base_timehelper = TimeHelper.create(base_timehelper, 20, 160);
        timeHelper = TimeHelper.create(timeHelper, 10, 170);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
        registerKey(DISPLAY_INFO, OPEN, OPEN2);
        setModules();
        setBms();
        Main.instance = this;
    }

    public static void registerKey(KeyMapping... keyMappings) {
        for (KeyMapping key : keyMappings)
            ClientRegistry.registerKeyBinding(key);
    }

    public static void setPlayerCamera() {
        setModules();
        PLAYER_CAMERA = new ArrayList<>();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null)
            for (Player player : mc.level.players()) {
                if (!player.isRemoved() && player.tickCount > 1 && player.deathTime <= 0) {
                    Module<Component> player_camera = new Module<>("", player.getDisplayName(), false, Minecraft.getInstance().font).setLeft(b -> Minecraft.getInstance().cameraEntity = player).setColor(new Color(timeHelper.integer_time / 2, 130, timeHelper.integer_time, timeHelper.integer_time));
                    PLAYER_CAMERA.add(player_camera);
                }
            }
    }

    public static void setBms() {
        KEY_DISPLAY_BM = new KeyDisplayBM();
    }

    public static void reload() {
        ModuleManager.modules.clear();
        ModuleManager.configuration_father_modules.clear();
        Module.every.clear();
        CLIENT = new Module<>("[Forge]X-Client:1.0").setFont(RainbowFont.INS).setLeft(d -> {
            BmMain.COMBAT.setPos(0, 11);
            BmMain.PLAYER.setPos(0, 22);
            BmMain.RENDER.setPos(0, 33);
            BmMain.MISC.setPos(0, 44);
        });

        AUTO_FIGHT = new Module<>("Auto Fight", auto_fight, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft(d -> auto_fight=!auto_fight).addChild(AUTO_FIGHT$ROTATION, AUTO_FIGHT$ATTACK_DELAY, AUTO_FIGHT$AUTO_SHIELD, AUTO_FIGHT$NORMAL_TRACKING, AUTO_FIGHT$TELEPORTATION_TRACKING);
        AUTO_FIGHT$ATTACK_DELAY = new Module<>("Auto Fight Attack Delay", auto_fight_attack_delay, false, RainbowFont.NORMAL).setLeft(d -> auto_fight_attack_delay=!auto_fight_attack_delay).setFather(AUTO_FIGHT);
        AUTO_FIGHT$ROTATION = new Module<>("Auto Fight Rotation", auto_fight_rotation, false, RainbowFont.NORMAL).setLeft(d -> auto_fight_rotation=!auto_fight_rotation).setFather(AUTO_FIGHT);
        AUTO_FIGHT$AUTO_SHIELD = new Module<>("Auto Fight Auto Shield", auto_fight_auto_shield, false, RainbowFont.NORMAL).setLeft(d -> auto_fight_auto_shield=!auto_fight_auto_shield).setFather(AUTO_FIGHT);
        AUTO_FIGHT$TELEPORTATION_TRACKING = new Module<>("Auto Fight Teleportation Tracking", auto_fight_teleportation_tracking, false, RainbowFont.NORMAL).setLeft(d -> auto_fight_teleportation_tracking=!auto_fight_teleportation_tracking).setFather(AUTO_FIGHT);
        AUTO_FIGHT$NORMAL_TRACKING = new Module<>("Auto Fight Normal Tracking", auto_fight_normal_tracking, false, RainbowFont.NORMAL).setLeft(d -> auto_fight_normal_tracking=!auto_fight_normal_tracking).setFather(AUTO_FIGHT);
        AUTO_ATTACK = new Module<>("Auto Attack", auto_attack, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft((d -> auto_attack = !auto_attack));
        SUPER_KILL_AURA$RANGE = new Module<>("KillAura Range", killaura_range, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft((d -> killaura_range += 0.5F)).setRight(d -> {
            if (killaura_range >= 0.1F)
                killaura_range -= 0.1F;
        }).setFather(SUPER_KILL_AURA);
        SUPER_KILL_AURA$ATTACK_PLAYER = new Module<>("KillAura AttackPlayer", killaura_attackPlayer, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft((d -> killaura_attackPlayer = !killaura_attackPlayer)).setFather(SUPER_KILL_AURA);
        SUPER_KILL_AURA$ROTATION = new Module<>("KillAura Rotation", killaura_rotation, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft((d -> killaura_rotation = !killaura_rotation)).setFather(SUPER_KILL_AURA);
        SUPER_KILL_AURA = new Module<>("Super KillAura", superKillAura, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft((d -> superKillAura = !superKillAura)).addChild(SUPER_KILL_AURA$RANGE, SUPER_KILL_AURA$ATTACK_PLAYER, SUPER_KILL_AURA$ROTATION);
        QUICKLY_BOW = new Module<>("Quickly Bow", quickly_bow, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft(d -> quickly_bow = !quickly_bow);
        CRITICAL = new Module<>("Critical", critical, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft(d -> critical = !critical);
        ARROW_DODGE = new Module<>("Arrow Dodge", arrow_dodge, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft(d -> arrow_dodge = !arrow_dodge);
        AUTO_RELEASE = new Module<>("Auto Release", auto_release, false, RainbowFont.NORMAL).setFather_Bm(BmMain.COMBAT).setLeft(d -> auto_release = !auto_release);

        AIR_JUMP$SPEED = new Module<>("Air Jump Speed", air_jump_speed, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft(d -> air_jump_speed += 0.1F).setRight(d -> air_jump_speed -= 0.1).setFather(AIR_JUMP);
        AIR_JUMP = new Module<>("Air Jump", air_jump, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft(d -> air_jump = !air_jump).addChild(AIR_JUMP$SPEED);
        FLY = new Module<>("Fly", fly, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> {
            fly = !fly;
            Minecraft mc = Minecraft.getInstance();
            Abilities abilities = new Abilities();
            abilities.mayfly = fly;
            if (mc.player != null) {
                mc.player.getAbilities().mayfly = fly;
                mc.player.connection.send(new ServerboundPlayerAbilitiesPacket(abilities));
            }
        }));
        JUMPING = new Module<>("Jumping", jumping, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> jumping = !jumping));
        NO_FALL = new Module<>("No Fall", no_fall, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> no_fall = !no_fall));
        RESPAWN = new Module<>("Respawn", respawn, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> respawn = !respawn));
        SPRINT = new Module<>("Sprint", sprint, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> sprint = !sprint));
        REACH = new Module<>("Reach", reach_distance, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft((d -> reach_distance += 0.1F)).setRight((d -> {
            if (reach_distance > 0F)
                reach_distance -= 0.1F;
        }));
        QUICKLY_PLACE = new Module<>("Quickly Place", quickly_place, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setFather_Bm(BmMain.PLAYER).setLeft(d -> quickly_place = !quickly_place);
        SPEED = new Module<>("Speed", speed, false, RainbowFont.NORMAL).setFather_Bm(BmMain.PLAYER).setLeft(d -> speed+=0.025F).setRight(d -> {
            if (speed >=0.025F)
                speed-=0.025F;
        });

        ENABLE_HURT_EFFECT = new Module<>("Hurt Effect", enableHurtEffect, false, RainbowFont.NORMAL).setFather_Bm(BmMain.RENDER).setLeft((d -> enableHurtEffect = !enableHurtEffect));
        RENDER_OUTLINE = new Module<>("Render Players Outline", renderPlayerOutline, false, RainbowFont.NORMAL).setFather_Bm(BmMain.RENDER).setLeft((d -> renderPlayerOutline = !renderPlayerOutline));
        FULL_BRIGHT = new Module<>("Full Bright", full_bright, false, RainbowFont.NORMAL).setFather_Bm(BmMain.RENDER).setLeft((d -> {
            full_bright = !full_bright;
            GAMMA = new Saver<>((int) Minecraft.getInstance().options.gamma);
            if (full_bright) {
                Minecraft.getInstance().options.gamma = 100D;
            } else Minecraft.getInstance().options.gamma = GAMMA.getV();
        }));
        DISABLE_NEGATIVE_EFFECT_RENDERER = new Module<>("Disable NegativeEffect Rendering", dner, false, RainbowFont.NORMAL).setFather_Bm(BmMain.RENDER).setLeft((d -> dner = !dner));
        KEY_DISPLAY = new Module<>("Key Display", key_display, false, RainbowFont.NORMAL).setFather_Bm(BmMain.RENDER).setLeft(d -> key_display = !key_display);

        TIMER = new Module<>("Timer", time_speed, false, RainbowFont.NORMAL).setFather_Bm(BmMain.MISC).setLeft(d -> time_speed+=0.05F).setRight(d -> {
            if (time_speed >= 0.05F)
                time_speed-=0.05F;
        });
        GHOST = new Module<>("Ghost", enabledCameraGhost, false, RainbowFont.NORMAL).setFather_Bm(BmMain.MISC).setLeft(d -> {
            enabledCameraGhost = !enabledCameraGhost;
            if (!enabledCameraGhost)
                CameraCore.POS = new Vec3(0 ,0 ,0);
        }).addChild(GHOST$SPEED);
        GHOST$SPEED = new Module<>("Ghost Speed", cameraGhostSpeed, false, RainbowFont.NORMAL).setFather_Bm(BmMain.MISC).setFather(GHOST).setLeft(d -> cameraGhostSpeed+=0.1F).setRight(d -> {
            if (cameraGhostSpeed >= 0.1F)
                cameraGhostSpeed -= 0.1F;
        });

        YScreen.RETURN_LOCAL = new Module<>("Return Local").setLeft(b -> Minecraft.getInstance().cameraEntity = Minecraft.getInstance().player).unaddToList();
    }

    public static void setModules() {
        reload();
        BmMain.setBms();
    }

    @Mod.EventBusSubscriber
    public static class ShaderEvents {
        @SuppressWarnings("ConstantConditions")
        @SubscribeEvent
        public static void renderHudEvent(Render2DEvent event) {
            Minecraft mc = Minecraft.getInstance();
            int x=0,y=mc.getWindow().getGuiScaledHeight()-mc.font.lineHeight;
            PoseStack stack = new PoseStack();
            if (auto_fight) {
                mc.font.drawShadow(stack, I18n.get("info.choose"), x, y, 0xFFFFFFFF);
                y-=mc.font.lineHeight+1;
                if (AutoFightCore.Target != null) {
                    Entity target = AutoFightCore.Target;
                    Font f = RainbowFont.WATER;
                    f.drawShadow(stack, "Name:" + target.getName().getString(), x, y, 0);
                    y -= mc.font.lineHeight + 1;
                    Vec3 v = target.position();
                    v = new Vec3(Double.parseDouble(String.format("%.2f", v.x)), Double.parseDouble(String.format("%.2f", v.y)), Double.parseDouble(String.format("%.2f", v.z)));
                    f.drawShadow(stack, "Position:" + v.toString().replace(",", ", "), x, y, 0);
                    y -= mc.font.lineHeight + 1;
                    Vec3 m = target.getDeltaMovement();
                    m = new Vec3(Double.parseDouble(String.format("%.2f", m.x)), Double.parseDouble(String.format("%.2f", m.y)), Double.parseDouble(String.format("%.2f", m.z)));
                    f.drawShadow(stack, "Motion:"+m.toString().replace(",", ", "), x, y, 0);
                    y -= mc.font.lineHeight + 1;
                    if (target instanceof LivingEntity l) {
                        f.drawShadow(stack, "Health:(" + String.format("%.2f", l.getHealth()) + "/" + String.format("%.2f", l.getMaxHealth()) + ")", x, y, 0);
                        y -= mc.font.lineHeight + 1;
                        f.drawShadow(stack, "Armor:" + String.format("%.2f", l.getAttribute(Attributes.ARMOR).getBaseValue()), x, y, 0);
                        y -= mc.font.lineHeight + 1;
                        f.drawShadow(stack, "AttackDamage:" + String.format("%.2f", l.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()), x, y, 0);
                        y -= mc.font.lineHeight + 1;
                    }
                }
            }
            if (enabledCameraGhost && mc.player != null) {
                Vec3 v = mc.gameRenderer.getMainCamera().getPosition();
                v = new Vec3(Double.parseDouble(String.format("%.2f", v.x)), Double.parseDouble(String.format("%.2f", v.y)), Double.parseDouble(String.format("%.2f", v.z)));
                mc.font.drawShadow(stack,"CameraPosition:" + v.toString().replace("(", "").replace(")", "").replace(",", ", ") , x, y, 0xFFFFFFFF);
            }
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class TickKeys {
        @SubscribeEvent
        public static void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
            MegaUtil.writeXCLIENT();
            if (PLAYER_CAMERA != null)
                PLAYER_CAMERA.clear();
        }

        @SubscribeEvent
        public static void input(InputEvent.KeyInputEvent event) {
            if (DISPLAY_INFO.consumeClick())
                enable_display_info = !enable_display_info;
            if (CHOOSE_TARGET.consumeClick() && Minecraft.getInstance().player != null) {
                AutoFightCore.Target = xclient.mega.utils.MegaUtil.getEntityToWatch(20, Minecraft.getInstance().player);
            }
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class CoreEvents {
        public static int client_ticks = 0;

        @SubscribeEvent
        public static void playerUpdate(TickEvent.PlayerTickEvent event) {
            if (event.side.isClient() && event.player instanceof LocalPlayer) {
                if (sprint) {
                    event.player.setSprinting(true);
                }
            }
        }

        @SubscribeEvent
        public static void renderUpdate(TickEvent.RenderTickEvent event) {
            if (quickly_place)
                Minecraft.getInstance().rightClickDelay = 0;
        }

        @SubscribeEvent
        public static void onKey(InputEvent.KeyInputEvent event) {
            if (OPEN.consumeClick())
                Minecraft.getInstance().setScreen(new XScreen());
            if (OPEN2.consumeClick())
                Minecraft.getInstance().setScreen(new YScreen()); 
        }

        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            client_ticks++;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            Entity point = mc.crosshairPickEntity;
            if (killaura_range > 6.0F)
                killaura_range = 6.0F;
            if (player != null) {
                if (superKillAura) {
                    for (Entity entity : xclient.mega.utils.MegaUtil.getEntitiesToWatch(30, player)) {
                        if (entity instanceof LivingEntity livingEntity && point != player && !livingEntity.isDeadOrDying() && !livingEntity.isInvisible() && player.distanceTo(entity) <= killaura_range && livingEntity.deathTime <= 0) {
                            if (mc.gameMode != null && ((LivingEntity) entity).hurtTime <= 0) {
                                if (client_ticks % 2 == 0 && !killaura_attackPlayer || (!(livingEntity instanceof Player) && killaura_attackPlayer)) {
                                    if (killaura_rotation)
                                        for (int i=0;i<40;i++)
                                            RotationUtil.rotationAtoB(player, livingEntity);
                                    mc.gameMode.attack(player, entity);

                                }
                            }
                            player.swing(InteractionHand.MAIN_HAND);
                        }
                    }
                }
            }
            if (point != null) {
                if (player != null)
                    if (auto_attack && client_ticks % 7 == 0) {
                        if (point instanceof LivingEntity livingEntity && point != player && !livingEntity.isDeadOrDying() && !livingEntity.isInvisible() && player.distanceTo(point) <= 3.8F && livingEntity.deathTime <= 0) {
                            if (mc.gameMode != null) {
                                mc.gameMode.attack(player, point);
                                player.swing(InteractionHand.MAIN_HAND);
                            }
                        }
                    }
            }
        }

        @SubscribeEvent
        public static void renderPointAndXCInfo(Render2DEvent event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            Entity toWatch = xclient.mega.utils.MegaUtil.getEntityToWatch(20, player);
            Font font = mc.font;
            PoseStack stack = new PoseStack();
            if (toWatch != null) {
                if (toWatch instanceof LivingEntity living_point)
                    InventoryScreen.renderEntityInInventory(105, 100, 30, 45, 45, living_point);
                else font.drawShadow(stack, "No model", 105, 100, 0xFFFFFFFF);
                if (toWatch instanceof LivingEntity livingEntity) {
                    font.drawShadow(stack, "Health:" + String.format("%.2f", livingEntity.getHealth()) + "/" + String.format("%.2f", livingEntity.getMaxHealth()), 105, (int) toWatch.getEyeHeight() * 5 + 118, RendererUtils.WHITE);
                    font.drawShadow(stack, "MainHandItem:" + livingEntity.getMainHandItem().getDisplayName().getString(), 105, (int) toWatch.getEyeHeight() * 5 + 126, RendererUtils.WHITE);
                }
            }
            int x = 0;
            int y = 0;
            if (enable_display_info && !(mc.screen instanceof XScreen) && !YScreen.display_players) {
                for (Module<?> module : ModuleManager.modules) {
                    module.render(stack, x, y, false);
                    y += 11;
                }
            }
        }
    }
}
