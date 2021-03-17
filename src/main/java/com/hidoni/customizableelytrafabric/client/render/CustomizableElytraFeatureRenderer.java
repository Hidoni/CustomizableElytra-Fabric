package com.hidoni.customizableelytrafabric.client.render;

import com.hidoni.customizableelytrafabric.client.CustomizableElytra;
import com.hidoni.customizableelytrafabric.client.render.model.ElytraWingModel;
import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomizableElytraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraFeatureRenderer<T, M>
{
    private final ElytraEntityModel<T> modelElytra = new ElytraEntityModel<>();
    private final ElytraWingModel<T> leftWing = new ElytraWingModel<>(false);
    private final ElytraWingModel<T> rightWing = new ElytraWingModel<>(true);
    private static final Identifier TEXTURE_DYEABLE_ELYTRA = new Identifier(CustomizableElytra.MOD_ID, "textures/entity/elytra.png");

    public CustomizableElytraFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext)
    {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l)
    {
        ItemStack elytra = tryFindElytra(livingEntity);
        if (elytra != ItemStack.EMPTY)
        {
            if (elytra.getSubTag("WingInfo") != null)
            {
                renderSplit(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra);
            }
            else
            {
                CompoundTag blockEntityTag = elytra.getSubTag("BlockEntityTag");
                if (blockEntityTag == null)
                {
                    renderDyed(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra);
                }
                else
                {
                    renderBanner(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra);
                }
            }
        }
    }

    public void renderDyed(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra)
    {
        List<Float> colors = getColors(elytra);
        if (colors != null)
        {
            Identifier elytraTexture;
            if (livingEntity instanceof AbstractClientPlayerEntity)
            {
                AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) livingEntity;
                if (abstractclientplayerentity.canRenderElytraTexture() && abstractclientplayerentity.getElytraTexture() != null)
                {
                    elytraTexture = abstractclientplayerentity.getElytraTexture();
                }
                else if (abstractclientplayerentity.canRenderCapeTexture() && abstractclientplayerentity.getCapeTexture() != null && abstractclientplayerentity.isPartVisible(PlayerModelPart.CAPE))
                {
                    elytraTexture = abstractclientplayerentity.getCapeTexture();
                }
                else
                {
                    elytraTexture = getElytraTexture(elytra, livingEntity);
                }
            }
            else
            {
                elytraTexture = getElytraTexture(elytra, livingEntity);
            }

            matrixStack.push();
            matrixStack.translate(0.0D, 0.0D, 0.125D);
            this.getContextModel().copyStateTo(this.modelElytra);
            this.modelElytra.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer glintConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(elytraTexture), false, elytra.hasGlint());
            this.modelElytra.render(matrixStack, glintConsumer, i, OverlayTexture.DEFAULT_UV, colors.get(0), colors.get(1), colors.get(2), 1.0F);
            matrixStack.pop();
        }
    }

    public void renderBanner(MatrixStack matrixStackIn, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra)
    {
        Identifier elytraTexture = getElytraTexture(elytra, livingEntity);
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.0D, 0.125D);
        this.getContextModel().copyStateTo(this.modelElytra);
        this.modelElytra.setAngles(livingEntity, f, g, j, k, l);
        VertexConsumer glintConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(elytraTexture), false, elytra.hasGlint());
        this.modelElytra.render(matrixStackIn, glintConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        List<com.mojang.datafixers.util.Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(ShieldItem.getColor(elytra), BannerBlockEntity.getPatternListTag(elytra));

        for (int count = 0; count < 17 && count < list.size(); ++count)
        {
            Pair<BannerPattern, DyeColor> pair = list.get(count);
            float[] afloat = pair.getSecond().getColorComponents();
            SpriteIdentifier rendermaterial = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, CustomizableElytraItem.getTextureLocation(pair.getFirst()));
            this.modelElytra.render(matrixStackIn, rendermaterial.getVertexConsumer(vertexConsumerProvider, RenderLayer::getArmorCutoutNoCull), i, OverlayTexture.DEFAULT_UV, afloat[0], afloat[1], afloat[2], 1.0F);
        }
        matrixStackIn.pop();
    }

    public void renderSplit(MatrixStack matrixStackIn, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra)
    {
        CompoundTag wingInfo = elytra.getSubTag("WingInfo");
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.0D, 0.125D);
        if (wingInfo.contains("left"))
        {
            CompoundTag wing = wingInfo.getCompound("left");
            if (wing.contains("color"))
            {
                renderSplitDyed(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, leftWing, wing.getInt("color"));
            }
            else if (wing.contains("Patterns"))
            {
                renderSplitBanner(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, leftWing, DyeColor.byId(wing.getInt("Base")), wing.getList("Patterns", 10).copy());
            }
            else
            {
                renderSplitFallback(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, leftWing);
            }
        }
        else
        {
            renderSplitFallback(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, leftWing);
        }
        if (wingInfo.contains("right"))
        {
            CompoundTag wing = wingInfo.getCompound("right");
            if (wing.contains("color"))
            {
                renderSplitDyed(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, rightWing, wing.getInt("color"));
            }
            else if (wing.contains("Patterns"))
            {
                renderSplitBanner(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, rightWing, DyeColor.byId(wing.getInt("Base")), wing.getList("Patterns", 10).copy());
            }
            else
            {
                renderSplitFallback(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, rightWing);
            }
        }
        else
        {
            renderSplitFallback(matrixStackIn, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytra, rightWing);
        }
        matrixStackIn.pop();
    }

    public void renderSplitFallback(MatrixStack matrixStackIn, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra, ElytraWingModel<T> wingIn)
    {
        Identifier elytraTexture;
        if (livingEntity instanceof AbstractClientPlayerEntity)
        {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) livingEntity;
            if (abstractclientplayerentity.canRenderElytraTexture() && abstractclientplayerentity.getElytraTexture() != null)
            {
                elytraTexture = abstractclientplayerentity.getElytraTexture();
            }
            else if (abstractclientplayerentity.canRenderCapeTexture() && abstractclientplayerentity.getCapeTexture() != null && abstractclientplayerentity.isPartVisible(PlayerModelPart.CAPE))
            {
                elytraTexture = abstractclientplayerentity.getCapeTexture();
            }
            else
            {
                elytraTexture = getElytraTexture(elytra, livingEntity);
            }
        }
        else
        {
            elytraTexture = getElytraTexture(elytra, livingEntity);
        }

        this.getContextModel().copyStateTo(wingIn);
        wingIn.setAngles(livingEntity, f, g, j, k, l);
        VertexConsumer glintConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(elytraTexture), false, elytra.hasGlint());
        wingIn.render(matrixStackIn, glintConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderSplitDyed(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra, ElytraWingModel<T> wingIn, int color)
    {
        List<Float> colors = getColors(color);
        Identifier elytraTexture;
        if (livingEntity instanceof AbstractClientPlayerEntity)
        {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) livingEntity;
            if (abstractclientplayerentity.canRenderElytraTexture() && abstractclientplayerentity.getElytraTexture() != null)
            {
                elytraTexture = abstractclientplayerentity.getElytraTexture();
            }
            else if (abstractclientplayerentity.canRenderCapeTexture() && abstractclientplayerentity.getCapeTexture() != null && abstractclientplayerentity.isPartVisible(PlayerModelPart.CAPE))
            {
                elytraTexture = abstractclientplayerentity.getCapeTexture();
            }
            else
            {
                elytraTexture = getElytraTexture(elytra, livingEntity);
            }
        }
        else
        {
            elytraTexture = getElytraTexture(elytra, livingEntity);
        }

        this.getContextModel().copyStateTo(wingIn);
        wingIn.setAngles(livingEntity, f, g, j, k, l);
        VertexConsumer glintConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(elytraTexture), false, elytra.hasGlint());
        wingIn.render(matrixStack, glintConsumer, i, OverlayTexture.DEFAULT_UV, colors.get(0), colors.get(1), colors.get(2), 1.0F);
    }

    public void renderSplitBanner(MatrixStack matrixStackIn, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, ItemStack elytra, ElytraWingModel<T> wingIn, DyeColor base, ListTag patterns)
    {
        Identifier elytraTexture = getElytraTexture(elytra, livingEntity);
        this.getContextModel().copyStateTo(wingIn);
        wingIn.setAngles(livingEntity, f, g, j, k, l);
        VertexConsumer glintConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(elytraTexture), false, elytra.hasGlint());
        wingIn.render(matrixStackIn, glintConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        List<com.mojang.datafixers.util.Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(base, patterns);

        for (int count = 0; count < 17 && count < list.size(); ++count)
        {
            Pair<BannerPattern, DyeColor> pair = list.get(count);
            float[] afloat = pair.getSecond().getColorComponents();
            SpriteIdentifier rendermaterial = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, CustomizableElytraItem.getTextureLocation(pair.getFirst()));
            wingIn.render(matrixStackIn, rendermaterial.getVertexConsumer(vertexConsumerProvider, RenderLayer::getArmorCutoutNoCull), i, OverlayTexture.DEFAULT_UV, afloat[0], afloat[1], afloat[2], 1.0F);
        }
    }

    public boolean shouldRender(ItemStack stack, LivingEntity entity)
    {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA;
    }

    public Identifier getElytraTexture(ItemStack stack, T entity)
    {
        return TEXTURE_DYEABLE_ELYTRA;
    }

    public List<Float> getColors(ItemStack elytraIn)
    {
        ArrayList<Float> colorOut = new ArrayList<>();
        if (elytraIn.getItem() == ModItems.CUSTOMIZABLE_ELYTRA)
        {
            int color = ((CustomizableElytraItem)elytraIn.getItem()).getColor(elytraIn);
            float redValue = (float) (color >> 16 & 255) / 255.0F;
            float greenValue = (float) (color >> 8 & 255) / 255.0F;
            float blueValue = (float) (color & 255) / 255.0F;
            colorOut.add(redValue);
            colorOut.add(greenValue);
            colorOut.add(blueValue);
            return colorOut;
        }
        return null;
    }

    @NotNull
    public List<Float> getColors(int color)
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

    public ItemStack getColytraSubItem(ItemStack stack)
    {
        CompoundTag colytraChestTag = stack.getSubTag("colytra:ElytraUpgrade");
        if (colytraChestTag != null)
        {
            ItemStack elytraStack = ItemStack.fromTag(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA)
            {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    /*public ItemStack getCurioElytra(LivingEntity entity)
    {
        Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.CUSTOMIZABLE_ELYTRA.get(), entity);
        if (curio.isPresent())
        {
            return curio.get().getRight();
        }
        return ItemStack.EMPTY;
    }*/

    public ItemStack tryFindElytra(LivingEntity entity)
    {
        ItemStack elytra = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (shouldRender(elytra, entity))
        {
            return elytra;
        }
        if (com.hidoni.customizableelytrafabric.CustomizableElytra.caleusLoaded)
        {
            elytra = getColytraSubItem(elytra);
            if (elytra != ItemStack.EMPTY)
            {
                return elytra;
            }
            /*if (CustomizableElytra.curiosLoaded)
            {
                return getCurioElytra(entity);
            }*/
            if (com.hidoni.customizableelytrafabric.CustomizableElytra.trinketsLoaded && entity instanceof PlayerEntity)
            {
                elytra = TrinketsApi.getTrinketComponent((PlayerEntity) entity).getStack("chest", "cape");
                if (elytra.getItem() == ModItems.CUSTOMIZABLE_ELYTRA)
                {
                    return elytra;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
