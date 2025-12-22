package games.rednblack.editor.view.stage;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;

import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.H2DDialogs;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIStageMediator extends Mediator<UIStage> {
    private static final String TAG = UIStageMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIStageMediator() {
        super(NAME, new UIStage());
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(MsgAPI.SHOW_ADD_LIBRARY_DIALOG,
                MsgAPI.SAVE_EDITOR_CONFIG);
    }

    @Override
    public void handleNotification(INotification notification) {
        switch (notification.getName()) {
            case MsgAPI.SHOW_ADD_LIBRARY_DIALOG:
                Sandbox sandbox = Sandbox.getInstance();

                int item = notification.getBody();

                MainItemComponent mainItemComponent = SandboxComponentRetriever.get(item, MainItemComponent.class);

                H2DDialogs.showInputDialog(sandbox.getUIStage(), "New Library Item", "Unique Name", false, new InputDialogListener() {
                    @Override
                    public void finished(String input) {
                        Object[] payload = new Object[2];
                        payload[0] = item;
                        payload[1] = input;
                        facade.sendNotification(MsgAPI.ACTION_ADD_TO_LIBRARY, payload);
                    }

                    @Override
                    public void canceled() {

                    }
                }).setText(mainItemComponent.libraryLink, true).pack();
                break;
            case MsgAPI.SAVE_EDITOR_CONFIG:
                Gdx.app.postRunnable(() -> getViewComponent().updateViewportDensity());
                break;
        }
    }
}
