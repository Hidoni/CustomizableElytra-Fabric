package com.hidoni.customizableelytrafabric.item;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements DyeableItem
{
    public final static String LEFT_WING_TRANSLATION_KEY = "item.customizable_elytra.left_wing";
    public final static String RIGHT_WING_TRANSLATION_KEY = "item.customizable_elytra.right_wing";

    public CustomizableElytraItem(Settings settings)
    {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public static Identifier getTextureLocation(BannerPattern bannerIn)
    {
        return new Identifier(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.getName());
    }

    @Override
    public int getColor(ItemStack stack)
    {
        return getColor(stack, 0);
    }

    public int getColor(ItemStack stack, int index)
    {
        return ElytraCustomizationUtil.getData(stack).handler.getColor(index);
    }

    @Override
    public boolean hasColor(ItemStack stack)
    {
        NbtCompound bannerTag = stack.getSubTag("BlockEntityTag");
        NbtCompound wingTag = stack.getSubTag("WingInfo");
        return DyeableItem.super.hasColor(stack) || bannerTag != null || wingTag != null;
    }

    @Override
    public void removeColor(ItemStack stack)
    {
        DyeableItem.super.removeColor(stack);
        NbtCompound compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null)
        {
            stack.removeSubTag("BlockEntityTag");
        }
        compoundTag = stack.getSubTag("WingInfo");
        if (compoundTag != null)
        {
            stack.removeSubTag("WingInfo");
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        BannerItem.appendBannerTooltip(stack, tooltip);
        NbtCompound wingInfo = stack.getSubTag("WingInfo");
        if (wingInfo != null)
        {
            if (wingInfo.contains("left"))
            {
                tooltip.add(new TranslatableText(LEFT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                NbtCompound leftWing = wingInfo.getCompound("left");
                applyWingTooltip(tooltip, context, leftWing);
            }
            if (wingInfo.contains("right"))
            {
                tooltip.add(new TranslatableText(RIGHT_WING_TRANSLATION_KEY).formatted(Formatting.GRAY));
                NbtCompound leftWing = wingInfo.getCompound("right");
                applyWingTooltip(tooltip, context, leftWing);
            }
        }
    }

    @Override
    public String getTranslationKey()
    {
        return Items.ELYTRA.getTranslationKey();
    }

    private void applyWingTooltip(List<Text> tooltip, TooltipContext context, NbtCompound wingIn)
    {
        if (wingIn.contains("color"))
        {
            if (context.isAdvanced())
            {
                tooltip.add((new TranslatableText("item.color", String.format("#%06X", wingIn.getInt("color")))).formatted(Formatting.GRAY));
            }
            else
            {
                tooltip.add((new TranslatableText("item.dyed")).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        }
        else if (wingIn.contains("Patterns"))
        {
            NbtList listnbt = wingIn.getList("Patterns", 10);

            for (int i = 0; i < listnbt.size() && i < 6; ++i)
            {
                NbtCompound patternNBT = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(patternNBT.getInt("Color"));
                BannerPattern bannerpattern = BannerPattern.byId(patternNBT.getString("Pattern"));
                if (bannerpattern != null)
                {
                    tooltip.add((new TranslatableText("block.minecraft.banner." + bannerpattern.getName() + '.' + dyecolor.getName())).formatted(Formatting.GRAY));
                }
            }
        }
    }
}
