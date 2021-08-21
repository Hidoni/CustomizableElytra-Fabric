package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.client.render.CustomizableElytraFeatureRenderer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {
    public ArmorStandEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx, new ArmorStandEntityModel(ctx.getPart(EntityModelLayers.ARMOR_STAND)), 0.0F);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V")
    private void postConstructor(EntityRendererFactory.Context context, CallbackInfo callbackInfo) {
        this.addFeature(new CustomizableElytraFeatureRenderer<>(this, context.getModelLoader()));
    }
}
