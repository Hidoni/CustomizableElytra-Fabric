package com.hidoni.customizableelytrafabric.util;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

public class BannerCustomizationHandler extends CustomizationHandler
{
    private final List<Pair<BannerPattern, DyeColor>> patterns;

    public BannerCustomizationHandler(ItemStack itemIn)
    {
        patterns = BannerBlockEntity.getPatternsFromNbt(ShieldItem.getColor(itemIn), BannerBlockEntity.getPatternListTag(itemIn));
    }

    public BannerCustomizationHandler(NbtCompound tagIn)
    {
        DyeColor dyeBaseColor = DyeColor.byId(tagIn.getInt("Base"));
        NbtList patternsList = tagIn.getList("Patterns", 10).copy();
        patterns = BannerBlockEntity.getPatternsFromNbt(dyeBaseColor, patternsList);
    }

    @Override
    public int getColor(int index)
    {
        float[] colorComponents = patterns.get(0).getSecond().getColorComponents();
        int red = (int)(colorComponents[0] * 255) << 16;
        int green = (int)(colorComponents[1] * 255) << 8;
        int blue = (int)(colorComponents[2] * 255);
        return red | green | blue;
    }

    @Override
    public <T extends LivingEntity, M extends AnimalModel<T>> void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, Identifier textureLocation, boolean hasGlint)
    {
        renderModel.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = ItemRenderer.getDirectItemGlintConsumer(bufferIn, RenderLayer.getEntityNoOutline(textureLocation), false, hasGlint);
        renderModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 17 && i < patterns.size(); ++i)
        {
            Pair<BannerPattern, DyeColor> pair = patterns.get(i);
            float[] afloat = pair.getSecond().getColorComponents();
            SpriteIdentifier rendermaterial = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, CustomizableElytraItem.getTextureLocation(pair.getFirst()));
            if (rendermaterial.getSprite().getId() != MissingSprite.getMissingSpriteId()) // Don't render this banner pattern if it's missing, silently hide the pattern
            {
                renderModel.render(matrixStackIn, rendermaterial.getVertexConsumer(bufferIn, RenderLayer::getEntityTranslucent), packedLightIn, OverlayTexture.DEFAULT_UV, afloat[0], afloat[1], afloat[2], 1.0F);
            }
        }
    }
}
