package games.rednblack.editor.view;

import java.util.Set;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ZIndexComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class ItemControlMediator {

    private final SceneControlMediator sceneControl;

    private TransformComponent transformComponent;
    private ZIndexComponent zIndexComponent;

    public ItemControlMediator(SceneControlMediator sceneControl) {
        this.sceneControl = sceneControl;
    }

    public void itemZIndexChange(Set<Integer> currentSelection, boolean isUp) {
        for (Integer item : currentSelection) {
            zIndexComponent = SandboxComponentRetriever.get(item, ZIndexComponent.class);

            int ammount = 1;
            if (!isUp) ammount = -1;

            int setting = zIndexComponent.getZIndex() + ammount;
            if (setting < 0) setting = 0;
            zIndexComponent.setZIndex(setting);
        }
    }

    public void moveItemBy(int entity, float x, float y) {
        transformComponent = SandboxComponentRetriever.get(entity, TransformComponent.class);
        transformComponent.x += x;
        transformComponent.y += y;
    }
}
