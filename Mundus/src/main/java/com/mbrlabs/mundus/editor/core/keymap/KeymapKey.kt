package com.mbrlabs.mundus.editor.core.keymap

enum class KeymapKey(val type: KeymapKeyType) {
    MOVE_FORWARD(KeymapKeyType.KEY),
    MOVE_BACK(KeymapKeyType.KEY),
    STRAFE_LEFT(KeymapKeyType.KEY),
    STRAFE_RIGHT(KeymapKeyType.KEY),
    MOVE_UP(KeymapKeyType.KEY),
    MOVE_DOWN(KeymapKeyType.KEY),
    FULLSCREEN(KeymapKeyType.KEY),
    UNDO(KeymapKeyType.KEY),
    REDO(KeymapKeyType.KEY),
    SAVE_PROJECT(KeymapKeyType.KEY),
    TRANSLATE_TOOL(KeymapKeyType.KEY),
    ROTATE_TOOL(KeymapKeyType.KEY),
    SCALE_TOOL(KeymapKeyType.KEY),
    SELECT_TOOL(KeymapKeyType.KEY),
    DEBUG_RENDER_MODE(KeymapKeyType.KEY),
    WIREFRAME_RENDER_MODE(KeymapKeyType.KEY),

    OBJECT_SELECTION(KeymapKeyType.BUTTON),
    LOOK_AROUND(KeymapKeyType.BUTTON);
}
