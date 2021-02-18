package com.hidoni.customizableelytrafabric.registry;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems
{
    public static final Item CUSTOMIZABLE_ELYTRA = Registry.register(Registry.ITEM, new Identifier(CustomizableElytra.MOD_ID, "customizable_elytra"), new CustomizableElytraItem(new FabricItemSettings().maxDamage(432).rarity(Rarity.UNCOMMON).equipmentSlot((stack ->
    {
        return EquipmentSlot.CHEST;
    }))));

    public static void register()
    {

    }
}
