package com.hidoni.customizableelytrafabric.client;

import com.hidoni.customizableelytrafabric.client.render.CustomizableElytraFeatureRenderer;
import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import com.hidoni.customizableelytrafabric.item.ElytraWingItem;
import com.hidoni.customizableelytrafabric.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CustomizableElytra implements ClientModInitializer {
    public static String MOD_ID = "customizableelytra";

    @Override
    public void onInitializeClient() {
        registerModelPredicates();
        registerColorProviders();
        registerFeatureRendererEventHandlers();
        registerFeatureRenderers();
    }

    private void registerModelPredicates() {
        FabricModelPredicateProviderRegistry.register(ModItems.CUSTOMIZABLE_ELYTRA, new Identifier(MOD_ID, "broken_elytra"), ((stack, world, entity, seed) ->
                stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA && CustomizableElytraItem.isUsable(stack) ? 0.0F : 1.0F));
    }

    private void registerColorProviders() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                ((CustomizableElytraItem) stack.getItem()).getColor(stack, tintIndex), ModItems.CUSTOMIZABLE_ELYTRA);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                tintIndex > 0 ? -1 : ((ElytraWingItem) stack.getItem()).getColor(stack), ModItems.ELYTRA_WING);
    }

    private void registerFeatureRendererEventHandlers() {
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((entity) -> {
            ItemStack stack = CustomizableElytraFeatureRenderer.tryFindElytra(entity);
            return stack.getItem() != ModItems.CUSTOMIZABLE_ELYTRA;
        });
    }

    private void registerFeatureRenderers() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof ArmorStandEntityRenderer || entityRenderer instanceof PlayerEntityRenderer) {
                registrationHelper.register(new CustomizableElytraFeatureRenderer<>(entityRenderer, context.getModelLoader()));
            }
        });
    }
}
