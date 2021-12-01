package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraBannerRecipe extends SpecialCraftingRecipe {
    public ElytraBannerRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack bannerItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() instanceof BannerItem) {
                    if (!bannerItem.isEmpty()) {
                        return false;
                    }

                    bannerItem = inventoryItem;
                } else {
                    if (inventoryItem.getItem() != Items.ELYTRA && inventoryItem.getItem() != ModItems.CUSTOMIZABLE_ELYTRA && inventoryItem.getItem() != ModItems.ELYTRA_WING) {
                        return false;
                    }

                    if (!elytraItem.isEmpty()) {
                        return false;
                    }

                    if (inventoryItem.getSubNbt("BlockEntityTag") != null || inventoryItem.getSubNbt("WingInfo") != null) {
                        return false;
                    }

                    elytraItem = inventoryItem;
                }
            }
        }

        return !elytraItem.isEmpty() && !bannerItem.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack bannerItem = ItemStack.EMPTY;
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() instanceof BannerItem) {
                    bannerItem = inventoryItem;
                } else if (inventoryItem.getItem() == Items.ELYTRA) {
                    ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA);
                    EnchantmentHelper.set(EnchantmentHelper.get(inventoryItem), customizableElytraItem);
                    if (elytraItem.hasCustomName()) {
                        customizableElytraItem.setCustomName(inventoryItem.getName());
                    }
                    customizableElytraItem.setDamage(inventoryItem.getDamage());
                    customizableElytraItem.setRepairCost(inventoryItem.getRepairCost());
                    elytraItem = customizableElytraItem;
                } else if (inventoryItem.getItem() == ModItems.CUSTOMIZABLE_ELYTRA || inventoryItem.getItem() == ModItems.ELYTRA_WING) {
                    elytraItem = inventoryItem.copy();
                    elytraItem.setCount(1);
                }
            }
        }

        if (!elytraItem.isEmpty()) {
            NbtCompound compoundnbt = bannerItem.getSubNbt("BlockEntityTag");
            NbtCompound compoundnbt1 = compoundnbt == null ? new NbtCompound() : compoundnbt.copy();
            compoundnbt1.putInt("Base", ((BannerItem) bannerItem.getItem()).getColor().getId());
            elytraItem.getOrCreateNbt().put("BlockEntityTag", compoundnbt1);
            NbtCompound displayTag = elytraItem.getSubNbt("display");
            if (displayTag != null && displayTag.contains("color", 99)) {
                displayTag.remove("color");
            }
        }
        return elytraItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_BANNER_RECIPE;
    }
}
