package rune.magic.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import rune.magic.Main;

@Mixin(ItemFrame.class)
public abstract class FrameTick extends HangingEntity {

    protected FrameTick(EntityType<? extends HangingEntity> p_31703_, Level p_31704_) {
        super(p_31703_, p_31704_);
    }

    @Override
    public void tick() {
        Main.VishantiSound.play(this);
        super.tick();
    }
}
