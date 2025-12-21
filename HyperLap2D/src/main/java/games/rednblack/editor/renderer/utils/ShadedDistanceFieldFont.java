package games.rednblack.editor.renderer.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Renders bitmap fonts using distance field textures, see the <a
 * href="https://github.com/libgdx/libgdx/wiki/Distance-field-fonts">Distance Field Fonts wiki article</a> for usage. Initialize
 * the SpriteBatch with the {@link #createDistanceFieldShader()} shader.
 * <p>
 * Attention: The batch is flushed before and after each string is rendered.
 */
public class ShadedDistanceFieldFont extends BitmapFont {
    private float distanceFieldSmoothing;
    private ShaderProgram distanceShader;

    public ShadedDistanceFieldFont() {
        super();
    }

    public ShadedDistanceFieldFont(BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
        super(data, pageRegions, integer);
    }

    public ShadedDistanceFieldFont(BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region, integer);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile, boolean flip) {
        super(fontFile, flip);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
        super(fontFile, imageFile, flip, integer);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
        super(fontFile, imageFile, flip);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile, TextureRegion region, boolean flip) {
        super(fontFile, region, flip);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile, TextureRegion region) {
        super(fontFile, region);
    }

    public ShadedDistanceFieldFont(FileHandle fontFile) {
        super(fontFile);
    }

    static public ShaderProgram createDistanceFieldShader() {
        ShaderProgram shader = ShaderCompiler.compileShader(DefaultShaders.DISTANCE_FIELD_VERTEX_SHADER, DefaultShaders.DISTANCE_FIELD_FRAGMENT_SHADER);
        if (!shader.isCompiled())
            throw new IllegalArgumentException("Error compiling distance field shader: " + shader.getLog());
        return shader;
    }

    protected void load(BitmapFontData data) {
        super.load(data);

        // Distance field font rendering requires font texture to be filtered linear.
        final Array<TextureRegion> regions = getRegions();
        for (TextureRegion region : regions)
            region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        setUseIntegerPositions(false);
    }

    @Override
    public BitmapFontCache newFontCache() {
        if (distanceShader == null)
            distanceShader = createDistanceFieldShader();
        return new DistanceFieldFontCache(this, distanceShader);
    }

    /**
     * @return The distance field smoothing factor for this font.
     */
    public float getDistanceFieldSmoothing() {
        return distanceFieldSmoothing;
    }

    /**
     * @param distanceFieldSmoothing Set the distance field smoothing factor for this font. SpriteBatch needs to have this shader
     *                               set for rendering distance field fonts.
     */
    public void setDistanceFieldSmoothing(float distanceFieldSmoothing) {
        this.distanceFieldSmoothing = distanceFieldSmoothing;
    }

    /**
     * Provides a font cache that uses distance field shader for rendering fonts. Attention: breaks batching because uniform is
     * needed for smoothing factor, so a flush is performed before and after every font rendering.
     */
    static private class DistanceFieldFontCache extends BitmapFontCache {
        ShaderProgram distanceShader;

        public DistanceFieldFontCache(ShadedDistanceFieldFont font, ShaderProgram distanceShader) {
            super(font, font.usesIntegerPositions());
            this.distanceShader = distanceShader;
        }

        public DistanceFieldFontCache(ShadedDistanceFieldFont font, boolean integer, ShaderProgram distanceShader) {
            super(font, integer);
            this.distanceShader = distanceShader;
        }

        private float getSmoothingFactor() {
            final ShadedDistanceFieldFont font = (ShadedDistanceFieldFont) super.getFont();
            return font.getDistanceFieldSmoothing() * font.getScaleX();
        }

        private void setSmoothingUniform(Batch batch, float smoothing) {
            batch.flush();
            distanceShader.setUniformf("u_smoothing", smoothing);
        }

        @Override
        public void draw(Batch spriteBatch) {
            ShaderProgram oldShader = spriteBatch.getShader();
            spriteBatch.setShader(distanceShader);
            setSmoothingUniform(spriteBatch, getSmoothingFactor());
            super.draw(spriteBatch);
            setSmoothingUniform(spriteBatch, 0);
            spriteBatch.setShader(oldShader);
        }

        @Override
        public void draw(Batch spriteBatch, int start, int end) {
            ShaderProgram oldShader = spriteBatch.getShader();
            spriteBatch.setShader(distanceShader);
            setSmoothingUniform(spriteBatch, getSmoothingFactor());
            super.draw(spriteBatch, start, end);
            setSmoothingUniform(spriteBatch, 0);
            spriteBatch.setShader(oldShader);
        }
    }
}
