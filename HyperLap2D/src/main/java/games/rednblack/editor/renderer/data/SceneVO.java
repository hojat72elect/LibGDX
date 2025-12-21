package games.rednblack.editor.renderer.data;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import games.rednblack.editor.renderer.utils.HyperJson;

public class SceneVO {

    public String sceneName = "";

    public CompositeItemVO composite;

    public PhysicsPropertiesVO physicsPropertiesVO = new PhysicsPropertiesVO();
    public LightsPropertiesVO lightsPropertiesVO = new LightsPropertiesVO();
    public ShaderVO shaderVO = new ShaderVO();

    public ArrayList<Float> verticalGuides = new ArrayList<Float>();
    public ArrayList<Float> horizontalGuides = new ArrayList<Float>();

    public SceneVO() {

    }

    public SceneVO(SceneVO vo) {
        sceneName = vo.sceneName;
        composite = new CompositeItemVO(vo.composite);
        physicsPropertiesVO = new PhysicsPropertiesVO(vo.physicsPropertiesVO);
        lightsPropertiesVO = vo.lightsPropertiesVO;
        shaderVO.set(vo.shaderVO);
    }

    @Override
    public String toString() {
        return sceneName;
    }

    public String constructJsonString() {
        String str = "";
        Json json = HyperJson.getJson();
        str = json.toJson(this);
        return str;
    }
}
