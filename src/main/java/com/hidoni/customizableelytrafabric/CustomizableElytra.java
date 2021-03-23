package com.hidoni.customizableelytrafabric;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.registry.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CustomizableElytra implements ModInitializer
{
    public static String MOD_ID = "customizableelytra";
    public static boolean caleusLoaded = false;
    public static boolean curiosLoaded = false;
    public static boolean trinketsLoaded = false;

    @Override
    public void onInitialize()
    {
        caleusLoaded = FabricLoader.getInstance().isModLoaded("caelus");
        // curiosLoaded = FabricLoader.getInstance().isModLoaded("curios"); TODO: Implement custom curio class to prevent Curious Elytra from rendering a vanilla elytra at the same time, until then, disabled.
        trinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        ModItems.register();
        ModRecipes.register();
    }
}
