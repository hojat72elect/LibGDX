package games.rednblack.editor.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class MoveCommandBuilder {

    Array<Object[]> payload;

    public MoveCommandBuilder() {
        payload = new Array<>();
    }

    public void setX(int entity, float x) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class, Sandbox.getInstance().getEngine());
        setXY(entity, x, transformComponent.y);
    }

    public void setY(int entity, float y) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class, Sandbox.getInstance().getEngine());
        setXY(entity, transformComponent.x, y);
    }

    public void setXY(int entity, float x, float y) {
        Object[] data = new Object[2];
        data[0] = EntityUtils.getEntityId(entity);
        data[1] = new Vector2(x, y);
        payload.add(data);
    }

    public void clear() {
        payload = new Array<>();
    }

    public void execute() {
        Facade.getInstance().sendNotification(MsgAPI.ACTION_ITEMS_MOVE_TO, payload);
    }
}
