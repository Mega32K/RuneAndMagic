package rune.magic.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import rune.magic.utils.RendererUtils;

import java.awt.*;

public class VertexRender {
    private final PoseStack stack;
    private final String name;

    public PoseStack getStack() {
        return stack;
    }

    public VertexRender(PoseStack stack, String name) {
        this.stack = stack;
        this.name = name;
    }

    public VertexRender(PoseStack stack) {
        this(stack, stack.toString());
    }

    public void renderModel(ModelType type, Vector3f scale, Color color, Vec3 pos, VertexConsumer consumer ) {
        stack.pushPose();
        RenderSystem.setShaderColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        stack.scale(scale.x(), scale.y(), scale.z());
        stack.translate(pos.x, pos.y, pos.z);
        type.render(stack, consumer);
        stack.popPose();
    }

    public String getName() {
        return name;
    }

    public static enum ModelType {
        TRIANGLE(null),
        RECTANGLE(null),
        SQUARE(null),
        CIRCLE((sta, vertexConsumer) -> {
            sta.pushPose();
            Color col = Color.WHITE;
            Color col2 = new Color(20, 200, 255, 150);
            sta.scale(1,0.01F,1);
            RendererUtils.dragonDeathLight(1000, sta, col, col2, s -> s.translate(0,0,0));
            sta.popPose();
        });

        public VertexRenderLogic renderLogic;

        ModelType(VertexRenderLogic logic) {
            renderLogic = logic;
        }

        public void render(PoseStack stack, VertexConsumer vertexConsumer) {
            renderLogic.doRender(stack, vertexConsumer);
        }
    }
}
