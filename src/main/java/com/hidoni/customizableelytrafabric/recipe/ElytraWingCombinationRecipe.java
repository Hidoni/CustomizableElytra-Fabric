package com.hidoni.customizableelytrafabric.recipe;

import com.google.common.collect.Maps;
import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;

public class ElytraWingCombinationRecipe extends SpecialCraftingRecipe {
    public ElytraWingCombinationRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        boolean isEnchanted=false;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                        isEnchanted = !EnchantmentHelper.get(leftWing).isEmpty();
                    } else if (rightWing == ItemStack.EMPTY && (!isEnchanted || EnchantmentHelper.get(inventoryItem).isEmpty())) {
                        rightWing = inventoryItem;
                    } else // We've already found two items.
                    {
                        return false;
                    }
                }
            }
        }
        return !leftWing.isEmpty() && !rightWing.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        boolean isEnchanted=false;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                        isEnchanted = !EnchantmentHelper.get(leftWing).isEmpty();
                    } else if (rightWing == ItemStack.EMPTY && (!isEnchanted || EnchantmentHelper.get(inventoryItem).isEmpty()))  {
                        rightWing = inventoryItem;
                    } else // We've already found two items.
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        ItemStack customizedElytra = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA);
        customizedElytra.setDamage((leftWing.getDamage() + rightWing.getDamage()) / 2);
        Map<Enchantment, Integer> enchantments= EnchantmentHelper.get(leftWing);
        if (enchantments.isEmpty()) {
            enchantments=EnchantmentHelper.get(rightWing);
        }
        EnchantmentHelper.set(enchantments,customizedElytra);
        customizedElytra.setRepairCost(Math.max(leftWing.getRepairCost(),rightWing.getRepairCost()));
        if (leftWing.hasCustomName()) {
            customizedElytra.setCustomName(leftWing.getName());
        }else if(rightWing.hasCustomName()) {
            customizedElytra.setCustomName(rightWing.getName());
        }
        NbtCompound leftWingNBT = convertWingToNBT(leftWing);
        NbtCompound rightWingNBT = convertWingToNBT(rightWing);
        NbtCompound wingInfo = new NbtCompound();
        if (leftWingNBT != null) {
            wingInfo.put("left", leftWingNBT);
        }
        if (rightWingNBT != null) {
            wingInfo.put("right", rightWingNBT);
        }
        customizedElytra.setSubNbt("WingInfo", wingInfo);
        return customizedElytra;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_WING_COMBINATION_RECIPE;
    }

    public NbtCompound convertWingToNBT(ItemStack wingIn) {
        return wingIn.getOrCreateNbt();
    }
}
