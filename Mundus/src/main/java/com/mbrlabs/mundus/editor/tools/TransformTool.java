package com.mbrlabs.mundus.editor.tools;

import com.badlogic.gdx.graphics.Color;
import com.mbrlabs.mundus.editor.core.keymap.KeyboardShortcutManager;
import com.mbrlabs.mundus.editor.core.project.ProjectManager;
import com.mbrlabs.mundus.editor.history.CommandHistory;
import com.mbrlabs.mundus.editor.preferences.MundusPreferencesManager;
import com.mbrlabs.mundus.editor.tools.picker.GameObjectPicker;
import com.mbrlabs.mundus.editor.tools.picker.ToolHandlePicker;
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent;

public abstract class TransformTool extends SelectionTool {

    protected static final int X_HANDLE_ID = 0;
    protected static final int Y_HANDLE_ID = 1;
    protected static final int Z_HANDLE_ID = 2;
    protected static final int XZ_HANDLE_ID = 3;
    protected static final int XYZ_HANDLE_ID = 4;
    protected static Color COLOR_X = Color.RED;
    protected static Color COLOR_Y = Color.GREEN;
    protected static Color COLOR_Z = Color.BLUE;
    protected static Color COLOR_XZ = Color.CYAN;
    protected static Color COLOR_XYZ = Color.LIGHT_GRAY;
    protected static Color COLOR_SELECTED = Color.YELLOW;
    protected ToolHandlePicker handlePicker;
    protected GameObjectModifiedEvent gameObjectModifiedEvent;
    public TransformTool(final ProjectManager projectManager,
                         final GameObjectPicker goPicker,
                         final ToolHandlePicker handlePicker,
                         final CommandHistory history,
                         final MundusPreferencesManager globalPreferencesManager,
                         final KeyboardShortcutManager keyboardShortcutManager) {
        super(projectManager, goPicker, history, globalPreferencesManager, keyboardShortcutManager);
        this.handlePicker = handlePicker;

        gameObjectModifiedEvent = new GameObjectModifiedEvent(null);
    }

    protected abstract void scaleHandles();

    protected abstract void translateHandles();

    protected abstract void rotateHandles();

    protected enum TransformState {
        TRANSFORM_X, TRANSFORM_Y, TRANSFORM_Z, TRANSFORM_XZ, TRANSFORM_XYZ, IDLE
    }
}
