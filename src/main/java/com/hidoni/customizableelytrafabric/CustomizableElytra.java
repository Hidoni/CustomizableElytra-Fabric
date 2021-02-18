package com.hidoni.customizableelytrafabric;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class CustomizableElytra implements ModInitializer
{
    public static String MOD_ID = "customizableelytra";
    @Override
    public void onInitialize()
    {
        ModItems.register();
    }
}
