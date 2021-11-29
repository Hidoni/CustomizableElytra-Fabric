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
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.List;

public class CustomizableElytraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraFeatureRenderer<T, M> {
    private static final Identifier TEXTURE_DYEABLE_ELYTRA = new Identifier(CustomizableElytra.MOD_ID, "textures/entity/elytra.png");
    private final ElytraWingModel<T> leftWing = new ElytraWingModel<>(false);
    private final ElytraWingModel<T> rightWing = new ElytraWingModel<>(true);

    public CustomizableElytraFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public static boolean shouldRender(ItemStack stack, LivingEntity entity) {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA;
    }

    public static ItemStack getColytraSubItem(ItemStack stack) {
        CompoundTag colytraChestTag = stack.getSubTag("colytra:ElytraUpgrade");
        if (colytraChestTag != null) {
            ItemStack elytraStack = ItemStack.fromTag(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA) {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack tryFindElytra(LivingEntity entity) {
        ItemStack elytra = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (shouldRender(elytra, entity)) {
            return elytra;
        }
        if (com.hidoni.customizableelytrafabric.CustomizableElytra.caleusLoaded) {
            elytra = getColytraSubItem(elytra);
            if (elytra != ItemStack.EMPTY) {
                return elytra;
            }
        }
        if (com.hidoni.customizableelytrafabric.CustomizableElytra.trinketsLoaded && entity instanceof PlayerEntity) {
            elytra = TrinketsApi.getTrinketComponent((PlayerEntity) entity).getStack("chest", "cape");
            if (elytra.getItem() == ModItems.CUSTOMIZABLE_ELYTRA) {
                return elytra;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack elytra = tryFindElytra(livingEntity);
        if (elytra != ItemStack.EMPTY) {
            matrixStack.push();
            matrixStack.translate(0.0D, 0.0D, 0.125D);
            ElytraCustomizationData data = ElytraCustomizationUtil.getData(elytra);
            if (data.type != ElytraCustomizationData.CustomizationType.Split) {
                ElytraEntityModel<T> elytraModel = ((ElytraFeatureRendererAccessor<T>) this).getElytraModel();
                this.getContextModel().copyStateTo(elytraModel);
                Identifier elytraTexture = getTextureWithCape(livingEntity, elytra.getTag(), data.handler.isWingCapeHidden(0));
                data.handler.render(matrixStack, vertexConsumerProvider, light, livingEntity, f, g, h, j, k, l, elytraModel, elytraTexture, elytra.hasGlint());
            } else {
                List<ElytraWingModel<T>> models = ImmutableList.of(leftWing, rightWing);
                for (ElytraWingModel<T> model : models) {
                    this.getContextModel().copyStateTo(model);
                }
                CompoundTag wingInfo = elytra.getSubTag("WingInfo");
                Identifier leftWingTexture = getTextureWithCape(livingEntity, wingInfo.getCompound("left"), data.handler.isWingCapeHidden(0));
                Identifier rightWingTexture = getTextureWithCape(livingEntity, wingInfo.getCompound("right"), data.handler.isWingCapeHidden(1));
                ((SplitCustomizationHandler) data.handler).render(matrixStack, vertexConsumerProvider, light, livingEntity, f, g, h, j, k, l, models, leftWingTexture, rightWingTexture, elytra.hasGlint());
            }
            matrixStack.pop();
        }
    }

    private Identifier getTextureWithCape(T livingEntity, CompoundTag customizationTag, boolean capeHidden) {
        Identifier elytraTexture = null;
        boolean isGrayscaleTexture = ElytraCustomizationUtil.getData(customizationTag).type != ElytraCustomizationData.CustomizationType.None;
        if (!capeHidden && livingEntity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) livingEntity;
            if (abstractclientplayerentity.canRenderElytraTexture() && abstractclientplayerentity.getElytraTexture() != null) {
                elytraTexture = abstractclientplayerentity.getElytraTexture();
            } else if (abstractclientplayerentity.canRenderCapeTexture() && abstractclientplayerentity.getCapeTexture() != null && abstractclientplayerentity.isPartVisible(PlayerModelPart.CAPE)) {
                elytraTexture = abstractclientplayerentity.getCapeTexture();
            }
        }
        if (elytraTexture == null) {
            elytraTexture = getElytraTexture(isGrayscaleTexture);
        } else if (isGrayscaleTexture) {
            elytraTexture = ElytraTextureUtil.getGrayscale(elytraTexture);
        }
        return elytraTexture;
    }

    public Identifier getElytraTexture(boolean isGrayscaleTexture) {
        if (isGrayscaleTexture) {
            return TEXTURE_DYEABLE_ELYTRA;
        }
        return ((ElytraFeatureRendererAccessor<T>) this).getElytraTexture();
    }
}
