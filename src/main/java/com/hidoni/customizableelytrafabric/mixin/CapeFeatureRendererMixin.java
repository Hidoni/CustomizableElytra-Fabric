package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.registry.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin
{
    @Inject(at = @At(value = "HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", cancellable = true)
    public void preRenderCustomizableElytraCheck(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci)
    {
        ItemStack stack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA)
        {
            ci.cancel();
        }
    }
}
