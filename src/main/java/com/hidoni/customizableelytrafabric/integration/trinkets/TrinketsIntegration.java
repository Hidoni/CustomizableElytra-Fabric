package com.hidoni.customizableelytrafabric.integration.trinkets;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.List;

public class TrinketsIntegration {
    public static void register_trinkets() {
        TrinketsApi.registerTrinket(ModItems.CUSTOMIZABLE_ELYTRA, new CustomizableElytraTrinket());
        EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
            TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(entity).orElse(null);
            if (trinketComponent != null) {
                List<Pair<SlotReference, ItemStack>> equipped = trinketComponent.getEquipped(ModItems.CUSTOMIZABLE_ELYTRA);
                if (!equipped.isEmpty()) {
                    Pair<SlotReference, ItemStack> slotReferenceItemStackPair = equipped.get(0);
                    return CustomizableElytraTrinket.useCustomElytraTrinket(entity, slotReferenceItemStackPair, tickElytra);
                }
            }
            return false;
        });
    }
}
