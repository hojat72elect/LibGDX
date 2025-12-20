package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

public class RepeatTextureDrawable extends BaseDrawable implements TransformDrawable {

    private Texture texture;
    private int shiftX;
    private int shiftY;

    public RepeatTextureDrawable() {
    }

    public RepeatTextureDrawable(Texture texture) {
        setTexture(texture);
    }

    public RepeatTextureDrawable(RepeatTextureDrawable drawable) {
        super(drawable);
        this.texture = drawable.texture;
        this.shiftX = drawable.shiftX;
        this.shiftY = drawable.shiftY;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.draw(texture,
                x, y,
                width, height,
                shiftX, shiftY,
                (int) width, (int) height,
                false, false);
    }

    @Override
    public void draw(Batch batch, float x, float y, float originX, float originY,
                     float width, float height, float scaleX, float scaleY, float rotation) {
        batch.draw(texture,
                x, y,
                originX, originY,
                width, height,
                scaleX, scaleY,
                rotation,
                shiftX, shiftY,
                (int) width, (int) height,
                false, false);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        if (texture != null) {
            setMinWidth(texture.getWidth());
            setMinHeight(texture.getHeight());
        }
    }

    /**
     * Shift in normalized values [0..1]
     */
    public void setShift(float shiftFactorX, float shiftFactorY) {
        shiftFactorX %= 1.0f;
        shiftFactorY %= 1.0f;
        this.shiftX = MathUtils.round(texture.getWidth() * shiftFactorX);
        this.shiftY = MathUtils.round(texture.getHeight() * shiftFactorY);
    }
}
