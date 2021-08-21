package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.client.render.CustomizableElytraFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, new PlayerEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V")
    private void postConstructor(EntityRendererFactory.Context context, boolean slim, CallbackInfo callbackInfo) {
        this.addFeature(new CustomizableElytraFeatureRenderer<>(this, context.getModelLoader()));
    }
}
