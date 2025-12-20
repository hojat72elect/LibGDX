package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.kotcrab.vis.ui.VisUI;

/**
 * Compatible with {@link ProgressBar}. Does not provide additional features.
 *
 * @see ProgressBar
 */
public class VisProgressBar extends ProgressBar {
    public VisProgressBar(float min, float max, float stepSize, boolean vertical) {
        this(min, max, stepSize, vertical, VisUI.getSkin().get("default-" + (vertical ? "vertical" : "horizontal"),
                ProgressBarStyle.class));
    }

    public VisProgressBar(float min, float max, float stepSize, boolean vertical, String styleName) {
        this(min, max, stepSize, vertical, VisUI.getSkin().get(styleName, ProgressBarStyle.class));
    }

    public VisProgressBar(float min, float max, float stepSize, boolean vertical, ProgressBarStyle style) {
        super(min, max, stepSize, vertical, style);
    }
}
