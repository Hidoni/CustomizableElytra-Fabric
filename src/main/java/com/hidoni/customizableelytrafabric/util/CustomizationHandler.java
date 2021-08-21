package com.hidoni.customizableelytrafabric.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/*
    This is the default handler, if there's no data we'll just render the most basic elytra with the vanilla colors.
 */
public class CustomizationHandler
{
    private final boolean wingCapeHidden;
    private final int wingLightLevel;

    public CustomizationHandler(boolean wingCapeHidden, int wingLightLevel)
    {
        this.wingCapeHidden = wingCapeHidden;
        this.wingLightLevel = wingLightLevel;
    }

    public int getColor(int index)
    {
        return 16777215;
    }

    public boolean isWingCapeHidden(int index) {return wingCapeHidden;}

    public int modifyWingLight(int lightLevel, int index) {
        if (wingLightLevel != 0) {
            lightLevel |= 0xFF;
        }
        return lightLevel;
    }

    @Environment(EnvType.CLIENT)
    public <T extends LivingEntity, M extends AnimalModel<T>> void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, Identifier textureLocation, boolean hasGlint)
    {
        renderModel.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorGlintConsumer(bufferIn, RenderLayer.getArmorCutoutNoCull(textureLocation), false, hasGlint);
        renderModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
