package com.mbrlabs.mundus.commons.water;

import com.badlogic.gdx.utils.Array;
import com.mbrlabs.mundus.commons.MundusAttribute;
import com.mbrlabs.mundus.commons.water.attributes.WaterAttributes;

public class WaterMaterial extends WaterAttributes {
    private static int counter = 0;

    public String id;

    /**
     * Create an empty material
     */
    public WaterMaterial() {
        this("mtl" + (++counter));
    }

    /**
     * Create an empty material
     */
    public WaterMaterial(final String id) {
        this.id = id;
    }

    /**
     * Create a material with the specified attributes
     */
    public WaterMaterial(final MundusAttribute... attributes) {
        this();
        set(attributes);
    }

    /**
     * Create a material with the specified attributes
     */
    public WaterMaterial(final String id, final MundusAttribute... attributes) {
        this(id);
        set(attributes);
    }

    /**
     * Create a material with the specified attributes
     */
    public WaterMaterial(final Array<MundusAttribute> attributes) {
        this();
        set(attributes);
    }

    /**
     * Create a material with the specified attributes
     */
    public WaterMaterial(final String id, final Array<MundusAttribute> attributes) {
        this(id);
        set(attributes);
    }

    /**
     * Create a material which is an exact copy of the specified material
     */
    public WaterMaterial(final WaterMaterial copyFrom) {
        this(copyFrom.id, copyFrom);
    }

    /**
     * Create a material which is an exact copy of the specified material
     */
    public WaterMaterial(final String id, final WaterMaterial copyFrom) {
        this(id);
        for (MundusAttribute attr : copyFrom)
            set(attr.copy());
    }

    /**
     * Create a copy of this material
     */
    public WaterMaterial copy() {
        return new WaterMaterial(this);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 3 * id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof WaterMaterial) && ((other == this) || ((((WaterMaterial) other).id.equals(id)) && super.equals(other)));
    }
}
