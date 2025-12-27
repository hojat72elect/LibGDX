package com.mbrlabs.mundus.editor.core.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonWriter;

public class ProjectSettings {

    private final ExportSettings export = new ExportSettings();

    public ExportSettings getExport() {
        return export;
    }

    /**
     *
     */
    public class ExportSettings {
        public JsonWriter.OutputType jsonType = JsonWriter.OutputType.json;
        public boolean compressScenes = false;
        public boolean allAssets = true;
        public FileHandle outputFolder;
    }
}
