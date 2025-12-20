package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.widget.color.BasicColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerWidgetStyle;

/**
 * Colors palette used to display colors using all possible values of saturation and value.
 */
public class Palette extends ShaderImage {
    private final ColorPickerWidgetStyle style;
    private final Sizes sizes;

    private int xV, yS;
    private final int maxValue;

    private float selectorX;
    private float selectorY;

    private float pickerHue;

    public Palette(PickerCommons commons, int maxValue, ChangeListener listener) {
        super(commons.paletteShader, commons.whiteTexture);
        this.style = commons.style;
        this.sizes = commons.sizes;
        this.maxValue = maxValue;

        setTouchable(Touchable.enabled);
        setValue(0, 0);
        addListener(listener);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateValueFromTouch(x, y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateValueFromTouch(x, y);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        style.verticalSelector.draw(batch,
                getX(),
                getY() + selectorY - style.verticalSelector.getMinHeight() / 2 + 0.1f,
                getImageWidth(), style.verticalSelector.getMinHeight());

        style.horizontalSelector.draw(batch,
                getX() + selectorX - style.horizontalSelector.getMinWidth() / 2 + 0.1f,
                getY(),
                style.horizontalSelector.getMinWidth(), getImageHeight());

        style.cross.draw(batch,
                getX() + selectorX - style.cross.getMinWidth() / 2 + 0.1f,
                getY() + selectorY - style.cross.getMinHeight() / 2 + 0.1f,
                style.cross.getMinWidth(), style.cross.getMinHeight());
    }

    @Override
    protected void setShaderUniforms(ShaderProgram shader) {
        shader.setUniformf("u_h", pickerHue);
    }

    public void setPickerHue(int pickerHue) {
        this.pickerHue = pickerHue / 360.0f;
    }

    public void setValue(int s, int v) {
        xV = v;
        yS = s;

        if (xV < 0) xV = 0;
        if (xV > maxValue) xV = maxValue;

        if (yS < 0) yS = 0;
        if (yS > maxValue) yS = maxValue;

        selectorX = ((float) xV / maxValue) * BasicColorPicker.PALETTE_SIZE * sizes.scaleFactor;
        selectorY = ((float) yS / maxValue) * BasicColorPicker.PALETTE_SIZE * sizes.scaleFactor;
    }

    private void updateValueFromTouch(float touchX, float touchY) {
        int newV = (int) (touchX / BasicColorPicker.PALETTE_SIZE * maxValue / sizes.scaleFactor);
        int newS = (int) (touchY / BasicColorPicker.PALETTE_SIZE * maxValue / sizes.scaleFactor);

        setValue(newS, newV);

        ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
        fire(changeEvent);
        Pools.free(changeEvent);
    }

    public int getV() {
        return xV;
    }

    public int getS() {
        return yS;
    }
}
