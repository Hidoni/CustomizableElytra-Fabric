package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private static final EntityAttributeModifier FLIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("618325f1-cecb-4349-b210-0cb6604d52d0"), "Customizable elytra chestplate flight", 1.0D, EntityAttributeModifier.Operation.ADDITION);
    @Shadow
    protected int roll;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    @Nullable
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Inject(at = @At("HEAD"), method = "initAi()V")
    public void handleCustomizableElytraTick(CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        EntityAttributeInstance attributeInstance = this.getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(FLIGHT_MODIFIER);
        }
        if (itemStack.getItem() instanceof CustomizableElytraItem) {
            handleFlightTick(itemStack);
            checkAndEnableFlight(itemStack, attributeInstance);
        }
    }

    private void handleFlightTick(ItemStack itemStack) {
        boolean isFlying = this.getFlag(7);
        if (isFlying && !this.onGround && !this.hasVehicle() && !this.hasStatusEffect(StatusEffects.LEVITATION)) {
            {
                int i = this.roll + 1;
                if (!this.world.isClient && i % 10 == 0) {
                    int j = i / 10;
                    if (j % 2 == 0) {
                        itemStack.damage(1, (LivingEntity) (Object) this, (player) ->
                        {
                            player.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
                        });
                    }
                }
            }
        }
    }

    private void checkAndEnableFlight(ItemStack itemStack, EntityAttributeInstance attributeInstance) {
        if (attributeInstance != null) {
            attributeInstance.removeModifier(FLIGHT_MODIFIER);
            if (CustomizableElytraItem.isUsable(itemStack)) {
                attributeInstance.addTemporaryModifier(FLIGHT_MODIFIER);
            }
        }
    }
}
