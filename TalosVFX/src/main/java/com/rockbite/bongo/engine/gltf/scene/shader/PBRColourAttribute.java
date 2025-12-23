
package com.rockbite.bongo.engine.gltf.scene.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;

public class PBRColourAttribute extends Attribute {

    public final static String BaseColourModifierAlias = "baseColourModifier";
    public final static long BaseColourModifier = register(BaseColourModifierAlias);
    static Color staticColour = new Color();
    public final float[] color = new float[]{1f, 1f, 1f, 1f};

    public PBRColourAttribute(final long type) {
        super(type);
    }

    public PBRColourAttribute(final long type, final Color color) {
        this(type);
        if (color != null) {
            this.color[0] = color.r;
            this.color[1] = color.g;
            this.color[2] = color.b;
            this.color[3] = color.a;
        }
    }

    public PBRColourAttribute(final long type, final float[] color) {
        this(type);
        if (color != null) {
            this.color[0] = color[0];
            this.color[1] = color[1];
            this.color[2] = color[2];
            this.color[3] = color[3];
        }
    }

    public PBRColourAttribute(final long type, float r, float g, float b, float a) {
        this(type);
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        this.color[3] = a;
    }

    public PBRColourAttribute(final PBRColourAttribute copyFrom) {
        this(copyFrom.type, copyFrom.color);
    }

    public final static PBRColourAttribute createBaseColourModifier(final Color color) {
        return new PBRColourAttribute(BaseColourModifier, color);
    }

    @Override
    public Attribute copy() {
        return new PBRColourAttribute(this);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        final int intbits = staticColour.set(color[0], color[1], color[2], color[3]).toIntBits();
        result = 953 * result + intbits;
        return result;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);

        final float[] otherColour = ((PBRColourAttribute) o).color;

        final int intbits = staticColour.set(color[0], color[1], color[2], color[3]).toIntBits();
        final int otherBits = staticColour.set(otherColour[0], otherColour[1], otherColour[2], otherColour[3]).toIntBits();

        return otherBits - intbits;
    }
}
