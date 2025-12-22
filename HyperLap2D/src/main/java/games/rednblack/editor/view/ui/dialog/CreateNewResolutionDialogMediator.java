package games.rednblack.editor.view.ui.dialog;

import games.rednblack.editor.proxy.ResolutionManager;
import games.rednblack.editor.renderer.data.ResolutionEntryVO;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.stage.UIStage;
import games.rednblack.editor.view.ui.box.bottom.UIResolutionBox;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class CreateNewResolutionDialogMediator extends Mediator<CreateNewResolutionDialog> {
    private static final String TAG = CreateNewResolutionDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public CreateNewResolutionDialogMediator() {
        super(NAME, new CreateNewResolutionDialog());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(UIResolutionBox.CREATE_NEW_RESOLUTION_BTN_CLICKED,
                CreateNewResolutionDialog.CREATE_BTN_CLICKED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
        switch (notification.getName()) {
            case UIResolutionBox.CREATE_NEW_RESOLUTION_BTN_CLICKED:
                viewComponent.show(uiStage);
                break;
            case CreateNewResolutionDialog.CREATE_BTN_CLICKED:
                ResolutionEntryVO resolutionEntryVO = notification.getBody();
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.createNewResolution(resolutionEntryVO);
                viewComponent.hide();
                break;
        }
    }
}
