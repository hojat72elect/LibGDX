package games.rednblack.h2d.common.vo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

import games.rednblack.editor.renderer.utils.HyperJson;

public class EditorConfigVO {
    public static final String EDITOR_CONFIG_FILE = "config.pit";
    private static final Json json = HyperJson.getJson();
    public String lastOpenedSystemPath = "";
    public String lastImportedSystemPath = "";
    public String keyBindingLayout = "default";

    public long totalSpentTime = 0;

    public boolean disableAmbientComposite = true;
    public boolean showBoundingBoxes = false;
    public float scrollVelocity = 30f;
    public boolean autoSave = false;
    public boolean enablePlugins = true;
    public Color backgroundColor = new Color(0.15f, 0.15f, 0.15f, 1.0f);

    public float uiScaleDensity = 1f;

    public int msaaSamples = 4;
    public int fpsLimit = 60;
    public boolean useANGLEGLES2 = true;
    public boolean failSafeException = false;

    //Map to store plugin storage
    public Map<String, Map<String, Object>> pluginStorage = new HashMap<>();
    public HashMap<String, Boolean> enabledFilters = new HashMap<>();

    public String constructJsonString() {
        String str = "";
        json.setOutputType(JsonWriter.OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
