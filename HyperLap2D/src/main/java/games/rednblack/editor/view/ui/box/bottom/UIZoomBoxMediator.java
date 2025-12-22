package games.rednblack.editor.view.ui.box.bottom;

import org.apache.commons.lang3.math.NumberUtils;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIZoomBoxMediator extends Mediator<UIZoomBox> {
    private static final String TAG = UIZoomBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String PREFIX = "games.rednblack.editor.view.ui.box.bottom.UIZoomBoxMediator.";

    public UIZoomBoxMediator() {
        super(NAME, new UIZoomBox());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(ProjectManager.PROJECT_OPENED,
                UIZoomBox.ZOOM_SHIFT_REQUESTED,
                UIZoomBox.ZOOM_VALUE_CHANGED,
                MsgAPI.ZOOM_CHANGED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                viewComponent.update();
                viewComponent.setCurrentZoom(sandbox.getZoomPercent() + "");
                break;
            case UIZoomBox.ZOOM_SHIFT_REQUESTED:
                float zoomDevider = notification.getBody();
                sandbox.zoomDivideBy(zoomDevider);
                break;
            case UIZoomBox.ZOOM_VALUE_CHANGED:
                sandbox.setZoomPercent(NumberUtils.toInt(viewComponent.getCurrentZoom()), false);
                break;
            case MsgAPI.ZOOM_CHANGED:
                viewComponent.setCurrentZoom(sandbox.getZoomPercent() + "");
                break;
        }
    }
}
