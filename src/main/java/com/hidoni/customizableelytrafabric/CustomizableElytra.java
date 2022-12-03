package com.hidoni.customizableelytrafabric;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CustomizableElytra implements ModInitializer {
    public static String MOD_ID = "customizableelytra";
    public static boolean caleusLoaded = false;

    @Override
    public void onInitialize() {
        caleusLoaded = FabricLoader.getInstance().isModLoaded("caelus");
        ModItems.register();
        ModRecipes.register();
    }
}
