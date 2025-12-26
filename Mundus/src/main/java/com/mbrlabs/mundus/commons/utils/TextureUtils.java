package com.mbrlabs.mundus.commons.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TextureUtils {

    public static Texture loadMipmapTexture(FileHandle fileHandle, boolean tilable) {
        Texture texture = new Texture(fileHandle, true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        if (tilable) {
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }

        return texture;
    }
}
