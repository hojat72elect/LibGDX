package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.view.stage.Sandbox;

public class ReleaseSelectionCommand extends RevertibleCommand {

    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.ReleaseSelectionCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private Array<String> entityIds;

    @Override
    public void doAction() {
        cancel();
        Set<Integer> items = new HashSet<>(getNotification().<Collection<Integer>>getBody());
        Sandbox.getInstance().getSelector().releaseSelections(items);

        entityIds = EntityUtils.getEntityId(items);

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Set<Integer> items = EntityUtils.getByUniqueId(entityIds);
        Sandbox.getInstance().getSelector().addSelections(items);

        facade.sendNotification(DONE);
    }
}
