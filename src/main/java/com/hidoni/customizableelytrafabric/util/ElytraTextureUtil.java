package com.hidoni.customizableelytrafabric.util;

import com.hidoni.customizableelytrafabric.CustomizableElytra;
import com.hidoni.customizableelytrafabric.mixin.PlayerSkinTextureAccessor;
import com.hidoni.customizableelytrafabric.mixin.PlayerSkinTextureInvoker;
import com.hidoni.customizableelytrafabric.mixin.ResourceTextureInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElytraTextureUtil
{
    private static final Map<Identifier, Identifier> TEXTURE_CACHE = new HashMap<>();

    private static void convertTextureToGrayscale(NativeImage nativeImage)
    {
        for (int x = 0; x < nativeImage.getWidth(); x++)
        {
            for (int y = 0; y < nativeImage.getHeight(); y++)
            {
                int pixelRGBA = nativeImage.getPixelColor(x, y);
                int originalRGB = pixelRGBA & 0xFFFFFF;
                int grayscale = (((originalRGB & 0xFF0000) >> 16) + ((originalRGB & 0xFF00) >> 8) + (originalRGB & 0xFF)) / 3;
                int newRGB = 0x010101 * grayscale;
                nativeImage.setPixelColor(x, y, (pixelRGBA & 0xFF000000) | (newRGB));
            }
        }
    }

    private static NativeImage getNativeImageFromTexture(Identifier locationIn)
    {
        AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(locationIn);
        if (texture instanceof NativeImageBackedTexture dynamicTexture)
        {
            NativeImage dynamicTextureImage = dynamicTexture.getImage();
            if (dynamicTextureImage != null)
            {
                NativeImage returnTexture = new NativeImage(dynamicTextureImage.getWidth(), dynamicTextureImage.getHeight(), false);
                returnTexture.copyFrom(dynamicTextureImage);
                return returnTexture;
            }
        }
        else if (texture instanceof PlayerSkinTexture)
        {
            File cacheFile = ((PlayerSkinTextureAccessor) texture).getCacheFile();
            if (cacheFile != null)
            {
                try
                {
                    return ((PlayerSkinTextureInvoker) texture).callLoadTexture(new FileInputStream(cacheFile));
                }
                catch (FileNotFoundException e)
                {
                    return null;
                }
            }
            return null;
        }
        else if (texture instanceof ResourceTexture)
        {
            try
            {
                return ((ResourceTextureInvoker) texture).callLoadTextureData(MinecraftClient.getInstance().getResourceManager()).getImage();
            }
            catch (IOException e)
            {
                return null;
            }
        }
        return null;
    }

    private static Identifier createGrayscaleTexture(Identifier locationIn)
    {
        NativeImage texture = getNativeImageFromTexture(locationIn);
        if (texture == null)
        {
            return locationIn;
        }
        convertTextureToGrayscale(texture);
        Identifier locationOut = new Identifier(CustomizableElytra.MOD_ID, "grayscale_" + locationIn.getPath());
        locationOut = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(locationOut.toUnderscoreSeparatedString(), new NativeImageBackedTexture(texture));
        TEXTURE_CACHE.put(locationIn, locationOut);
        return locationOut;
    }

    public static Identifier getGrayscale(Identifier locationIn)
    {
        if (!TEXTURE_CACHE.containsKey(locationIn))
        {
            return createGrayscaleTexture(locationIn);
        }
        return TEXTURE_CACHE.get(locationIn);
    }
}
