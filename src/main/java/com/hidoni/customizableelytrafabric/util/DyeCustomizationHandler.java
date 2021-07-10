package com.hidoni.customizableelytrafabric.util;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DyeCustomizationHandler extends CustomizationHandler
{
    private final int color;

    public DyeCustomizationHandler(ItemStack itemIn)
    {
        this(itemIn.getOrCreateTag());
    }

    public DyeCustomizationHandler(NbtCompound tagIn)
    {
        super(tagIn.getBoolean("HideCapePattern"));
        NbtCompound childTag = tagIn.getCompound("display");
        this.color = childTag.contains("color", 99) ? childTag.getInt("color") : 16777215;
    }

    @NotNull
    public static List<Float> getColors(int color)
    {
        ArrayList<Float> colorOut = new ArrayList<>();
        float redValue = (float) (color >> 16 & 255) / 255.0F;
        float greenValue = (float) (color >> 8 & 255) / 255.0F;
        float blueValue = (float) (color & 255) / 255.0F;
        colorOut.add(redValue);
        colorOut.add(greenValue);
        colorOut.add(blueValue);
        return colorOut;
    }

    @Override
    public int getColor(int index)
    {
        return color;
    }

    @Override
    public <T extends LivingEntity, M extends AnimalModel<T>> void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, Identifier textureLocation, boolean hasGlint)
    {
        List<Float> colors = getColors(color);
        renderModel.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorGlintConsumer(bufferIn, RenderLayer.getArmorCutoutNoCull(textureLocation), false, hasGlint);
        renderModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, colors.get(0), colors.get(1), colors.get(2), 1.0F);
    }
}
