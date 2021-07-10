package com.hidoni.customizableelytrafabric.client.render;

import com.google.common.collect.ImmutableList;
import com.hidoni.customizableelytrafabric.client.CustomizableElytra;
import com.hidoni.customizableelytrafabric.client.render.model.ElytraWingModel;
import com.hidoni.customizableelytrafabric.mixin.ElytraFeatureRendererAccessor;
import com.hidoni.customizableelytrafabric.registry.ModItems;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationData;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationUtil;
import com.hidoni.customizableelytrafabric.util.ElytraTextureUtil;
import com.hidoni.customizableelytrafabric.util.SplitCustomizationHandler;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

public class CustomizableElytraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraFeatureRenderer<T, M>
{
    private static final Identifier TEXTURE_DYEABLE_ELYTRA = new Identifier(CustomizableElytra.MOD_ID, "textures/entity/elytra.png");
    private final ElytraWingModel<T> leftWing;
    private final ElytraWingModel<T> rightWing;

    public CustomizableElytraFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, EntityModelLoader loader)
    {
        super(featureRendererContext, loader);
        this.leftWing = new ElytraWingModel<>(loader.getModelPart(EntityModelLayers.ELYTRA), false);
        this.rightWing = new ElytraWingModel<>(loader.getModelPart(EntityModelLayers.ELYTRA), true);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l)
    {
        ItemStack elytra = tryFindElytra(livingEntity);
        if (elytra != ItemStack.EMPTY)
        {
            matrixStack.push();
            matrixStack.translate(0.0D, 0.0D, 0.125D);
            ElytraCustomizationData data = ElytraCustomizationUtil.getData(elytra);
            if (data.type != ElytraCustomizationData.CustomizationType.Split)
            {
                ElytraEntityModel<T> elytraModel = ((ElytraFeatureRendererAccessor<T>) this).getElytraModel();
                this.getContextModel().copyStateTo(elytraModel);
                data.handler.render(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, elytraModel, getTextureWithCape(livingEntity, elytra), elytra.hasGlint());
            }
            else
            {
                List<ElytraWingModel<T>> models = ImmutableList.of(leftWing, rightWing);
                for (ElytraWingModel<T> model : models)
                {
                    this.getContextModel().copyStateTo(model);
                }
                ((SplitCustomizationHandler) data.handler).render(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, models, getTextureWithCape(livingEntity, elytra), elytra.hasGlint());
            }
            matrixStack.pop();
        }
    }

    private Identifier getTextureWithCape(T livingEntity, ItemStack elytra)
    {
        Identifier elytraTexture;
        if (livingEntity instanceof AbstractClientPlayerEntity)
        {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) livingEntity;
            if (abstractclientplayerentity.canRenderElytraTexture() && abstractclientplayerentity.getElytraTexture() != null)
            {
                elytraTexture = ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getElytraTexture());
            }
            else if (abstractclientplayerentity.canRenderCapeTexture() && abstractclientplayerentity.getCapeTexture() != null && abstractclientplayerentity.isPartVisible(PlayerModelPart.CAPE))
            {
                elytraTexture = ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getCapeTexture());
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
        return elytraTexture;
    }

    public static boolean shouldRender(ItemStack stack, LivingEntity entity)
    {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA;
    }

    public Identifier getElytraTexture(ItemStack stack, T entity)
    {
        return TEXTURE_DYEABLE_ELYTRA;
    }

    public static ItemStack getColytraSubItem(ItemStack stack)
    {
        NbtCompound colytraChestTag = stack.getSubTag("colytra:ElytraUpgrade");
        if (colytraChestTag != null)
        {
            ItemStack elytraStack = ItemStack.fromNbt(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA)
            {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack tryFindElytra(LivingEntity entity)
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
        }
        if (com.hidoni.customizableelytrafabric.CustomizableElytra.trinketsLoaded && entity instanceof PlayerEntity)
        {
            Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent((PlayerEntity) entity);
            if (trinketComponent.isPresent())
            {
                List<Pair<SlotReference, ItemStack>> equipped_elytra = trinketComponent.get().getEquipped(ModItems.CUSTOMIZABLE_ELYTRA);
                if (!equipped_elytra.isEmpty())
                {
                    return equipped_elytra.get(0).getRight();
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
