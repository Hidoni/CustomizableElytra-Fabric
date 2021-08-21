package com.hidoni.customizableelytrafabric.mixin;

import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourceTexture.class)
public interface ResourceTextureInvoker
{
    @Invoker("loadTextureData")
    ResourceTexture.TextureData callLoadTextureData(ResourceManager resourceManager);
}
