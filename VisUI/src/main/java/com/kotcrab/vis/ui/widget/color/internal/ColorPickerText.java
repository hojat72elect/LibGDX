package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.Locales;
import com.kotcrab.vis.ui.i18n.BundleText;

/**
 * Contains texts for chooser access via I18NBundle.
 *
 * @since 0.7.0
 */
public enum ColorPickerText implements BundleText {
    TITLE("title"),
    RESTORE("restore"),
    CANCEL("cancel"),
    OK("ok"),
    HEX("hex");

    private final String name;

    ColorPickerText(final String name) {
        this.name = name;
    }

    private static I18NBundle getBundle() {
        return Locales.getColorPickerBundle();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String get() {
        return getBundle().get(name);
    }

    @Override
    public final String format() {
        return getBundle().format(name);
    }

    @Override
    public final String format(final Object... arguments) {
        return getBundle().format(name, arguments);
    }

    @Override
    public final String toString() {
        return get();
    }
}
