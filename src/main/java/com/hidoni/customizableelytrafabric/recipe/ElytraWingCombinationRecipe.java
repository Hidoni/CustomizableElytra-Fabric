package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraWingCombinationRecipe extends SpecialCraftingRecipe
{
    public ElytraWingCombinationRecipe(Identifier id)
    {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i)
        {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty())
            {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING)
                {
                    if (leftWing == ItemStack.EMPTY)
                    {
                        leftWing = inventoryItem;
                    }
                    else if (rightWing == ItemStack.EMPTY)
                    {
                        rightWing = inventoryItem;
                    }
                    else // We've already found two items.
                    {
                        return false;
                    }
                }
            }
        }
        return !leftWing.isEmpty() && !rightWing.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv)
    {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i)
        {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty())
            {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING)
                {
                    if (leftWing == ItemStack.EMPTY)
                    {
                        leftWing = inventoryItem;
                    }
                    else if (rightWing == ItemStack.EMPTY)
                    {
                        rightWing = inventoryItem;
                    }
                    else // We've already found two items.
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        ItemStack customizedElytra = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA);
        CompoundTag leftWingNBT = convertWingToNBT(leftWing);
        CompoundTag rightWingNBT = convertWingToNBT(rightWing);
        CompoundTag wingInfo = new CompoundTag();
        if (leftWingNBT != null)
        {
            wingInfo.put("left", leftWingNBT);
        }
        if (rightWingNBT != null)
        {
            wingInfo.put("right", rightWingNBT);
        }
        customizedElytra.putSubTag("WingInfo", wingInfo);
        return customizedElytra;
    }

    @Override
    public boolean fits(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.ELYTRA_WING_COMBINATION_RECIPE;
    }

    public CompoundTag convertWingToNBT(ItemStack wingIn)
    {
        return wingIn.getOrCreateTag();
    }
}
