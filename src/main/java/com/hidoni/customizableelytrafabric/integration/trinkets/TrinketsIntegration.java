package com.hidoni.customizableelytrafabric.integration.trinkets;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import dev.emi.trinkets.api.TrinketsApi;

public class TrinketsIntegration
{
    public static void register_trinkets()
    {
        TrinketsApi.registerTrinket(ModItems.CUSTOMIZABLE_ELYTRA, new CustomizableElytraTrinket());
    }
}
