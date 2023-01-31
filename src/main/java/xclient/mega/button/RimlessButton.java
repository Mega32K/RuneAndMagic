package xclient.mega.button;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RimlessButton extends SolidColorButton {
    public RimlessButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
        R = G = B = 1.0F;
    }

    public RimlessButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_, float alpha) {
        this(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
        this.alpha = alpha;
    }

    public RimlessButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_, float r, float g, float b, float alpha) {
        this(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_, alpha);
        setColor(r, g, b);
    }

    @Override
    protected int getYImage(boolean p_93668_) {
        int i = 4;
        if (!this.active) {
            i = 3;
        } else if (p_93668_) {
            i = 5;
        }
        return i;
    }
}
