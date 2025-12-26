package com.mbrlabs.mundus.commons.water.attributes;

import com.mbrlabs.mundus.commons.MundusAttribute;

public class WaterIntAttribute extends MundusAttribute {
    public static final String CullFaceAlias = "cullfaceWater";
    public static final long CullFace = register(CullFaceAlias);
    public int value;

    public WaterIntAttribute(long type) {
        super(type);
    }

    public WaterIntAttribute(long type, int value) {
        super(type);
        this.value = value;
    }

    /**
     * create a cull face attribute to be used in a material
     *
     * @param value cull face value, possible values are GL_FRONT_AND_BACK, GL_BACK, GL_FRONT, or -1 to inherit default
     * @return an attribute
     */
    public static WaterIntAttribute createCullFace(int value) {
        return new WaterIntAttribute(CullFace, value);
    }

    @Override
    public MundusAttribute copy() {
        return new WaterIntAttribute(type, value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 983 * result + value;
        return result;
    }

    @Override
    public int compareTo(MundusAttribute o) {
        if (type != o.type) return (int) (type - o.type);
        return value - ((WaterIntAttribute) o).value;
    }
}
