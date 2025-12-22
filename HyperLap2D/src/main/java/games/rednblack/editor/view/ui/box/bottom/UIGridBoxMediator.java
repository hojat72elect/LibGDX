package games.rednblack.editor.view.ui.box.bottom;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIGridBoxMediator extends Mediator<UIGridBox> {
    private static final String TAG = UIGridBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIGridBoxMediator() {
        super(NAME, new UIGridBox());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(ProjectManager.PROJECT_OPENED,
                MsgAPI.GRID_SIZE_CHANGED,
                MsgAPI.LOCK_LINES_CHANGED,
                UIGridBox.GRID_SIZE_TEXT_FIELD_UPDATED);
        interests.add(UIGridBox.LOCK_LINES_CHECKBOX_FIELD_UPDATED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();

        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                viewComponent.update();
                viewComponent.setGridSize(sandbox.getGridSize());
                viewComponent.setLockLines(sandbox.getLockLines());
                break;
            case MsgAPI.GRID_SIZE_CHANGED:
                viewComponent.setGridSize(sandbox.getGridSize());
                break;
            case MsgAPI.LOCK_LINES_CHANGED:
                Boolean lockLinesChanged = notification.getBody();
                viewComponent.setLockLines(lockLinesChanged);
                break;
            case UIGridBox.GRID_SIZE_TEXT_FIELD_UPDATED:
                String body = notification.getBody();
                sandbox.setGridSize(Float.parseFloat(body));
                break;
            case UIGridBox.LOCK_LINES_CHECKBOX_FIELD_UPDATED:
                Boolean lockLines = notification.getBody();
                sandbox.setLockLines(lockLines);
                break;
            default:
                break;
        }
    }
}
