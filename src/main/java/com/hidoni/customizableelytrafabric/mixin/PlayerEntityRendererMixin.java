package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.client.render.CustomizableElytraFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{
    public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius)
    {
        super(dispatcher, model, shadowRadius);
    }

    @Inject(at=@At("RETURN"), method= "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V")
    private void postConstructor(CallbackInfo callbackInfo)
    {
        this.addFeature(new CustomizableElytraFeatureRenderer<>(this));
    }
}
