package games.rednblack.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.Actor;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class PanelMediator<T extends Actor> extends Mediator<T> {

    public PanelMediator(String mediatorName, T viewComponent) {
        super(mediatorName, viewComponent);
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(ProjectManager.PROJECT_OPENED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                viewComponent.setVisible(true);
                break;
        }
    }
}
