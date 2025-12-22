package games.rednblack.editor.controller.commands;

import games.rednblack.editor.renderer.components.LayerMapComponent;
import games.rednblack.editor.renderer.components.NodeComponent;
import games.rednblack.editor.renderer.components.ZIndexComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.stage.Sandbox;

public class RenameLayerCommand extends EntityModifyRevertibleCommand {
    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.RenameLayerCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    public static String[] payload(String oldName, String newName) {
        String[] payload = new String[2];
        payload[0] = oldName;
        payload[1] = newName;

        return payload;
    }

    @Override
    public void doAction() {
        String[] payload = getNotification().getBody();
        String oldName = payload[0];
        String newName = payload[1];

        if (oldName.equals(newName) || newName.isEmpty()) {
            cancel();
        }

        renameLayer(oldName, newName);

        facade.sendNotification(DONE);
    }

    @Override
    public void undoAction() {
        String[] payload = getNotification().getBody();
        String oldName = payload[0];
        String newName = payload[1];

        renameLayer(newName, oldName);

        facade.sendNotification(DONE);
    }

    private void renameLayer(String fromName, String toName) {
        if (isCancelled) return;

        int viewEntity = Sandbox.getInstance().getCurrentViewingEntity();
        NodeComponent nodeComponent = SandboxComponentRetriever.get(viewEntity, NodeComponent.class);
        LayerMapComponent layerMapComponent = SandboxComponentRetriever.get(viewEntity, LayerMapComponent.class);

        if (layerMapComponent.getLayer(toName.hashCode()) != null) {
            cancel();
            return;
        }

        layerMapComponent.rename(fromName, toName);

        for (int childEntity : nodeComponent.children) {
            ZIndexComponent zIndexComponent = SandboxComponentRetriever.get(childEntity, ZIndexComponent.class);
            if (zIndexComponent.getLayerName().equals(fromName)) {
                zIndexComponent.setLayerName(toName);
            }
        }
    }
}
