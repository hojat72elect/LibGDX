package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.viewport.Viewport;

import games.rednblack.editor.renderer.components.ParentNodeComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;

public class CompositeCameraChangeCommand extends RevertibleCommand {

    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.CompositeCameraChangeCommand";
    public static final String DONE = CLASS_NAME + "DONE";

    private String previousViewEntityId;
    private String enteringInto;

    private boolean wasPrevSelected = false;

    @Override
    public void doAction() {
        cancel();
        int entity = getNotification().getBody();
        int oldEntity = sandbox.getCurrentViewingEntity();

        // check if entity is selected
        wasPrevSelected = sandbox.getSelector().isSelected(entity);

        if (enteringInto == null) enteringInto = EntityUtils.getEntityId(entity);
        if (previousViewEntityId == null) previousViewEntityId = EntityUtils.getEntityId(oldEntity);

        ViewPortComponent viewPortComponent = SandboxComponentRetriever.get(oldEntity, ViewPortComponent.class);
        Viewport currViewport = viewPortComponent.viewPort;
        int currPPWU = viewPortComponent.pixelsPerWU;
        sandbox.getEngine().edit(oldEntity).remove(ViewPortComponent.class);

        ViewPortComponent newViewPortComponent = sandbox.getEngine().edit(entity).create(ViewPortComponent.class);
        newViewPortComponent.viewPort = currViewport;
        newViewPortComponent.pixelsPerWU = currPPWU;

        sandbox.setCurrentViewingEntity(entity);

        sandbox.getSelector().clearSelections();

        TransformComponent transformComponent = SandboxComponentRetriever.get(entity, TransformComponent.class);
        TransformComponent previousTransformComponent = SandboxComponentRetriever.get(oldEntity, TransformComponent.class);
        ParentNodeComponent parentNodeComponent = SandboxComponentRetriever.get(entity, ParentNodeComponent.class);
        if (parentNodeComponent == null || oldEntity != parentNodeComponent.parentEntity)
            previousTransformComponent.enableTransform();
        transformComponent.disableTransform();

        facade.sendNotification(DONE, enteringInto);
        facade.sendNotification(MsgAPI.EMPTY_SPACE_CLICKED);

        sandbox.overrideAmbientLightInComposite();
        sandbox.adjustCameraInComposites();
    }

    @Override
    public void undoAction() {
        int oldEntity = EntityUtils.getByUniqueId(previousViewEntityId);
        int currEntity = sandbox.getCurrentViewingEntity();

        ViewPortComponent viewPortComponent = SandboxComponentRetriever.get(currEntity, ViewPortComponent.class);
        Viewport currViewport = viewPortComponent.viewPort;
        int currPPWU = viewPortComponent.pixelsPerWU;
        sandbox.getEngine().edit(currEntity).remove(ViewPortComponent.class);

        ViewPortComponent newViewPortComponent = sandbox.getEngine().edit(oldEntity).create(ViewPortComponent.class);
        newViewPortComponent.viewPort = currViewport;
        newViewPortComponent.pixelsPerWU = currPPWU;
        sandbox.setCurrentViewingEntity(oldEntity);

        facade.sendNotification(DONE, previousViewEntityId);

        TransformComponent transformComponent = SandboxComponentRetriever.get(currEntity, TransformComponent.class);
        TransformComponent previousTransformComponent = SandboxComponentRetriever.get(oldEntity, TransformComponent.class);
        previousTransformComponent.disableTransform();
        transformComponent.enableTransform();

        if (wasPrevSelected) {
            sandbox.getSelector().setSelection(EntityUtils.getByUniqueId(enteringInto), true);
        }

        sandbox.overrideAmbientLightInComposite();
    }
}
