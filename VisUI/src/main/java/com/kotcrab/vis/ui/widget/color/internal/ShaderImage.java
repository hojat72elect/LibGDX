package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.kotcrab.vis.ui.widget.VisImage;

/**
 * Allow to render standard {@link VisImage} with shader. Shaders uniforms can be set in {@link #setShaderUniforms(ShaderProgram)}
 */
public class ShaderImage extends VisImage {
    private final ShaderProgram shader;

    public ShaderImage(ShaderProgram shader, Texture texture) {
        super(texture);
        this.shader = shader;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShaderProgram originalShader = batch.getShader();
        batch.setShader(shader);
        setShaderUniforms(shader);

        super.draw(batch, parentAlpha);

        batch.setShader(originalShader);
    }

    protected void setShaderUniforms(ShaderProgram shader) {

    }
}
