package games.rednblack.editor.view.ui.panel;

import java.util.Set;

import games.rednblack.editor.controller.commands.CustomVariableModifyCommand;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.menu.WindowMenu;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.stage.UIStage;
import games.rednblack.editor.view.ui.properties.panels.UIBasicItemProperties;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class CustomVariablesPanelMediator extends Mediator<CustomVariablesPanel> {
    private static final String TAG = CustomVariablesPanelMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    private int observable = -1;

    public CustomVariablesPanelMediator() {
        super(NAME, new CustomVariablesPanel());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        viewComponent.setEmptyMsg("No item selected.");
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(MsgAPI.ITEM_SELECTION_CHANGED,
                MsgAPI.EMPTY_SPACE_CLICKED,
                UIBasicItemProperties.CUSTOM_VARS_BUTTON_CLICKED,
                CustomVariablesPanel.ADD_BUTTON_PRESSED);
        interests.add(CustomVariablesPanel.DELETE_BUTTON_PRESSED,
                WindowMenu.CUSTOM_VARIABLES_EDITOR_OPEN,
                CustomVariableModifyCommand.DONE);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        switch (notification.getName()) {
            case WindowMenu.CUSTOM_VARIABLES_EDITOR_OPEN:
            case UIBasicItemProperties.CUSTOM_VARS_BUTTON_CLICKED:
                if (observable == -1) setObservable(Sandbox.getInstance().getRootEntity());
                viewComponent.show(uiStage);
                break;
            case MsgAPI.ITEM_SELECTION_CHANGED:
                Set<Integer> selection = notification.getBody();
                if (selection.size() == 1) {
                    setObservable(selection.iterator().next());
                } else {
                    if (selection.isEmpty()) {
                        setObservable(Sandbox.getInstance().getRootEntity());
                    } else {
                        viewComponent.setEmptyMsg("Multiple items selected.");
                    }
                }
                break;
            case MsgAPI.EMPTY_SPACE_CLICKED:
                setObservable(Sandbox.getInstance().getRootEntity());
                break;
            case CustomVariablesPanel.ADD_BUTTON_PRESSED:
                setVariable();
                break;
            case CustomVariablesPanel.DELETE_BUTTON_PRESSED:
                removeVariable(notification.getBody());
                break;
            case CustomVariableModifyCommand.DONE:
                updateView();
                break;
        }
    }

    private void setVariable() {
        String key = viewComponent.getKey();
        String value = viewComponent.getValue();

        sendNotification(MsgAPI.CUSTOM_VARIABLE_MODIFY, CustomVariableModifyCommand.addCustomVariable(observable, key, value));
    }

    private void removeVariable(String key) {
        sendNotification(MsgAPI.CUSTOM_VARIABLE_MODIFY, CustomVariableModifyCommand.removeCustomVariable(observable, key));
    }

    private void setObservable(int item) {
        observable = item;
        updateView();
        viewComponent.setKeyFieldValue("");
        viewComponent.setValueFieldValue("");
    }

    private void updateView() {
        if (observable == -1) {
            viewComponent.setEmptyMsg("No item selected.");
        } else {
            MainItemComponent mainItemComponent = SandboxComponentRetriever.get(observable, MainItemComponent.class);
            viewComponent.updateView(mainItemComponent.customVariables);
        }
    }
}
