package rune.magic.common.entity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rune.magic.Main;
import rune.magic.client.render.entity.BallRenderer;

public class Register {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEventHandler {
        @SubscribeEvent
        public static void onClientSetUpEvent(FMLClientSetupEvent event) {
            EntityRenderers.register(BALL.get(), BallRenderer::new);
        }
    }
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Main.ID);
    public static final RegistryObject<EntityType<BallEntity>> BALL = ENTITY_TYPES.register("ball", () -> EntityType.Builder.of(BallEntity::new, MobCategory.MISC).sized(10, 10).build("ball"));

    public static void register() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
