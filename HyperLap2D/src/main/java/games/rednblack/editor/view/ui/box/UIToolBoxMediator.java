package games.rednblack.editor.view.ui.box;

import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageButton;

import java.util.HashMap;

import games.rednblack.editor.view.stage.SandboxMediator;
import games.rednblack.editor.view.stage.tools.ConeLightTool;
import games.rednblack.editor.view.stage.tools.PanTool;
import games.rednblack.editor.view.stage.tools.PointLightTool;
import games.rednblack.editor.view.stage.tools.PolygonTool;
import games.rednblack.editor.view.stage.tools.SelectionTool;
import games.rednblack.editor.view.stage.tools.TextTool;
import games.rednblack.editor.view.stage.tools.TransformTool;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.view.tools.Tool;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIToolBoxMediator extends Mediator<UIToolBox> {
    private static final String TAG = UIToolBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<String, Tool> toolList;
    private String currentTool;

    public UIToolBoxMediator() {
        super(NAME, new UIToolBox());
    }

    @Override
    public void onRegister() {
        toolList = new HashMap<>();
        initToolList();
        currentTool = SelectionTool.NAME;
    }

    private void initToolList() {
        toolList.put(SelectionTool.NAME, new SelectionTool());
        viewComponent.addToolButton(SelectionTool.NAME, toolList.get(SelectionTool.NAME));
        toolList.put(TransformTool.NAME, new TransformTool());
        viewComponent.addToolButton(TransformTool.NAME, toolList.get(TransformTool.NAME));
        toolList.put(PolygonTool.NAME, new PolygonTool());
        viewComponent.addToolButton(PolygonTool.NAME, toolList.get(PolygonTool.NAME));

        viewComponent.add(new Separator("tool")).padTop(2).padBottom(2).fill().expand().row();

        toolList.put(TextTool.NAME, new TextTool());
        viewComponent.addToolButton(TextTool.NAME, toolList.get(TextTool.NAME));
        toolList.put(PointLightTool.NAME, new PointLightTool());
        viewComponent.addToolButton(PointLightTool.NAME, toolList.get(PointLightTool.NAME));
        toolList.put(ConeLightTool.NAME, new ConeLightTool());
        viewComponent.addToolButton(ConeLightTool.NAME, toolList.get(ConeLightTool.NAME));
        toolList.put(PanTool.NAME, new PanTool());
    }

    public void addTool(String toolName, VisImageButton.VisImageButtonStyle toolBtnStyle, boolean addSeparator, Tool tool) {
        toolList.put(toolName, tool);
        if (addSeparator) {
            viewComponent.add(new Separator("tool")).padTop(2).padBottom(2).fill().expand().row();
        }
        viewComponent.addToolButton(toolName, toolBtnStyle, tool);

        facade.sendNotification(toolName, tool);
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(MsgAPI.TOOL_CLICKED,
                SandboxMediator.SANDBOX_TOOL_CHANGED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case MsgAPI.TOOL_CLICKED:
                currentTool = notification.getBody();
                facade.sendNotification(MsgAPI.TOOL_SELECTED, currentTool);
                break;
            case SandboxMediator.SANDBOX_TOOL_CHANGED:
                if (notification.getBody() instanceof Tool) {
                    currentTool = ((Tool) notification.getBody()).getName();
                } else {
                    currentTool = notification.getBody();
                }

                setCurrentTool(currentTool);
                break;
        }
    }

    public void setCurrentTool(String tool) {
        viewComponent.setCurrentTool(tool);
        currentTool = tool;
    }

    public HashMap<String, Tool> getToolList() {
        return toolList;
    }
}
