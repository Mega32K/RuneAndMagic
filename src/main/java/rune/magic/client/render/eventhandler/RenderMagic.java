package rune.magic.client.render.eventhandler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rune.magic.client.render.VertexRender;
import rune.magic.common.item.TheBookOfVishanti;
import rune.magic.utils.RendererUtils;

import java.awt.*;

@Mod.EventBusSubscriber
public class RenderMagic {
    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {

    }
}
