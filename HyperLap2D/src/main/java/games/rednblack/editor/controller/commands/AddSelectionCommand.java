package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.Array;

import java.util.Set;

import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.view.stage.Sandbox;

public class AddSelectionCommand extends RevertibleCommand {

    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.AddSelectionCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Array<String> entityIds;

    @Override
    public void doAction() {
        cancel();
        if (entityIds == null) {
            Set<Integer> items = getNotification().getBody();
            entityIds = EntityUtils.getEntityId(items);
        }

        Set<Integer> items = EntityUtils.getByUniqueId(entityIds);
        Sandbox.getInstance().getSelector().addSelections(items);
        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Set<Integer> items = EntityUtils.getByUniqueId(entityIds);
        Sandbox.getInstance().getSelector().releaseSelections(items);
        facade.sendNotification(DONE);
    }
}
