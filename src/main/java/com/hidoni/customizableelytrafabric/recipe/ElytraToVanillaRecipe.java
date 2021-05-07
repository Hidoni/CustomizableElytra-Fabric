package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraToVanillaRecipe extends SpecialCraftingRecipe
{
    public ElytraToVanillaRecipe(Identifier id)
    {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i)
        {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inv.isEmpty())
            {
                if (!elytraItem.isEmpty() || !(inventoryItem.getItem() instanceof CustomizableElytraItem))
                {
                    return false;
                }
                elytraItem = inventoryItem;
            }
        }

        return !elytraItem.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv)
    {
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i)
        {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inv.isEmpty())
            {
                if (!elytraItem.isEmpty() || !(inventoryItem.getItem() instanceof CustomizableElytraItem))
                {
                    return ItemStack.EMPTY;
                }
                elytraItem = inventoryItem;
            }
        }

        ItemStack vanillaElytraItem = new ItemStack(Items.ELYTRA, 1);
        EnchantmentHelper.set(EnchantmentHelper.get(elytraItem), vanillaElytraItem);
        if (elytraItem.hasCustomName())
        {
            vanillaElytraItem.setCustomName(elytraItem.getName());
        }
        vanillaElytraItem.setDamage(elytraItem.getDamage());
        vanillaElytraItem.setRepairCost(elytraItem.getRepairCost());
        return !elytraItem.isEmpty() ? vanillaElytraItem : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return null;
    }
}
