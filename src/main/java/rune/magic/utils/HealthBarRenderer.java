package rune.magic.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class HealthBarRenderer {

    public static void drawDamageNumber(PoseStack matrix, int dmg, double x, double y,
                                        float width) {
        int i = Math.abs(Math.round(dmg));
        if (i == 0) {
            return;
        }
        String s = Integer.toString(i);
        Minecraft minecraft = Minecraft.getInstance();
        int sw = minecraft.font.width(s);
        int color = 1;
        RainbowFont.INS.drawShadow(matrix, s, (int) (x + (width / 2) - sw), (int) y + 5, color);
    }

}
