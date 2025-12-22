package games.rednblack.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;

import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.view.tools.Tool;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import games.rednblack.puremvc.Facade;

public class UIToolBox extends VisTable {
    private final ButtonGroup<VisImageButton> toolsButtonGroup;

    private final HashMap<String, VisImageButton> buttonMap = new HashMap<>();

    public UIToolBox() {
        toolsButtonGroup = new ButtonGroup<>();
    }

    public void addToolButton(String name, Tool tool) {
        VisImageButton button = createButton("tool-" + name, name);
        String toolTip = tool.getTitle() + (tool.getShortcut() != null ? " (" + tool.getShortcut() + ")" : "");
        StandardWidgetsFactory.addTooltip(button, toolTip);

        buttonMap.put(name, button);
        add(button).width(31).height(31).row();
    }

    public void addToolButton(String name, VisImageButton.VisImageButtonStyle btnStyle, Tool tool) {
        VisImageButton button = createButton(btnStyle, name);
        String toolTip = tool.getTitle() + (tool.getShortcut() != null ? " (" + tool.getShortcut() + ")" : "");
        StandardWidgetsFactory.addTooltip(button, toolTip);


        buttonMap.put(name, button);
        add(button).width(31).height(31).row();
    }

    private VisImageButton createButton(String styleName, String toolId) {
        VisImageButton visImageButton = new VisImageButton(styleName);
        toolsButtonGroup.add(visImageButton);
        visImageButton.addListener(new ToolboxButtonClickListener(toolId));
        return visImageButton;
    }

    private VisImageButton createButton(VisImageButton.VisImageButtonStyle btnStyle, String toolId) {
        VisImageButton visImageButton = new VisImageButton(btnStyle);
        toolsButtonGroup.add(visImageButton);
        visImageButton.addListener(new ToolboxButtonClickListener(toolId));
        return visImageButton;
    }

    public void setCurrentTool(String tool) {
        buttonMap.get(tool).setChecked(true);
    }

    private static class ToolboxButtonClickListener extends ClickListener {

        private final String toolId;

        public ToolboxButtonClickListener(String toolId) {
            this.toolId = toolId;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Facade facade = Facade.getInstance();
            facade.sendNotification(MsgAPI.TOOL_CLICKED, toolId);
        }
    }
}
