package games.rednblack.editor.view.ui.properties;

import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.interfaces.INotification;

public abstract class UIItemPropertiesMediator<V extends UIAbstractProperties> extends UIAbstractEntityPropertiesMediator<V> {

    public UIItemPropertiesMediator(String mediatorName, V viewComponent) {
        super(mediatorName, viewComponent);
    }

    @Override
    public void handleNotification(INotification notification) {
        if (!validReference())
            return;

        if (notification.getName().equals(viewComponent.getUpdateEventName())) {
            if (!lockUpdates) {
                translateViewToItemData();
                afterItemDataModified();
            }
        }

        switch (notification.getName()) {
            case MsgAPI.ITEM_DATA_UPDATED:
                if (observableReference == -1) return;
                onItemDataUpdate();
                break;
            default:
                break;
        }
    }

    protected void afterItemDataModified() {

    }

    private boolean validReference() {
        return observableReference != -1
                && sandbox.getEngine().getEntityManager().isActive(observableReference)
                && SandboxComponentRetriever.get(observableReference, MainItemComponent.class) != null;
    }
}
