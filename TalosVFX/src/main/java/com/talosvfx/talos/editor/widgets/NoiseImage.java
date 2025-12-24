
package com.talosvfx.talos.editor.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rockbite.bongo.engine.render.ShaderFlags;
import com.rockbite.bongo.engine.render.SpriteShaderCompiler;
import com.talosvfx.talos.runtime.vfx.utils.SimplexNoise;

public class NoiseImage extends Actor {

    Texture white;
    ShaderProgram shaderProgram;
    Pixmap pixmap;
    private final Skin skin;
    private float frequency = 20f;

    public NoiseImage(Skin skin) {
        this.skin = skin;
        white = new Texture(Gdx.files.internal("white.png")); //TODO: not cool

        shaderProgram = SpriteShaderCompiler.getOrCreateShader("ui/noise", Gdx.files.internal("shaders/gl2/ui/default.vert.glsl"), Gdx.files.internal("shaders/gl2/ui/noise.frag.glsl"), new ShaderFlags());

        pixmap = new Pixmap(165, 100, Pixmap.Format.RGB888);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //drawPixmap(batch, parentAlpha);
        } else {
            drawWithShader(batch, parentAlpha);
        }
    }

    public void drawWithShader(Batch batch, float parentAlpha) {
        ShaderProgram prevShader = batch.getShader();
        batch.setShader(shaderProgram);

        shaderProgram.setUniformf("frequency", frequency);

        // do the rendering
        batch.draw(white, getX(), getY(), getWidth(), getHeight());

        batch.setShader(prevShader);
    }

    /**
     * @param batch
     * @param parentAlpha
     * @deprecated WARNING THIS IS FOR TESTING PURPOSES ONLY,
     * DO NOT USE AS THIS HAS HORRIBLE PERFORMANCE
     */
    public void drawPixmap(Batch batch, float parentAlpha) {
        SimplexNoise simplexNoise = new SimplexNoise();
        pixmap.setColor(0, 0, 0, 1f);
        pixmap.fill();
        for (int x = 0; x < 165; x++) {
            for (int y = 0; y < 100; y++) {
                float v = simplexNoise.query(x / 165f, y / 100f, frequency) * 0.5f + 0.5f;
                pixmap.setColor(v, v, v, 1f);
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap, Pixmap.Format.RGB888, false);
        batch.draw(texture, getX(), getY());
    }

    public void setFrequency(float value) {
        frequency = value;
    }
}
