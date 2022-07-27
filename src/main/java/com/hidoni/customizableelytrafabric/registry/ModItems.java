package com.hidoni.customizableelytrafabric.registry;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import com.hidoni.customizableelytrafabric.item.ElytraWingItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item CUSTOMIZABLE_ELYTRA = Registry.register(Registry.ITEM, new Identifier(CustomizableElytra.MOD_ID, "customizable_elytra"), new CustomizableElytraItem(new FabricItemSettings().maxDamage(Items.ELYTRA.getMaxDamage()).rarity(Rarity.UNCOMMON).equipmentSlot(stack -> EquipmentSlot.CHEST)));

    public static final Item ELYTRA_WING = Registry.register(Registry.ITEM, new Identifier(CustomizableElytra.MOD_ID, "elytra_wing"), new ElytraWingItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(Items.ELYTRA.getMaxDamage())));

    public static void register() {


        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(CUSTOMIZABLE_ELYTRA, (state, world, pos, player, hand, stack) -> {
            if (!(stack.getItem() instanceof CustomizableElytraItem)) {
                return ActionResult.PASS;
            } else {
                if (!world.isClient) {
                    ItemStack vanillaElytraItemStack = new ItemStack(Items.ELYTRA, 1);
                    EnchantmentHelper.set(EnchantmentHelper.get(stack), vanillaElytraItemStack);
                    if (stack.hasCustomName()) {
                        vanillaElytraItemStack.setCustomName(stack.getName());
                    }
                    vanillaElytraItemStack.setDamage(stack.getDamage());
                    vanillaElytraItemStack.setRepairCost(stack.getRepairCost());
                    player.setStackInHand(hand, vanillaElytraItemStack);
                    player.incrementStat(Stats.CLEAN_ARMOR);
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }

                return ActionResult.success(world.isClient);
            }
        });
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(ELYTRA_WING, CauldronBehavior.CLEAN_DYEABLE_ITEM);
    }
}
