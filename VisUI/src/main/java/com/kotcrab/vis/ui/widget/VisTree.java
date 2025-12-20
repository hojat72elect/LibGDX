package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Focusable;
import com.kotcrab.vis.ui.VisUI;

/**
 * Does not provide additional features over standard {@link Tree}, however for proper VisUI focus border management VisTree
 * should be always preferred. Compatible with standard {@link Tree}
 *
 *  @see Tree
 */
public class VisTree<N extends Node, V> extends Tree<N, V> {
    public VisTree(String styleName) {
        super(VisUI.getSkin(), styleName);
        init();
    }

    public VisTree() {
        super(VisUI.getSkin());
        init();
    }

    public VisTree(TreeStyle style) {
        super(style);
        init();
    }

    private void init() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Focusable focusable = FocusManager.getFocusedWidget();
                if (!(focusable instanceof Actor) || !isAscendantOf((Actor) focusable)) {
                    FocusManager.resetFocus(getStage());
                }
                return false;
            }
        });
    }
}
