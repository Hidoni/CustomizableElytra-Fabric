package com.hidoni.customizableelytrafabric.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ElytraCustomizationUtil
{
    public static ElytraCustomizationData getData(ItemStack elytraIn)
    {
        if (elytraIn.getSubTag("WingInfo") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Split, new SplitCustomizationHandler(elytraIn));
        }
        return getData(elytraIn.getTag());
    }

    /*
     * Before version 1.5, Crafting wings separately only stored the value of a tag, i.e. only the color sub-tag of
     * the display tag, after 1.5 this is no longer viable due to the ability to hide cape patterns, and data-fixers
     * don't work with mods, so this function had to be implemented as a fix.
     */
    public static NbtCompound migrateOldSplitWingFormat(NbtCompound wingIn)
    {
        if (wingIn == null)
        {
            return new NbtCompound();
        }
        if (wingIn.contains("color", 99))
        {
            NbtCompound newNBT = new NbtCompound();
            NbtCompound displayNBT = new NbtCompound();
            displayNBT.putInt("color", wingIn.getInt("color"));
            newNBT.put("display", displayNBT);
            return newNBT;
        }
        else if (wingIn.contains("Patterns", 9))
        {
            NbtCompound newNBT = new NbtCompound();
            NbtCompound blockEntityTagNBT = new NbtCompound();
            blockEntityTagNBT.put("Patterns", wingIn.getList("Patterns", 10));
            blockEntityTagNBT.putInt("Base", wingIn.getInt("Base"));
            newNBT.put("BlockEntityTag", blockEntityTagNBT);
            return newNBT;
        }
        return wingIn;
    }

    public static ElytraCustomizationData getData(NbtCompound wingIn)
    {
        NbtCompound wingNBT = migrateOldSplitWingFormat(wingIn);
        if (wingNBT.contains("display"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(wingNBT));
        }
        else if (wingNBT.contains("BlockEntityTag"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(wingNBT));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler(wingNBT.getBoolean("HideCapePattern")));
    }
}
