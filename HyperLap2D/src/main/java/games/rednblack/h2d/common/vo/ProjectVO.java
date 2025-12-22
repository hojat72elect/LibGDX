package games.rednblack.h2d.common.vo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import games.rednblack.editor.renderer.utils.HyperJson;

public class ProjectVO {

    public String projectName = "";

    public String projectVersion = null;

    public String projectMainExportPath = "";

    public String lastOpenScene = "";
    public String lastOpenResolution = "";
    public boolean lockLines = false;
    public float gridSize = 1;
    public Color backgroundColor = new Color(0, 0, 0, 1);
    public boolean box2dDebugRender = false;

    public TexturePackerVO texturePackerVO = new TexturePackerVO();

    public ArrayList<SceneConfigVO> sceneConfigs = new ArrayList<>();

    public String constructJsonString() {
        String str = "";
        Json json = HyperJson.getJson();
        str = json.toJson(this);
        return str;
    }
}