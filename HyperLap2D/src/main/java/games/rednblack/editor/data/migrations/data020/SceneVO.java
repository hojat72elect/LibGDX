package games.rednblack.editor.data.migrations.data020;

import java.util.ArrayList;

import games.rednblack.editor.renderer.data.LightsPropertiesVO;
import games.rednblack.editor.renderer.data.PhysicsPropertiesVO;

public class SceneVO {
    public String sceneName = "";

    public CompositeVO composite;

    public PhysicsPropertiesVO physicsPropertiesVO = new PhysicsPropertiesVO();
    public LightsPropertiesVO lightsPropertiesVO = new LightsPropertiesVO();

    public ArrayList<Float> verticalGuides = new ArrayList<Float>();
    public ArrayList<Float> horizontalGuides = new ArrayList<Float>();
}
