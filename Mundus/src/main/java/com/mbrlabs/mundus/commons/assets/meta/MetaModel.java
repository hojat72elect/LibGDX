package com.mbrlabs.mundus.commons.assets.meta;

import com.badlogic.gdx.utils.ObjectMap;

public class MetaModel {

    public static final String JSON_DEFAULT_MATERIALS = "mats";
    public static final String JSON_NUM_BONES = "numBones";

    // g3db material id -> material asset uuid
    private final ObjectMap<String, String> defaultMaterials = new ObjectMap<>();

    private int numBones;

    public ObjectMap<String, String> getDefaultMaterials() {
        return defaultMaterials;
    }

    public int getNumBones() {
        return numBones;
    }

    public void setNumBones(int numBones) {
        this.numBones = numBones;
    }

    @Override
    public String toString() {
        return "MetaModel{" +
                "defaultMaterials=" + defaultMaterials +
                "numBones=" + numBones +
                '}';
    }
}
