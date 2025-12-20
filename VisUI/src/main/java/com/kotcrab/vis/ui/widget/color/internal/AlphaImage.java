package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.kotcrab.vis.ui.widget.VisImage;

/**
 * Image that displays checkerboard as background, used by ColorPicker to display selected colors with alphas. Note that for perfect grid
 * this image should have size which is multiplication of gridSize. Eg. if gridSize is equal to 5, this image can have size 65x100.
 * (because both 65 and 100 are divisible by 5)
 */
public class AlphaImage extends VisImage {
    private final GridSubImage gridImage;

    public AlphaImage(PickerCommons commons, float gridSize) {
        super(commons.whiteTexture);
        gridImage = new GridSubImage(commons.gridShader, commons.whiteTexture, gridSize);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //don't draw grid if widget alpha is different than 1 because
        //this creates weird affect when window is fading in/out,
        //both parent image and grid is visible
        if (getColor().a != 1) gridImage.draw(batch, this);
        super.draw(batch, parentAlpha);
    }
}
