package rune.magic.mixin;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class SafeInsert {
    @Shadow public abstract boolean mayPlace(ItemStack p_40231_);

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void set(ItemStack p_40240_);

    @Shadow public abstract int getMaxStackSize(ItemStack p_40238_);

    /**
     * @author mega
     * @reason to safe
     */
    @Overwrite
    public ItemStack safeInsert(ItemStack p_150657_, int p_150658_) {
        if (!p_150657_.isEmpty() && this.mayPlace(p_150657_)) {
            ItemStack itemstack = this.getItem();
            int i = Math.min(Math.min(p_150658_, p_150657_.getCount()), this.getMaxStackSize(p_150657_) - itemstack.getCount());
            if (itemstack.isEmpty()) {
                this.set(p_150657_.split(i));
            } else if (ItemStack.isSameItemSameTags(itemstack, p_150657_) ) {
                p_150657_.shrink(i);
                itemstack.grow(i);
                this.set(itemstack);
            }

            return p_150657_;
        } else {
            return p_150657_;
        }
    }
}
