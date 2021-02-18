package com.hidoni.customizableelytrafabric.item;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements DyeableItem
{
    public CustomizableElytraItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public int getColor(ItemStack stack)
    {
        CompoundTag compoundTag = stack.getSubTag("display");
        if (compoundTag != null)
        {
            return compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 16777215;
        }
        compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null)
        {
            return DyeColor.byId(compoundTag.getInt("Base")).getMaterialColor().color;
        }
        return 16777215;
    }

    @Override
    public boolean hasColor(ItemStack stack)
    {
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        return DyeableItem.super.hasColor(stack) || compoundTag != null;
    }

    @Override
    public void removeColor(ItemStack stack)
    {
        DyeableItem.super.removeColor(stack);
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        if (compoundTag != null)
        {
            stack.removeSubTag("BlockEntityTag");
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    @Environment(EnvType.CLIENT)
    public static Identifier getTextureLocation(BannerPattern bannerIn)
    {
        /*if (Config.useLowQualityElytraBanners.get())
        {
            return new ResourceLocation(CustomizableElytra.MOD_ID, "entity/elytra_banner_low/" + bannerIn.getFileName());
        }*/
        return new Identifier(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.getName());
    }

    @Override
    public String getTranslationKey()
    {
        return Items.ELYTRA.getTranslationKey();
    }
}
