package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraHideCapeRecipe extends SpecialCraftingRecipe {
    public ElytraHideCapeRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack paperItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack inventoryItem = inventory.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.PAPER) {
                    if (!paperItem.isEmpty()) {
                        return false;
                    }

                    paperItem = inventoryItem;
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

        return !elytraItem.isEmpty() && !paperItem.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        ItemStack paperItem = ItemStack.EMPTY;
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack inventoryItem = inventory.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.PAPER) {
                    if (!paperItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    paperItem = inventoryItem;
                } else if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA);
                    EnchantmentHelper.set(EnchantmentHelper.get(inventoryItem), customizableElytraItem);
                    if (!inventoryItem.getName().equals(Text.translatable(Items.ELYTRA.getTranslationKey()))) {
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
            elytraItem.getOrCreateNbt().putBoolean("HideCapePattern", true);
        }
        return elytraItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_HIDE_CAPE_RECIPE;
    }
}
