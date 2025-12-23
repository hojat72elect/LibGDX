
package com.rockbite.bongo.engine.gltf.scene.shader;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.math.Vector3;

public class PBRVec3Attribute extends Attribute {

    public final static String EmissiveFactorAlias = "emissiveFactor";
    public final static long EmissiveFactor = register(EmissiveFactorAlias);
    public final Vector3 vec3 = new Vector3();

    public PBRVec3Attribute(final long type) {
        super(type);
    }

    public PBRVec3Attribute(final long type, final Vector3 vec3) {
        this(type);
        if (vec3 != null) this.vec3.set(vec3);
    }

    public PBRVec3Attribute(final PBRVec3Attribute copyFrom) {
        this(copyFrom.type, copyFrom.vec3);
    }

    public final static PBRVec3Attribute createEmmissiveModifier(final Vector3 vec3) {
        return new PBRVec3Attribute(EmissiveFactor, vec3);
    }

    @Override
    public Attribute copy() {
        return new PBRVec3Attribute(this);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 953 * result + vec3.hashCode();
        return result;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);
        return ((PBRVec3Attribute) o).vec3.hashCode() - vec3.hashCode();
    }
}
