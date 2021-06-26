package com.hidoni.customizableelytrafabric.integration.trinkets;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.UUID;

public class CustomizableElytraTrinket implements Trinket
{
    private static final EntityAttributeModifier FLIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("a34b6e10-c28d-4a11-b131-ae54b505298b"), "Customizable elytra trinket flight", 1.0D, EntityAttributeModifier.Operation.ADDITION);

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity)
    {
        int i = entity.getRoll() + 1;
        if (!entity.world.isClient && i % 10 == 0)
        {
            int j = i / 10;
            if (j % 2 == 0)
            {
                stack.damage(1, entity, (player) ->
                {
                    TrinketsApi.onTrinketBroken(stack, slot, entity);
                });
            }

            entity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
        }

        EntityAttributeInstance flightAttributeInstance = entity.getAttributeInstance(CaelusApi.getInstance().getFlightAttribute());
        if (CustomizableElytraItem.isUsable(stack))
        {
            if (flightAttributeInstance != null && !flightAttributeInstance.hasModifier(FLIGHT_MODIFIER))
            {
                flightAttributeInstance.addTemporaryModifier(FLIGHT_MODIFIER);
            }
        }
        else
        {
            flightAttributeInstance.removeModifier(FLIGHT_MODIFIER);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity)
    {
        EntityAttributeInstance flightAttributeInstance = entity.getAttributeInstance(CaelusApi.getInstance().getFlightAttribute());
        if (flightAttributeInstance != null)
        {
            flightAttributeInstance.removeModifier(FLIGHT_MODIFIER);
        }
    }
}
