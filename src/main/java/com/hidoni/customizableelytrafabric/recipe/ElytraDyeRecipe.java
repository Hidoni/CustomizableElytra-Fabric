package com.hidoni.customizableelytrafabric.recipe;

import com.google.common.collect.Lists;
import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class ElytraDyeRecipe extends SpecialCraftingRecipe {
    public ElytraDyeRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack elytraItem = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return false;
                    }

                    elytraItem = inventoryItem;
                } else {
                    if (!(inventoryItem.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    list.add(inventoryItem);
                }
            }
        }

        return !elytraItem.isEmpty() && !list.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack elytraItem = ItemStack.EMPTY;
        List<DyeItem> list = Lists.newArrayList();

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    elytraItem = inventoryItem;
                } else {
                    if (!(inventoryItem.getItem() instanceof DyeItem)) {
                        return ItemStack.EMPTY;
                    }

                    list.add((DyeItem) inventoryItem.getItem());
                }
            }
        }

        ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA, elytraItem.getCount());
        EnchantmentHelper.set(EnchantmentHelper.get(elytraItem), customizableElytraItem);
        if (elytraItem.hasCustomName()) {
            customizableElytraItem.setCustomName(elytraItem.getName());
        }
        customizableElytraItem.setDamage(elytraItem.getDamage());
        customizableElytraItem.setRepairCost(elytraItem.getRepairCost());
        return !elytraItem.isEmpty() && !list.isEmpty() ? DyeableItem.blendAndSetColor(customizableElytraItem, list) : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_DYE_RECIPE;
    }
}
