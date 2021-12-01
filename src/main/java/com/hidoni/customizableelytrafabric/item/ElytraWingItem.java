package com.hidoni.customizableelytrafabric.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElytraWingItem extends Item implements DyeableItem {
    public ElytraWingItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound compoundTag = stack.getSubNbt("display");
        if (compoundTag != null) {
            return compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 16777215;
        }
        compoundTag = stack.getSubNbt("BlockEntityTag");
        if (compoundTag != null) {
            return DyeColor.byId(compoundTag.getInt("Base")).getMapColor().color;
        }
        return 16777215;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        NbtCompound compoundTag = stack.getSubNbt("BlockEntityTag");
        return DyeableItem.super.hasColor(stack) || compoundTag != null || stack.getOrCreateNbt().getInt("WingLightLevel") > 0 || stack.getOrCreateNbt().getBoolean("HideCapePattern");
    }

    @Override
    public void removeColor(ItemStack stack) {
        DyeableItem.super.removeColor(stack);
        NbtCompound compoundTag = stack.getSubNbt("BlockEntityTag");
        if (compoundTag != null) {
            stack.removeSubNbt("BlockEntityTag");
        }
        stack.getOrCreateNbt().remove("HideCapePattern");
        stack.getOrCreateNbt().remove("WingLightLevel");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CustomizableElytraItem.applyTooltip(tooltip, context, stack.getOrCreateNbt(), true);
    }
}
