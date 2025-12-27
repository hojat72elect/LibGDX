package com.mbrlabs.mundus.editor.core.kryo.descriptors;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ProjectSettingsDescriptor {

    // Export
    @Tag(0)
    private boolean exportCompressScenes;
    @Tag(1)
    private boolean exportAllAssets;
    @Tag(2)
    private String exportOutputFolder;
    @Tag(3)
    private String jsonType;

    public String getJsonType() {
        return jsonType;
    }

    public void setJsonType(String jsonType) {
        this.jsonType = jsonType;
    }

    public boolean isExportCompressScenes() {
        return exportCompressScenes;
    }

    public void setExportCompressScenes(boolean exportCompressScenes) {
        this.exportCompressScenes = exportCompressScenes;
    }

    public boolean isExportAllAssets() {
        return exportAllAssets;
    }

    public void setExportAllAssets(boolean exportAllAssets) {
        this.exportAllAssets = exportAllAssets;
    }

    public String getExportOutputFolder() {
        return exportOutputFolder;
    }

    public void setExportOutputFolder(String exportOutputFolder) {
        this.exportOutputFolder = exportOutputFolder;
    }
}
