package com.hidoni.customizableelytrafabric.registry;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.recipe.*;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static SpecialRecipeSerializer<ElytraDyeRecipe> ELYTRA_DYE_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_dye_recipe"), new SpecialRecipeSerializer<>(ElytraDyeRecipe::new));
    public static SpecialRecipeSerializer<ElytraBannerRecipe> ELYTRA_BANNER_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_banner_recipe"), new SpecialRecipeSerializer<>(ElytraBannerRecipe::new));
    public static SpecialRecipeSerializer<ElytraWingCombinationRecipe> ELYTRA_WING_COMBINATION_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_wing_combination_recipe"), new SpecialRecipeSerializer<>(ElytraWingCombinationRecipe::new));
    public static SpecialRecipeSerializer<ElytraToVanillaRecipe> ELYTRA_TO_VANILLA_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_to_vanilla_recipe"), new SpecialRecipeSerializer<>(ElytraToVanillaRecipe::new));
    public static SpecialRecipeSerializer<ElytraHideCapeRecipe> ELYTRA_HIDE_CAPE_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_hide_cape_recipe"), new SpecialRecipeSerializer<>(ElytraHideCapeRecipe::new));

    public static void register() {

    }
}
