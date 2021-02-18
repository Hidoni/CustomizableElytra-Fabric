package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.client.render.CustomizableElytraFeatureRenderer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>
{
    public ArmorStandEntityRendererMixin(EntityRenderDispatcher dispatcher, ArmorStandArmorEntityModel model, float shadowRadius)
    {
        super(dispatcher, model, shadowRadius);
    }

    @Inject(at=@At("RETURN"), method= "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V")
    private void postConstructor(CallbackInfo callbackInfo)
    {
        this.addFeature(new CustomizableElytraFeatureRenderer<>(this));
    }
}
