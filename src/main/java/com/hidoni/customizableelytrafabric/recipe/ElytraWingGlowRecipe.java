package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraWingGlowRecipe extends SpecialCraftingRecipe {
    public ElytraWingGlowRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack glowInkSacItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack inventoryItem = inventory.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.GLOW_INK_SAC) {
                    if (!glowInkSacItem.isEmpty()) {
                        return false;
                    }

                    glowInkSacItem = inventoryItem;
                } else {
                    if (inventoryItem.getItem() != Items.ELYTRA && inventoryItem.getItem() != ModItems.CUSTOMIZABLE_ELYTRA && inventoryItem.getItem() != ModItems.ELYTRA_WING) {
                        return false;
                    }

                    if (!elytraItem.isEmpty()) {
                        return false;
                    }

                    elytraItem = inventoryItem;
                }
            }
        }

        return !elytraItem.isEmpty() && !glowInkSacItem.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        ItemStack glowInkSacItem = ItemStack.EMPTY;
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack inventoryItem = inventory.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.GLOW_INK_SAC) {
                    if (!glowInkSacItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    glowInkSacItem = inventoryItem;
                } else if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA);
                    EnchantmentHelper.set(EnchantmentHelper.get(inventoryItem), customizableElytraItem);
                    if (!inventoryItem.getName().equals(new TranslatableText(Items.ELYTRA.getTranslationKey()))) {
                        customizableElytraItem.setCustomName(inventoryItem.getName());
                    }
                    customizableElytraItem.setDamage(inventoryItem.getDamage());
                    customizableElytraItem.setRepairCost(inventoryItem.getRepairCost());
                    elytraItem = customizableElytraItem;
                } else if (inventoryItem.getItem() == ModItems.CUSTOMIZABLE_ELYTRA || inventoryItem.getItem() == ModItems.ELYTRA_WING) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    elytraItem = inventoryItem.copy();
                    elytraItem.setCount(1);
                }
            }
        }

        if (!elytraItem.isEmpty()) {
            elytraItem.getOrCreateNbt().putInt("WingLightLevel", 1);
        }
        return elytraItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_WING_GLOW_RECIPE;
    }
}
