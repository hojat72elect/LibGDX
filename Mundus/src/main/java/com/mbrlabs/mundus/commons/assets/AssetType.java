package com.mbrlabs.mundus.commons.assets;

public enum AssetType {
    /**
     * Texture type. Can be pretty much any type of image.
     */
    TEXTURE("Texture"),
    /**
     * A Texture, backed by a pixmap. Can be pretty much any type of image.
     */
    PIXMAP_TEXTURE("Pixmap Texture"),
    /**
     * 3D file. Can be g3db, g3dbj, dae, obj, fbx.
     */
    MODEL("Model"),
    /**
     * Terra file. Contains height data for terrains.
     */
    TERRAIN("Terrain"),
    /**
     * Material file. Mundus material file contains material information.
     */
    MATERIAL("Material"),
    /**
     * Water file. Contains data for water.
     */
    WATER("Water"),
    /**
     * Skybox file. Holds reference to skybox textures.
     **/
    SKYBOX("Skybox"),
    /**
     * Custom file. For plugins.
     **/
    CUSTOM("Custom"),
    /**
     * Holds the terrain textures to be used.
     **/
    TERRAIN_LAYER("Terrain Layer");

    private final String value;

    AssetType(String value) {
        this.value = value;
    }

    public static AssetType valueFromString(String string) {
        for (AssetType res : values()) {
            if (res.value.equals(string)) {
                return res;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }
}
