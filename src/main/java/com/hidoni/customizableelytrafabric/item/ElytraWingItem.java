package com.hidoni.customizableelytrafabric.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElytraWingItem extends Item implements DyeableItem {
    public ElytraWingItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getSubTag("display");
        if (compoundTag != null) {
            return compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 16777215;
        }
        compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null) {
            return DyeColor.byId(compoundTag.getInt("Base")).getMaterialColor().color;
        }
        return 16777215;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        return DyeableItem.super.hasColor(stack) || compoundTag != null || stack.getTag().getBoolean("HideCapePattern");
    }

    @Override
    public void removeColor(ItemStack stack) {
        DyeableItem.super.removeColor(stack);
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null) {
            stack.removeSubTag("BlockEntityTag");
        }
        stack.getOrCreateTag().remove("HideCapePattern");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateTag().getBoolean("HideCapePattern")) {
            tooltip.add(new TranslatableText(CustomizableElytraItem.HIDDEN_CAPE_TRANSLATION_KEY).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        BannerItem.appendBannerTooltip(stack, tooltip);
    }
}
