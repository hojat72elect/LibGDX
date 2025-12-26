package com.mbrlabs.mundus.commons.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.mbrlabs.mundus.commons.assets.TextureAsset;
import com.mbrlabs.mundus.commons.utils.TextureProvider;

public class SplatTexture implements TextureProvider {

    public Channel channel;
    public TextureAsset texture;
    public SplatTexture(Channel channel, TextureAsset texture) {
        this.channel = channel;
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        if (texture != null) {
            return texture.getTexture();
        }
        return null;
    }

    public void setTexture(TextureAsset texture) {
        this.texture = texture;
    }

    public enum Channel {
        BASE, R, G, B, A
    }
}
