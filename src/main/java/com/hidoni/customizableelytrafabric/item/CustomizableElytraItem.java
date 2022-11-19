package com.hidoni.customizableelytrafabric.item;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationData;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements DyeableItem, FabricElytraItem {
    public final static String LEFT_WING_TRANSLATION_KEY = "item.customizableelytra.left_wing";
    public final static String RIGHT_WING_TRANSLATION_KEY = "item.customizableelytra.right_wing";
    public final static String HIDDEN_CAPE_TRANSLATION_KEY = "item.customizableelytra.cape_hidden";
    public final static String GLOWING_WING_TRANSLATION_KEY = "item.customizableelytra.glowing_wing";

    public CustomizableElytraItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public static Identifier getTextureLocation(RegistryKey<BannerPattern> bannerIn) {
        return new Identifier(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.getValue().getPath());
    }

    @Override
    public int getColor(ItemStack stack) {
        return getColor(stack, 0);
    }

    public int getColor(ItemStack stack, int index) {
        return ElytraCustomizationUtil.getData(stack).handler.getColor(index);
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        NbtCompound bannerTag = stack.getSubNbt("BlockEntityTag");
        NbtCompound wingTag = stack.getSubNbt("WingInfo");
        return DyeableItem.super.hasColor(stack) || bannerTag != null || wingTag != null || stack.getOrCreateNbt().getInt("WingLightLevel") > 0 || stack.getOrCreateNbt().getBoolean("HideCapePattern");
    }

    @Override
    public void removeColor(ItemStack stack) {
        DyeableItem.super.removeColor(stack);
        NbtCompound compoundTag = stack.getSubNbt("BlockEntityTag");
        if (compoundTag != null) {
            stack.removeSubNbt("BlockEntityTag");
        }
        compoundTag = stack.getSubNbt("WingInfo");
        if (compoundTag != null) {
            stack.removeSubNbt("WingInfo");
        }
        stack.getOrCreateNbt().remove("HideCapePattern");
        stack.getOrCreateNbt().remove("WingLightLevel");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        applyTooltip(tooltip, context, stack.getOrCreateNbt(), true);
        NbtCompound wingInfo = stack.getSubNbt("WingInfo");
        if (wingInfo != null) {
            if (wingInfo.contains("left")) {
                NbtCompound leftWing = wingInfo.getCompound("left");
                ElytraCustomizationData leftWingData = ElytraCustomizationUtil.getData(leftWing);
                if (leftWingData.handler.isModified()) {
                    tooltip.add(Text.translatable(LEFT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                    applyTooltip(tooltip, context, leftWing);
                }
            }
            if (wingInfo.contains("right")) {
                NbtCompound rightWing = wingInfo.getCompound("right");
                ElytraCustomizationData rightWingData = ElytraCustomizationUtil.getData(rightWing);
                if (rightWingData.handler.isModified()) {
                    tooltip.add(Text.translatable(RIGHT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                    applyTooltip(tooltip, context, rightWing);
                }
            }
        }
    }

    @Override
    public String getTranslationKey() {
        return Items.ELYTRA.getTranslationKey();
    }

    public static void applyTooltip(List<Text> tooltip, TooltipContext context, NbtCompound wingIn) {
        applyTooltip(tooltip, context, wingIn, false);
    }

    public static void applyTooltip(List<Text> tooltip, TooltipContext context, NbtCompound wingIn, boolean ignoreDisplayTag) {
        NbtCompound wing = ElytraCustomizationUtil.migrateOldSplitWingFormat(wingIn);
        if (wing.getBoolean("HideCapePattern")) {
            tooltip.add(Text.translatable(HIDDEN_CAPE_TRANSLATION_KEY).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        if (wing.getInt("WingLightLevel") > 0) {
            tooltip.add(Text.translatable(GLOWING_WING_TRANSLATION_KEY).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        if (!ignoreDisplayTag && wing.contains("display")) {
            NbtCompound displayTag = wing.getCompound("display");
            if (context.isAdvanced()) {
                tooltip.add((Text.translatable("item.color", String.format("#%06X", displayTag.getInt("color")))).formatted(Formatting.GRAY));
            } else {
                tooltip.add((Text.translatable("item.dyed")).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        } else if (wing.contains("BlockEntityTag")) {
            NbtCompound blockEntityTag = wing.getCompound("BlockEntityTag");
            int baseColor = blockEntityTag.getInt("Base");
            tooltip.add((Text.translatable("block.minecraft.banner." + BannerPatterns.BASE.getValue().getPath() + '.' + DyeColor.byId(baseColor).getName())).formatted(Formatting.GRAY));
            NbtList listnbt = blockEntityTag.getList("Patterns", 10);

            for (int i = 0; i < listnbt.size() && i < 6; ++i) {
                NbtCompound patternNBT = listnbt.getCompound(i);
                DyeColor dyeColor = DyeColor.byId(patternNBT.getInt("Color"));
                RegistryEntry<BannerPattern> registryEntry = BannerPattern.byId(patternNBT.getString("Pattern"));
                if (registryEntry != null) {
                    registryEntry.getKey().map((key) -> key.getValue().toShortTranslationKey()).ifPresent((translationKey) -> {
                        Identifier identifier = new Identifier(translationKey);
                        tooltip.add(Text.translatable("block." + identifier.getNamespace() +".banner." + identifier.getPath() + "." + dyeColor.getName()).formatted(Formatting.GRAY));
                    });
                }
            }
        }
    }
}
