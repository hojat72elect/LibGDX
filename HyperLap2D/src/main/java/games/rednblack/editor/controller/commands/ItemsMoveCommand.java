package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class ItemsMoveCommand extends EntityModifyRevertibleCommand {

    public static final String TAG = ItemsMoveCommand.class.getCanonicalName();

    private final HashMap<String, Vector2> prevLocations = new HashMap<>();

    @Override
    public void doAction() {
        Array<Object[]> payload = getNotification().getBody();

        for (int i = 0; i < payload.size; i++) {
            Object[] itemData = payload.get(i);

            String entityUniqueId = (String) itemData[0];

            int entity = EntityUtils.getByUniqueId(entityUniqueId);
            Vector2 newLocation = (Vector2) itemData[1];

            TransformComponent transformComponent = SandboxComponentRetriever.get(entity, TransformComponent.class);

            Vector2 prevLocation = new Vector2(transformComponent.x, transformComponent.y);
            if (itemData.length > 2) {
                prevLocation = (Vector2) itemData[2];
            }
            prevLocations.put(EntityUtils.getEntityId(entity), prevLocation);

            transformComponent.x = newLocation.x;
            transformComponent.y = newLocation.y;

            // pining UI to update current item properties tools
            Facade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity, TAG);
        }
    }

    @Override
    public void undoAction() {
        for (Map.Entry<String, Vector2> entry : prevLocations.entrySet()) {
            String entityUniqueId = entry.getKey();
            Vector2 prevLocation = entry.getValue();

            int entity = EntityUtils.getByUniqueId(entityUniqueId);

            TransformComponent transformComponent = SandboxComponentRetriever.get(entity, TransformComponent.class);
            transformComponent.x = prevLocation.x;
            transformComponent.y = prevLocation.y;

            // pining UI to update current item properties tools
            Facade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity, TAG);
        }
    }
}
