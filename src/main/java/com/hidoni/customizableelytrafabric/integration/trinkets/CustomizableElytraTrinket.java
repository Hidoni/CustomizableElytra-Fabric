package com.hidoni.customizableelytrafabric.integration.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.event.GameEvent;

public class CustomizableElytraTrinket implements Trinket {
    public static boolean useCustomElytraTrinket(LivingEntity entity, Pair<SlotReference, ItemStack> slotReferenceItemStackPair, boolean tickElytra) {
        if (ElytraItem.isUsable(slotReferenceItemStackPair.getRight())) {
            if (tickElytra) {
                doTrinketElytraTick(entity, slotReferenceItemStackPair.getRight(), slotReferenceItemStackPair.getLeft());
            }
            return true;
        }

        return false;
    }

    public static void doTrinketElytraTick(LivingEntity entity, ItemStack stack, SlotReference slotReference) {
        int nextRoll = entity.getRoll() + 1;

        if (!entity.world.isClient && nextRoll % 10 == 0) {
            if ((nextRoll / 10) % 2 == 0) {
                stack.damage(1, entity, p -> TrinketsApi.onTrinketBroken(stack, slotReference, entity));
            }

            entity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
        }
    }
}
