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
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CustomizableElytra implements ClientModInitializer {
    public static String MOD_ID = "customizableelytra";

    @Override
    public void onInitializeClient() {
        registerModelPredicates();
        registerColorProviders();
        registerElytraBannerPatterns();
        registerFeatureRendererEventHandlers();
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

    private void registerElytraBannerPatterns() {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) ->
        {
            for (BannerPattern pattern : BannerPattern.values()) {
                Identifier textureIdentifier = CustomizableElytraItem.getTextureLocation(pattern);
                registry.register(textureIdentifier);
            }
        }));
    }

    private void registerFeatureRendererEventHandlers() {
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((entity) -> {
            ItemStack stack = CustomizableElytraFeatureRenderer.tryFindElytra(entity);
            return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA;
        });
    }
}
