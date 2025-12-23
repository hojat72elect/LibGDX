package com.badlogic.gdx.ai.tests.utils.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A {@code TabSelectionChangeListener} for a {@link TabbedPane} widget.
 */
public abstract class TabSelectionChangeListener extends ChangeListener {

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if (event.getListenerActor() == event.getTarget()) {
            tabSelectionChanged(event, actor);
        }
    }

    public abstract void tabSelectionChanged(ChangeEvent event, Actor actor);
}
