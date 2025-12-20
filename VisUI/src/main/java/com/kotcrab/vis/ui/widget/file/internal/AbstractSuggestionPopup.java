package com.kotcrab.vis.ui.widget.file.internal;

import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class AbstractSuggestionPopup extends PopupMenu {
    public static final int MAX_SUGGESTIONS = 10;

    final FileChooser chooser;

    public AbstractSuggestionPopup(FileChooser chooser) {
        super(chooser.getChooserStyle().popupMenuStyle);
        this.chooser = chooser;
    }

    protected MenuItem createMenuItem(String name) {
        MenuItem item = new MenuItem(name);
        item.getImageCell().size(0);
        item.getShortcutCell().space(0).pad(0);
        item.getSubMenuIconCell().size(0).space(0).pad(0);
        return item;
    }
}
