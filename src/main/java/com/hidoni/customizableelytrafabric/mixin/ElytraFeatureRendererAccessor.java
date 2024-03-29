package com.hidoni.customizableelytrafabric.mixin;

import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ElytraFeatureRenderer.class)
public interface ElytraFeatureRendererAccessor<T extends LivingEntity> {
    @Accessor("elytra")
    ElytraEntityModel<T> getElytraModel();

    @Accessor("SKIN")
    Identifier getElytraTexture();
}
