package games.rednblack.editor.controller.commands;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

import games.rednblack.editor.utils.runtime.ComponentCloner;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class UpdateEntityComponentsCommand extends EntityModifyRevertibleCommand {

    private final Array<Component> backupComponents = new Array<>();
    private String entityId;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();

        int entity = (int) payload[0];
        entityId = EntityUtils.getEntityId(entity);
        Array<Component> components = (Array<Component>) payload[1];

        for (int i = 0; i < components.size; i++) {
            //backup the original component
            Component originalComponent = SandboxComponentRetriever.get(entity, components.get(i).getClass());
            backupComponents.add(ComponentCloner.get(originalComponent));

            //now modify the entity component from provided data
            ComponentCloner.set(originalComponent, components.get(i));
        }

        EntityUtils.refreshComponents(entity);

        Facade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {
        int entity = EntityUtils.getByUniqueId(entityId);
        for (int i = 0; i < backupComponents.size; i++) {
            Component entityComponent = SandboxComponentRetriever.get(entity, backupComponents.get(i).getClass());
            ComponentCloner.set(entityComponent, backupComponents.get(i));
        }

        EntityUtils.refreshComponents(entity);

        facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }
}
