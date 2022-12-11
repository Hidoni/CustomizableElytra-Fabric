package com.hidoni.customizableelytrafabric.mixin;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.util.ElytraCustomizationUtil;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomTextureAtlas(CallbackInfo ci) {
        LAYERS_TO_LOADERS = new HashMap<>(LAYERS_TO_LOADERS);
        LAYERS_TO_LOADERS.put(ElytraCustomizationUtil.ELYTRA_TEXTURE_ATLAS, new Identifier(CustomizableElytra.MOD_ID, "elytra_patterns"));
    }
}
