package xclient.mega;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xclient.mega.mod.ModuleValue;
import xclient.mega.utils.Textures;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MegaUtil {
    public static List<String> entity_banned_ = new ArrayList<>();
    public static List<String> effect_banned_ = new ArrayList<>();
    public static List<String> particle_banned = new ArrayList<>();
    public static List<String> enchantment_banned = new ArrayList<>();
    public static List<String> item_name_banned = new ArrayList<>();
    public static List<String> item_keyname_banned = new ArrayList<>();
    public static List<String> structure_banned = new ArrayList<>();
    public static List<String> texture_banned = new ArrayList<>();
    public static List<String> textureName_banned = new ArrayList<>();
    public static List<String> explosion_banned = new ArrayList<>();
    public static List<String> dimension_banned = new ArrayList<>();
    public static List<String> se_banned = new ArrayList<>();

    public static boolean getT(ForgeRegistryEntry<EntityType<?>> type) {
        if (type.getRegistryName() != null)
            return Config.ENTITY_BANNED_LIST.get().contains(getEntityTypeResourceLocationSimpleNameWithRegistryPath(type.getRegistryName()));
        return false;
    }

    public static void addT(ResourceLocation type) {
        if (!getT(type))
            Config.ENTITY_BANNED_LIST.get().add(getEntityTypeResourceLocationSimpleNameWithRegistryPath(type));
        Config.ENTITY_BANNED_LIST.save();
    }

    public static void removeT(ResourceLocation type) {
        Config.ENTITY_BANNED_LIST.get().remove(getEntityTypeResourceLocationSimpleNameWithRegistryPath(type));
        Config.ENTITY_BANNED_LIST.save();
    }

    public static boolean getT(ResourceLocation type) {
        return Config.ENTITY_BANNED_LIST.get().contains(getEntityTypeResourceLocationSimpleNameWithRegistryPath(type));
    }

    public static void add(ParticleType<?> type) {
        if (!get(type))
            Config.PARTICLE_BANNED_LIST.get().add(getParticleSimpleNameWithRegistryPath(type));
        Config.PARTICLE_BANNED_LIST.save();
    }

    public static void remove(ParticleType<?> type) {
        Config.PARTICLE_BANNED_LIST.get().remove(getParticleSimpleNameWithRegistryPath(type));
        Config.PARTICLE_BANNED_LIST.save();
    }

    public static boolean get(ParticleType<?> type) {
        return Config.PARTICLE_BANNED_LIST.get().contains(getParticleSimpleNameWithRegistryPath(type));
    }

    public static void add(ForgeRegistryEntry<Enchantment> type) {
        if (!get(type))
            Config.ITEM_ENCHANTMENT_BANNED_LIST.get().add(getEnchantmentSimpleNameWithRegistryPath(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static void remove(ForgeRegistryEntry<Enchantment> type) {
        Config.ITEM_ENCHANTMENT_BANNED_LIST.get().remove(getEnchantmentSimpleNameWithRegistryPath(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static boolean get(ForgeRegistryEntry<Enchantment> type) {
        return Config.ITEM_ENCHANTMENT_BANNED_LIST.get().contains(getEnchantmentSimpleNameWithRegistryPath(type));
    }

    public static void addE(ForgeRegistryEntry<MobEffect> type) {
        if (!getE(type))
            Config.ITEM_ENCHANTMENT_BANNED_LIST.get().add(getMobEffectSimpleNameWithRegistryPath(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static void removeE(ForgeRegistryEntry<MobEffect> type) {
        Config.ITEM_ENCHANTMENT_BANNED_LIST.get().remove(getMobEffectSimpleNameWithRegistryPath(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static boolean getE(ForgeRegistryEntry<MobEffect> type) {
        return Config.ITEM_ENCHANTMENT_BANNED_LIST.get().contains(getMobEffectSimpleNameWithRegistryPath(type));
    }

    public static void addI(Item type) {
        if (!get(type))
            item_name_banned.add(type.getName(new ItemStack(type)).getString());
    }

    public static boolean get(Item type) {
        return item_name_banned.contains(type.getName(new ItemStack(type)).getString()) || item_keyname_banned.contains(getItemSimpleNameWithRegistryPath(type));
    }

    public static void addS(ResourceLocation s) {
        if (!getS(s))
            Config.STRUCTURE_BANNED_LIST.get().add(getStructureName(s));
        Config.STRUCTURE_BANNED_LIST.save();
    }

    public static void removeS(ResourceLocation s) {
        Config.STRUCTURE_BANNED_LIST.get().remove(getStructureName(s));
        Config.STRUCTURE_BANNED_LIST.save();
    }

    public static boolean getS(ResourceLocation s) {
        return Config.STRUCTURE_BANNED_LIST.get().contains(getStructureName(s));
    }

    public static void addTexture(ResourceLocation s) {
        if (!getTexture(s))
            Config.TEXTURE_BANNED_LIST.get().add(getTextureName(s));
        Config.TEXTURE_BANNED_LIST.save();
    }

    public static void removeTexture(ResourceLocation s) {
        Config.TEXTURE_BANNED_LIST.get().remove(getTextureName(s));
        Config.TEXTURE_BANNED_LIST.save();
    }

    public static boolean getTexture(String s) {
        return Config.TEXTURE_NAME_BANNED_LIST.get().contains(s);
    }

    public static void addTexture(String s) {
        if (!getTexture(s))
            Config.TEXTURE_NAME_BANNED_LIST.get().add(s);
        Config.TEXTURE_NAME_BANNED_LIST.save();
    }

    public static void removeTexture(String s) {
        Config.TEXTURE_NAME_BANNED_LIST.get().remove(s);
        Config.TEXTURE_NAME_BANNED_LIST.save();
    }

    public static boolean getTexture(ResourceLocation s) {
        return Config.TEXTURE_BANNED_LIST.get().contains(getTextureName(s));
    }

    public static void addD(CapabilityProvider<Level> type) {
        if (!getD(type))
            Config.ITEM_ENCHANTMENT_BANNED_LIST.get().add(getDimensionName(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static void removeD(CapabilityProvider<Level> type) {
        Config.ITEM_ENCHANTMENT_BANNED_LIST.get().remove(getDimensionName(type));
        Config.ITEM_ENCHANTMENT_BANNED_LIST.save();
    }

    public static boolean getD(CapabilityProvider<Level> type) {
        return Config.ITEM_ENCHANTMENT_BANNED_LIST.get().contains(getDimensionName(type));
    }

    public static String getSimpleName(Object o) {
        return o.getClass().getSimpleName();
    }

    public static String getParticleSimpleNameWithRegistryPath(ParticleType<?> o) {
        if (o.getRegistryName() != null)
            return getSimpleName(o) + "  path->" + o.getRegistryName().getPath() + "  namespace->" + o.getRegistryName().getNamespace();
        return getSimpleName(o);
    }

    public static String getDimensionName(CapabilityProvider<Level> o) {
        return ((Level) o).dimensionType().toString();
    }

    public static String getEnchantmentSimpleNameWithRegistryPath(ForgeRegistryEntry<Enchantment> o) {
        if (o.getRegistryName() != null)
            return getSimpleName(o) + "  path->" + o.getRegistryName().getPath() + "  namespace->" + o.getRegistryName().getNamespace();
        return getSimpleName(o);
    }

    public static String getMobEffectSimpleNameWithRegistryPath(ForgeRegistryEntry<MobEffect> o) {
        if (o.getRegistryName() != null)
            return getSimpleName(o) + "  path->" + o.getRegistryName().getPath() + "  namespace->" + o.getRegistryName().getNamespace();
        return getSimpleName(o);
    }

    public static String getEntityTypeResourceLocationSimpleNameWithRegistryPath(ResourceLocation o) {
        return "EntityType" + "  path->" + o.getPath() + "  namespace->" + o.getNamespace();
    }

    public static String getItemSimpleNameWithRegistryPath(Item o) {
        if (o.getRegistryName() != null)
            return o.getClass().getSimpleName() + "  path->" + o.getRegistryName().getPath() + "  namespace->" + o.getRegistryName().getNamespace();
        return o.getClass().getSimpleName();
    }

    public static String getStructureName(ResourceLocation o) {
        return "structure-> " + o.toString();
    }

    public static String getTextureName(ResourceLocation o) {
        return o.toString();
    }

    public static <V, T extends net.minecraftforge.common.ForgeConfigSpec.ConfigValue<V>> void set(T cv, V value) {
        cv.set(value);
    }

    public static void writeXCLIENT() {
        if (!Main.hasRead)
            return;
        try {
            for (Field f : Main.class.getFields()) {
                if (f.isAnnotationPresent(ModuleValue.class)) {
                    String name = f.getName();
                    for (Field f_inConfig : Config.class.getFields()) {
                        if (Modifier.isStatic(f_inConfig.getModifiers())) {
                            if (name.equals(f_inConfig.getName())) {
                                Object f_value = f.get(Main.class);
                                Object f_inConfig_value = f_inConfig.get(Config.class);
                                if (f_value instanceof Boolean v) {
                                    ((ForgeConfigSpec.ConfigValue<Boolean>) f_inConfig_value).set(v);
                                }
                                if (f_value instanceof Integer v) {
                                    ((ForgeConfigSpec.ConfigValue<Integer>) f_inConfig_value).set(v);
                                }
                                if (f_value instanceof Float v) {
                                    ((ForgeConfigSpec.ConfigValue<Float>) f_inConfig_value).set(v);
                                }
                                if (f_value instanceof Double v) {
                                    ((ForgeConfigSpec.ConfigValue<Double>) f_inConfig_value).set(v);
                                }
                                if (f_value instanceof String v) {
                                    ((ForgeConfigSpec.ConfigValue<String>) f_inConfig_value).set(v);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable rabble) {
            rabble.printStackTrace();
        }
        /*
        set(Config.killaura_range, Main.killaura_range);
        set(Config.killaura_attackPlayer, Main.killaura_attackPlayer);
        set(Config.auto_attack, Main.auto_attack);
        set(Config.enableHurtEffect, Main.enableHurtEffect);
        set(Config.superKillAura, Main.superKillAura);
        set(Config.reach_distance, Main.reach_distance);
        set(Config.no_fall, Main.no_fall);
        set(Config.respawn, Main.respawn);
        set(Config.fly, Main.fly);
        set(Config.sprint, Main.sprint);
        set(Config.renderPlayerOutline, Main.renderPlayerOutline);
        set(Config.full_bright, Main.full_bright);
        set(Config.dner, Main.dner);
        set(Config.quickly_place, Main.quickly_place);
        set(Config.key_display, Main.key_display);
        set(Config.jumping, Main.jumping);
        set(Config.quickly_bow, Main.quickly_bow);
        */
        set(Config.background, Textures.background);
        set(Config._x_, Main._x_);
        set(Config._y_, Main._y_);
        set(Config.key_scale, Main.key_scale);

    }

    public static void read() {
        particle_banned = Config.PARTICLE_BANNED_LIST.get();
        entity_banned_ = Config.ENTITY_BANNED_LIST.get();
        item_name_banned = Config.ITEM_BANNED_LIST.get();
        item_keyname_banned = Config.ITEM_CLASS_BANNED_LIST.get();
        enchantment_banned = Config.ITEM_ENCHANTMENT_BANNED_LIST.get();
        effect_banned_ = Config.EFFECT_BANNED_LIST.get();
        structure_banned = Config.STRUCTURE_BANNED_LIST.get();
        texture_banned = Config.TEXTURE_BANNED_LIST.get();
        textureName_banned = Config.TEXTURE_NAME_BANNED_LIST.get();
        explosion_banned = Config.EXPLOSION_BANNED_LIST.get();
        dimension_banned = Config.DIMENSION_BANNED_LIST.get();

        try {
            for (Field f : Main.class.getFields()) {
                if (f.isAnnotationPresent(ModuleValue.class)) {
                    String name = f.getName();
                    for (Field f_inConfig : Config.class.getFields()) {
                        if (Modifier.isStatic(f_inConfig.getModifiers())) {
                            if (name.equals(f_inConfig.getName())) {
                                f.set(Main.class, ((ForgeConfigSpec.ConfigValue<?>) f_inConfig.get(Config.class)).get());
                            }
                        }
                    }
                }
            }
        } catch (Throwable rabble) {
            rabble.printStackTrace();
        }
        /*
        Main.killaura_range = Config.killaura_range.get();
        Main.killaura_attackPlayer = Config.killaura_attackPlayer.get();
        Main.auto_attack = Config.auto_attack.get();
        Main.enableHurtEffect = Config.enableHurtEffect.get();
        Main.superKillAura = Config.superKillAura.get();
        Main.reach_distance = Config.reach_distance.get();
        Main.no_fall = Config.no_fall.get();
        Main.respawn = Config.respawn.get();
        Main.fly = Config.fly.get();
        Main.sprint = Config.sprint.get();
        Main.renderPlayerOutline = Config.renderPlayerOutline.get();
        Main.full_bright = Config.full_bright.get();
        Main.dner = Config.dner.get();
        Main.key_display = Config.key_display.get();
        Main.quickly_place = Config.quickly_place.get();
        Main.jumping = Config.jumping.get();
        Main.quickly_bow = Config.quickly_bow.get();
         */
        Main._x_ = Config._x_.get();
        Main._y_ = Config._y_.get();
        Main.key_scale = Config.key_scale.get();
        Textures.background = Config.background.get();
        Main.hasRead = true;
    }

    public static boolean hasTexture(String location) {
        for (String s : Config.TEXTURE_BANNED_LIST.get()) {
            if (location.contains(s) || s.contains(location))
                return true;
        }
        for (String s : Config.TEXTURE_NAME_BANNED_LIST.get()) {
            if (location.contains(s) || s.contains(location))
                return true;
        }
        return false;
    }

    public static boolean explosionIsBanned(Entity entity) {
        if (explosion_banned.contains("all"))
            return true;
        if (entity instanceof PrimedTnt && explosion_banned.contains("tnt"))
            return true;
        if (entity instanceof MinecartTNT && explosion_banned.contains("minecraft"))
            return true;
        if (entity instanceof EndCrystal && explosion_banned.contains("crystal"))
            return true;
        return entity instanceof Creeper && explosion_banned.contains("creeper");
    }
}
