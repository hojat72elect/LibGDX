package games.rednblack.editor.view.ui.box.bottom;

import com.badlogic.gdx.math.Vector3;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.proxy.ResolutionManager;
import games.rednblack.editor.renderer.data.ResolutionEntryVO;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.ui.dialog.CreateNewResolutionDialog;
import games.rednblack.h2d.common.H2DDialogs;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIResolutionBoxMediator extends Mediator<UIResolutionBox> {
    private static final String TAG = UIResolutionBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private ProjectManager projectManager;

    public UIResolutionBoxMediator() {
        super(NAME, new UIResolutionBox());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(ProjectManager.PROJECT_OPENED,
                UIResolutionBox.CHANGE_RESOLUTION_BTN_CLICKED,
                UIResolutionBox.DELETE_RESOLUTION_BTN_CLICKED,
                MsgAPI.ACTION_REPACK);
        interests.add(ResolutionManager.RESOLUTION_LIST_CHANGED,
                CreateNewResolutionDialog.CLOSE_DIALOG);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        ResolutionEntryVO resolutionEntryVO;
        switch (notification.getName()) {
            case ResolutionManager.RESOLUTION_LIST_CHANGED:
            case ProjectManager.PROJECT_OPENED:
                viewComponent.update();
                break;
            case CreateNewResolutionDialog.CLOSE_DIALOG:
                viewComponent.setCurrentResolution();
                break;
            case UIResolutionBox.CHANGE_RESOLUTION_BTN_CLICKED:
                resolutionEntryVO = notification.getBody();
                float zoom = sandbox.getZoomPercent();
                Vector3 cameraPos = new Vector3(sandbox.getCamera().position);
                String name = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectPath(), resolutionEntryVO.name);
                sandbox.loadScene(name);
                sandbox.setZoomPercent(zoom, false);
                sandbox.getCamera().position.set(cameraPos);
                break;
            case UIResolutionBox.DELETE_RESOLUTION_BTN_CLICKED:
                resolutionEntryVO = notification.getBody();
                H2DDialogs.showConfirmDialog(sandbox.getUIStage(),
                        "Delete Resolution",
                        "Are you sure you want to delete '" + resolutionEntryVO.name + "' resolution?",
                        new String[]{"Cancel", "Delete"}, new Integer[]{0, 1},
                        result -> {
                            if (result == 1) {
                                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                                resolutionManager.deleteResolution(resolutionEntryVO);
                                String sceneName = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                                sandbox.loadScene(sceneName);
                            }
                        }).padBottom(20).pack();
                break;

            case MsgAPI.ACTION_REPACK:
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.rePackProjectImagesForAllResolutions(true);
                String sceneName = sandbox.sceneControl.getCurrentSceneVO().sceneName;
                sandbox.loadScene(sceneName);
                break;
        }
    }
}
