package games.rednblack.editor.view.ui.dialog;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.view.menu.FileMenu;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.stage.UIStage;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class NewProjectDialogMediator extends Mediator<NewProjectDialog> {
    private static final String TAG = NewProjectDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public NewProjectDialogMediator() {
        super(NAME, new NewProjectDialog());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(FileMenu.NEW_PROJECT,
                NewProjectDialog.CREATE_BTN_CLICKED);
    }

    @Override
    public void onRegister() {
        super.onRegister();

        SettingsManager settingsManager = facade.retrieveProxy(SettingsManager.NAME);
        viewComponent.setDefaultWorkspacePath(settingsManager.getWorkspacePath().path());
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
        switch (notification.getName()) {
            case FileMenu.NEW_PROJECT:
                viewComponent.show(uiStage);
                break;
            case NewProjectDialog.CREATE_BTN_CLICKED:
                ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
                int originWidth = Integer.parseInt(viewComponent.getOriginWidth());
                int originHeight = Integer.parseInt(viewComponent.getOriginHeight());
                int pixelPerWorldUnit = Integer.parseInt(viewComponent.getPixelPerWorldUnit());
                projectManager.createNewProject(notification.getBody(), originWidth, originHeight, pixelPerWorldUnit);
                //TODO: this should be not here
                sandbox.loadCurrentProject();
                viewComponent.hide();
                break;
        }
    }
}
