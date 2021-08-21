package com.hidoni.customizableelytrafabric.integration.trinkets;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.UUID;

public class CustomizableElytraTrinket implements Trinket
{
    private static final EntityAttributeModifier FLIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("a34b6e10-c28d-4a11-b131-ae54b505298b"), "Customizable elytra trinket flight", 1.0D, EntityAttributeModifier.Operation.ADDITION);

    @Override
    public boolean canWearInSlot(String group, String slot)
    {
        return group.equals(SlotGroups.CHEST) && slot.equals(Slots.CAPE);
    }

    @Override
    public void tick(PlayerEntity player, ItemStack stack)
    {
        int i = player.getRoll() + 1;
        if (!player.world.isClient && i % 10 == 0)
        {
            int j = i / 10;
            if (j % 2 == 0)
            {
                stack.damage(1, player, (playerEntity) -> {});
            }
        }

        EntityAttributeInstance flightAttributeInstance = player.getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);
        if (CustomizableElytraItem.isUsable(stack))
        {
            if (flightAttributeInstance != null && !flightAttributeInstance.hasModifier(FLIGHT_MODIFIER))
            {
                flightAttributeInstance.addTemporaryModifier(FLIGHT_MODIFIER);
            }
        }
        else
        {
            if (flightAttributeInstance != null)
            {
                flightAttributeInstance.removeModifier(FLIGHT_MODIFIER);
            }
        }
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack)
    {
        EntityAttributeInstance flightAttributeInstance = player.getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);
        if (flightAttributeInstance != null)
        {
            flightAttributeInstance.removeModifier(FLIGHT_MODIFIER);
        }
    }
}
