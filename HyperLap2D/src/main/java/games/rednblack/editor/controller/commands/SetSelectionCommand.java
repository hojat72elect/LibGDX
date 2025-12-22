package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import games.rednblack.editor.renderer.components.NodeComponent;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.stage.Sandbox;

public class SetSelectionCommand extends RevertibleCommand {

    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.SetSelectionCommand";
    public static final String DONE = CLASS_NAME + ".DONE";

    private Array<String> previousSelectionIds;

    @Override
    public void doAction() {
        cancel();
        HashSet<Integer> previousSelection = new HashSet<>(Sandbox.getInstance().getSelector().getSelectedItems());
        previousSelectionIds = EntityUtils.getEntityId(previousSelection);

        if (getNotification().getBody() instanceof Integer) {
            int entity = getNotification().getBody();
            sandbox.getSelector().setSelection(entity, true);
        } else {
            Set<Integer> items = getNotification().getBody();

            if (items == null) {
                // deselect all
                sandbox.getSelector().setSelections(items, true);
                facade.sendNotification(DONE);
                return;
            }

            // check if items are in viewable element, if no - cancel
            NodeComponent nodeComponent = SandboxComponentRetriever.get(sandbox.getCurrentViewingEntity(), NodeComponent.class);
            for (Iterator<Integer> iterator = items.iterator(); iterator.hasNext(); ) {
                int item = iterator.next();
                if (!nodeComponent.children.contains(item, false)) {
                    iterator.remove();
                }
            }

            if (items.size() == 0) {
                cancel();
            } else {
                sandbox.getSelector().setSelections(items, true);
            }
        }

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        Sandbox.getInstance().getSelector().setSelections(EntityUtils.getByUniqueId(previousSelectionIds), true);
        facade.sendNotification(DONE);
    }
}
