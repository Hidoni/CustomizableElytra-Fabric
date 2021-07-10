package com.hidoni.customizableelytrafabric.mixin;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.InputStream;

@Mixin(PlayerSkinTexture.class)
public interface PlayerSkinTextureInvoker
{
    @Invoker("loadTexture")
    NativeImage callLoadTexture(InputStream inputStreamIn);
}
