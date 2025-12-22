package games.rednblack.editor.plugin.tiled.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import games.rednblack.editor.renderer.utils.HyperJson;

public class SaveDataManager {

    public DataToSave dataToSave;

    private final Json json;
    private final FileHandle fileHandle;


    public SaveDataManager(String projectPath) {
        json = HyperJson.getJson();
        fileHandle = Gdx.files.absolute(projectPath + "/tiled_plugin.dt");
        load();
    }

    private void load() {
        if (!fileHandle.exists()) {
            dataToSave = new DataToSave();
            return;
        }

        String jsonString = fileHandle.readString();
        dataToSave = json.fromJson(DataToSave.class, jsonString);
    }

    public void save() {
        String dataString = json.toJson(dataToSave);
        fileHandle.writeString(dataString, false);
    }
}
