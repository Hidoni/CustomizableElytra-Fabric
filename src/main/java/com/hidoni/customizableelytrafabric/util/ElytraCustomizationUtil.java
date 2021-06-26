package com.hidoni.customizableelytrafabric.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ElytraCustomizationUtil
{
    public static ElytraCustomizationData getData(ItemStack elytraIn)
    {
        if (elytraIn.getSubTag("display") != null && elytraIn.getSubTag("display").get("color") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(elytraIn));
        }
        else if (elytraIn.getSubTag("BlockEntityTag") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(elytraIn));
        }
        else if (elytraIn.getSubTag("WingInfo") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Split, new SplitCustomizationHandler(elytraIn));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
    }

    public static ElytraCustomizationData getData(NbtCompound wingIn)
    {
        if (wingIn == null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
        }
        if (wingIn.contains("color"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(wingIn));
        }
        else if (wingIn.contains("Patterns"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(wingIn));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
    }
}
