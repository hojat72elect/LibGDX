package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;

/**
 * Compatible with {@link List}. Does not provide additional features however for proper VisUI focus management List
 * should be always preferred.
 *
 * @see List
 */
public class VisList<T> extends List<T> {

    public VisList() {
        super(VisUI.getSkin());
        init();
    }

    public VisList(String styleName) {
        super(VisUI.getSkin(), styleName);
        init();
    }

    public VisList(ListStyle style) {
        super(style);
        init();
    }

    private void init() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                FocusManager.resetFocus(getStage());
                return false;
            }
        });
    }
}
