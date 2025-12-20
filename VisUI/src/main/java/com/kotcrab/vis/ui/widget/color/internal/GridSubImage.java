package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GridSubImage {
    private final ShaderProgram gridShader;
    private final Texture whiteTexture;
    private final float gridSize;

    public GridSubImage(ShaderProgram gridShader, Texture whiteTexture, float gridSize) {
        this.gridShader = gridShader;
        this.whiteTexture = whiteTexture;
        this.gridSize = gridSize;
    }

    public void draw(Batch batch, Image parent) {
        ShaderProgram originalShader = batch.getShader();
        batch.setShader(gridShader);
        gridShader.setUniformf("u_width", parent.getWidth());
        gridShader.setUniformf("u_height", parent.getHeight());
        gridShader.setUniformf("u_gridSize", gridSize);
        batch.draw(whiteTexture, parent.getX() + parent.getImageX(), parent.getY() + parent.getImageY(),
                parent.getImageWidth() * parent.getScaleX(), parent.getImageHeight() * parent.getScaleY());
        batch.setShader(originalShader);
    }
}
