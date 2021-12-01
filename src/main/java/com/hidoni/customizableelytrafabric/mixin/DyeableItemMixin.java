package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import com.hidoni.customizableelytrafabric.item.ElytraWingItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyeableItem.class)
public interface DyeableItemMixin {
    @Redirect(method = "blendAndSetColor(Lnet/minecraft/item/ItemStack;Ljava/util/List;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/DyeableItem;hasColor(Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean redirectHasColor(DyeableItem instance, ItemStack stack) {
        if (instance instanceof CustomizableElytraItem || instance instanceof ElytraWingItem) {
            NbtCompound nbtCompound = stack.getSubNbt("display");
            return nbtCompound != null && nbtCompound.contains("color", 99);
        }
        return instance.hasColor(stack);
    }
}
