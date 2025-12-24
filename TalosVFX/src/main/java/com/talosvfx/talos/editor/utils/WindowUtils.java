package com.talosvfx.talos.editor.utils;

import com.talosvfx.talos.editor.dialogs.IWindowDialog;
import com.talosvfx.talos.editor.layouts.LayoutApp;

public interface WindowUtils {
    void openWindow(LayoutApp layoutApp);

    void openWindow(IWindowDialog dialog);
}
