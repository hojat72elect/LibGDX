package com.mbrlabs.mundus.commons.assets.meta;

import com.badlogic.gdx.files.FileHandle;
import com.mbrlabs.mundus.commons.assets.AssetType;

public class Meta {

    public static final String META_EXTENSION = "meta";
    public static final int CURRENT_VERSION = 1;

    public static final String JSON_VERSION = "v";
    public static final String JSON_LAST_MOD = "mod";
    public static final String JSON_UUID = "id";
    public static final String JSON_TYPE = "t";
    public static final String JSON_TERRAIN = "ter";
    public static final String JSON_MODEL = "mdl";
    public static final String JSON_CUSTOM = "cus";
    private final FileHandle file;
    private int version;
    private long lastModified;
    private String uuid;
    private AssetType type;
    private MetaModel model;
    private MetaTerrain terrain;
    private MetaCustom custom;

    public Meta(FileHandle file) {
        this.file = file;
    }

    public FileHandle getFile() {
        return file;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public MetaModel getModel() {
        return model;
    }

    public void setModel(MetaModel model) {
        this.model = model;
    }

    public MetaTerrain getTerrain() {
        return terrain;
    }

    public void setTerrain(MetaTerrain terrain) {
        this.terrain = terrain;
    }

    public MetaCustom getCustom() {
        return custom;
    }

    public void setCustom(MetaCustom custom) {
        this.custom = custom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meta meta = (Meta) o;

        return uuid.equals(meta.uuid) && file.equals(meta.file);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "version=" + version +
                ", lastModified=" + lastModified +
                ", uuid='" + uuid + '\'' +
                ", type=" + type +
                ", model=" + model +
                ", terrain=" + terrain +
                ", custom=" + custom +
                ", file=" + file +
                '}';
    }
}
