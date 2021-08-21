package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.item.CustomizableElytraItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    protected int roll;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "tickFallFlying()V")
    public void handleCustomizableElytraTick(CallbackInfo ci) {
        boolean isFlying = this.getFlag(7);
        if (isFlying && !this.onGround && !this.hasVehicle() && !this.hasStatusEffect(StatusEffects.LEVITATION)) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() instanceof CustomizableElytraItem) {
                int i = this.roll + 1;
                if (!this.world.isClient && i % 10 == 0) {
                    int j = i / 10;
                    if (j % 2 == 0) {
                        itemStack.damage(1, (LivingEntity) (Object) this, (player) -> {
                            player.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
                        });
                    }
                }
                this.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
            }
        }
    }
}
