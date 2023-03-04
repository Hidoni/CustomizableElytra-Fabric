package com.hidoni.customizableelytrafabric.recipe;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Predicate;

public class SplitToWingRecipe extends SpecialCraftingRecipe {
    public SplitToWingRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }
    private static final TagKey<Item> ElytraItemTag = TagKey.of(RegistryKeys.ITEM, new Identifier("c","elytra"));

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        return !(getElytraItem(inv,stack->stack.isIn(ElytraItemTag)).isEmpty());
    }

    @Override
    public ItemStack craft(CraftingInventory inv, DynamicRegistryManager registryManager) {
        ItemStack elytraItem = getElytraItem(inv,stack->stack.isIn(ElytraItemTag) );
        if (elytraItem.isEmpty())
            return ItemStack.EMPTY;
        ItemStack leftWing = new ItemStack(ModItems.ELYTRA_WING);
        NbtCompound nbt = elytraItem.getOrCreateNbt();
        if (nbt.contains("WingInfo", NbtElement.COMPOUND_TYPE)) {
            NbtCompound wingInfo = nbt.getCompound("WingInfo");
            if (wingInfo.contains("left",NbtElement.COMPOUND_TYPE)) {
                leftWing.setNbt(wingInfo.getCompound("left"));
            }
        }

        EnchantmentHelper.set(EnchantmentHelper.get(elytraItem), leftWing);
        leftWing.setDamage(elytraItem.getDamage());
        leftWing.setRepairCost(elytraItem.getRepairCost());
        leftWing.removeCustomName();
        if (elytraItem.hasCustomName()) {
            leftWing.setCustomName(elytraItem.getName());
        }
        return leftWing;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
        DefaultedList<ItemStack> remainders = super.getRemainder(inventory);
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack elytraItem = inventory.getStack(slot);
            if (elytraItem.isIn(ElytraItemTag) && remainders.get(slot).isEmpty()) {
                ItemStack rightWing = new ItemStack(ModItems.ELYTRA_WING);
                NbtCompound nbt = elytraItem.getOrCreateNbt();
                if (nbt.contains("WingInfo", NbtElement.COMPOUND_TYPE)) {
                    NbtCompound wingInfo = nbt.getCompound("WingInfo");
                    if (wingInfo.contains("right",NbtElement.COMPOUND_TYPE)) {
                        rightWing.setNbt(wingInfo.getCompound("right"));
                    }
                }
                EnchantmentHelper.set(Map.of(),rightWing);
                rightWing.setDamage(elytraItem.getDamage());
                rightWing.setRepairCost(elytraItem.getRepairCost());
                rightWing.removeCustomName();
                remainders.set(slot,rightWing);
            }
        }
        return remainders;
    }

    private ItemStack getElytraItem(CraftingInventory inv, Predicate<ItemStack> itemCheck) {
        ItemStack elytraItem = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack inventoryItem = inv.getStack(i);
            if (!inventoryItem.isEmpty()) {
                if (!elytraItem.isEmpty() || !itemCheck.test(inventoryItem)) {
                    return ItemStack.EMPTY;
                }
                elytraItem = inventoryItem;
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
        return ModRecipes.SPLIT_TO_WING_RECIPE;
    }
}
