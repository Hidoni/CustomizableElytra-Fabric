package com.hidoni.customizableelytrafabric.item;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements DyeableItem {
    public final static String LEFT_WING_TRANSLATION_KEY = "item.customizableelytra.left_wing";
    public final static String RIGHT_WING_TRANSLATION_KEY = "item.customizableelytra.right_wing";
    public final static String HIDDEN_CAPE_TRANSLATION_KEY = "item.customizableelytra.cape_hidden";

    public CustomizableElytraItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public static Identifier getTextureLocation(BannerPattern bannerIn) {
        return new Identifier(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.getName());
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
        CompoundTag bannerTag = stack.getSubTag("BlockEntityTag");
        CompoundTag wingTag = stack.getSubTag("WingInfo");
        return DyeableItem.super.hasColor(stack) || bannerTag != null || wingTag != null || stack.getOrCreateTag().getBoolean("HideCapePattern");
    }

    @Override
    public void removeColor(ItemStack stack) {
        DyeableItem.super.removeColor(stack);
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null) {
            stack.removeSubTag("BlockEntityTag");
        }
        compoundTag = stack.getSubTag("WingInfo");
        if (compoundTag != null) {
            stack.removeSubTag("WingInfo");
        }
        stack.getOrCreateTag().remove("HideCapePattern");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        applyTooltip(tooltip, context, stack.getTag(), true);
        CompoundTag wingInfo = stack.getSubTag("WingInfo");
        if (wingInfo != null) {
            if (wingInfo.contains("left")) {
                CompoundTag leftWing = wingInfo.getCompound("left");
                if (!leftWing.isEmpty()) {
                    tooltip.add(new TranslatableText(LEFT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                    applyTooltip(tooltip, context, leftWing);
                }
            }
            if (wingInfo.contains("right")) {
                CompoundTag rightWing = wingInfo.getCompound("right");
                if (!rightWing.isEmpty()) {
                    tooltip.add(new TranslatableText(RIGHT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                    applyTooltip(tooltip, context, rightWing);
                }
            }
        }
    }

    @Override
    public String getTranslationKey() {
        return Items.ELYTRA.getTranslationKey();
    }

    public static void applyTooltip(List<Text> tooltip, TooltipContext context, CompoundTag wingIn) {
        applyTooltip(tooltip, context, wingIn, false);
    }

    public static void applyTooltip(List<Text> tooltip, TooltipContext context, CompoundTag wingIn, boolean ignoreDisplayTag) {
        CompoundTag wing = ElytraCustomizationUtil.migrateOldSplitWingFormat(wingIn);
        if (wing.getBoolean("HideCapePattern")) {
            tooltip.add(new TranslatableText(HIDDEN_CAPE_TRANSLATION_KEY).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        if (!ignoreDisplayTag && wing.contains("display")) {
            CompoundTag displayTag = wing.getCompound("display");
            if (context.isAdvanced()) {
                tooltip.add((new TranslatableText("item.color", String.format("#%06X", displayTag.getInt("color")))).formatted(Formatting.GRAY));
            } else {
                tooltip.add((new TranslatableText("item.dyed")).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        } else if (wing.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = wing.getCompound("BlockEntityTag");
            ListTag listnbt = blockEntityTag.getList("Patterns", 10);

            for (int i = 0; i < listnbt.size() && i < 6; ++i) {
                CompoundTag patternNBT = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(patternNBT.getInt("Color"));
                BannerPattern bannerpattern = BannerPattern.byId(patternNBT.getString("Pattern"));
                if (bannerpattern != null) {
                    tooltip.add((new TranslatableText("block.minecraft.banner." + bannerpattern.getName() + '.' + dyecolor.getName())).formatted(Formatting.GRAY));
                }
            }
        }
    }
}
