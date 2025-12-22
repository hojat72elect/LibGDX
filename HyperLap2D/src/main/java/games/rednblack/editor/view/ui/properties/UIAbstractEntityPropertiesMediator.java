package games.rednblack.editor.view.ui.properties;

import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public abstract class UIAbstractEntityPropertiesMediator<V extends UIAbstractProperties> extends UIAbstractPropertiesMediator<Integer, V> {
    protected Sandbox sandbox;

    protected int observableReference;

    protected boolean lockUpdates = true;

    public UIAbstractEntityPropertiesMediator(String mediatorName, V viewComponent) {
        super(mediatorName, viewComponent);

        sandbox = Sandbox.getInstance();
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(MsgAPI.ITEM_DATA_UPDATED,
                viewComponent.getUpdateEventName());
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);


        if (notification.getName().equals(viewComponent.getUpdateEventName())) {
            if (!lockUpdates) {
                translateViewToItemData();
            }
        }

        switch (notification.getName()) {
            case MsgAPI.ITEM_DATA_UPDATED:
                onItemDataUpdate();
                break;
            default:
                break;
        }
    }

    @Override
    public void setItem(Integer item) {
        setItem((int) item);
    }

    public void setItem(int item) {
        observableReference = item;
        lockUpdates = true;
        translateObservableDataToView(observableReference);
        lockUpdates = false;
    }

    public void onItemDataUpdate() {
        lockUpdates = true;
        translateObservableDataToView(observableReference);
        lockUpdates = false;
    }

    @Override
    protected void translateObservableDataToView(Integer item) {
        translateObservableDataToView((int) item);
    }

    protected abstract void translateObservableDataToView(int item);

    protected abstract void translateViewToItemData();
}
