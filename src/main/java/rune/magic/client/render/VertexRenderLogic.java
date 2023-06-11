package rune.magic.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface VertexRenderLogic {
    void doRender(PoseStack stack, VertexConsumer vertexConsumer);
}
