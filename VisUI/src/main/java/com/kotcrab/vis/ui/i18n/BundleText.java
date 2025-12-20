package com.kotcrab.vis.ui.i18n;

/**
 * A simple interface for one text line of the bundle file.
 */
public interface BundleText {

    /**
     * @return name of the bundle text in the bundle file.
     */
    String getName();

    /**
     * @return text's unformatted message as it appears in the bundle.
     */
    String get();

    /**
     * @return text's formatted message without any arguments.
     */
    String format();

    /**
     * @return text's formatted message with the passes arguments filling bundle placeholders.
     */
    String format(Object... arguments);
}
