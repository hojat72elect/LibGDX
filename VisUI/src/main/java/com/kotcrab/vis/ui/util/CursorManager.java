package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;

/**
 * Manages default cursor of VisUI app. If you are using custom cursor you must set it here otherwise some VisUI widget
 * that changes cursors will reset it to default system cursor.
 *
 * @since 1.1.2
 */
public class CursorManager {
    private static Cursor defaultCursor;
    private static Cursor.SystemCursor defaultSystemCursor = Cursor.SystemCursor.Arrow;
    private static boolean systemCursorAsDefault = true;

    /**
     * Sets cursor that will be used as default when {@link #restoreDefaultCursor()} is called, for example by VisUI widget.
     *
     * @param defaultCursor default cursor, can't be null
     */
    public static void setDefaultCursor(Cursor defaultCursor) {
        if (defaultCursor == null) throw new IllegalArgumentException("defaultCursor can't be null");
        CursorManager.defaultCursor = defaultCursor;
        CursorManager.systemCursorAsDefault = false;
    }

    /**
     * Sets cursor that will be used as default when {@link #restoreDefaultCursor()} is called, for example by Vis widget.
     *
     * @param defaultCursor default cursor from {@link Cursor.SystemCursor}, can't be null
     */
    public static void setDefaultCursor(Cursor.SystemCursor defaultCursor) {
        if (defaultCursor == null) throw new IllegalArgumentException("defaultCursor can't be null");
        CursorManager.defaultSystemCursor = defaultCursor;
        CursorManager.systemCursorAsDefault = true;
    }

    /**
     * Restores currently used cursor to default one.
     */
    public static void restoreDefaultCursor() {
        if (systemCursorAsDefault) {
            Gdx.graphics.setSystemCursor(defaultSystemCursor);
        } else {
            Gdx.graphics.setCursor(defaultCursor);
        }
    }
}
