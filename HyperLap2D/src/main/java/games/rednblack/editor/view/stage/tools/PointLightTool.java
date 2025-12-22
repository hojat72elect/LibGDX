package games.rednblack.editor.view.stage.tools;

import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.factory.ItemFactory;
import games.rednblack.editor.renderer.components.light.LightObjectComponent;
import games.rednblack.editor.renderer.data.LightVO;
import games.rednblack.editor.renderer.factory.EntityFactory;

public class PointLightTool extends ItemDropTool {

    public static final String NAME = "POINT_LIGHT_TOOL";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortcut() {
        return null;
    }

    @Override
    public String getTitle() {
        return "Point Light Tool";
    }

    @Override
    public int putItem(float x, float y) {
        LightVO vo = new LightVO();
        vo.type = LightObjectComponent.LightType.POINT;
        vo.distance = vo.distance / sandbox.getPixelPerWU();

        return ItemFactory.get().createLightItem(vo, new Vector2(x, y));
    }

    @Override
    public int[] listItemFilters() {
        int[] filter = {EntityFactory.LIGHT_TYPE};
        return filter;
    }
}
