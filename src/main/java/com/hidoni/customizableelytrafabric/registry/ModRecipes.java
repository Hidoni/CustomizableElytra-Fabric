package com.hidoni.customizableelytrafabric.registry;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.recipe.ElytraBannerRecipe;
import com.hidoni.customizableelytrafabric.recipe.ElytraDyeRecipe;
import com.hidoni.customizableelytrafabric.recipe.ElytraWingCombinationRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes
{
    public static SpecialRecipeSerializer<ElytraDyeRecipe> ELYTRA_DYE_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_dye_recipe"), new SpecialRecipeSerializer<>(ElytraDyeRecipe::new));
    public static SpecialRecipeSerializer<ElytraBannerRecipe> ELYTRA_BANNER_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_banner_recipe"), new SpecialRecipeSerializer<>(ElytraBannerRecipe::new));
    public static SpecialRecipeSerializer<ElytraWingCombinationRecipe> ELYTRA_WING_COMBINATION_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CustomizableElytra.MOD_ID, "elytra_wing_combination_recipe"), new SpecialRecipeSerializer<>(ElytraWingCombinationRecipe::new));

    public static void register()
    {

    }
}
