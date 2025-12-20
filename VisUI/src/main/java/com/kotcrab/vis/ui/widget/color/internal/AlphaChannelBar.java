package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Channel bar intended for alpha channel, renders alpha grid below channel texture.
 */
public class AlphaChannelBar extends ChannelBar {
    private final GridSubImage gridImage;

    public AlphaChannelBar(PickerCommons commons, int mode, int maxValue, ChangeListener changeListener) {
        super(commons, mode, maxValue, changeListener);
        gridImage = new GridSubImage(commons.gridShader, commons.whiteTexture, 6 * commons.sizes.scaleFactor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        gridImage.draw(batch, this);
        super.draw(batch, parentAlpha);
    }
}
