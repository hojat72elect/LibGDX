package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.LmlViewController;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;
import com.github.czyzby.lml.annotation.LmlAction;

public class VfxViewController extends LmlViewController {

    private VfxManager vfxManager;
    private WidgetGroup canvasRoot;

    public VfxViewController(ViewControllerManager viewControllers, CommonLmlParser lmlParser) {
        super(viewControllers, lmlParser);
    }

    public VfxManager getVfxManager() {
        return vfxManager;
    }

    public Group getCanvasRoot() {
        return canvasRoot;
    }

    @LmlAction("createCanvas")
    Actor createCanvas() {
        canvasRoot = new WidgetGroup();
        canvasRoot.setName("canvasRoot");
        canvasRoot.setFillParent(true);

        VfxWidgetGroup vfxGroup = new VfxWidgetGroup(Pixmap.Format.RGBA8888);
        vfxGroup.setName("vfxGroup");
        vfxGroup.addActor(canvasRoot);
        vfxGroup.setMatchWidgetSize(true);

        vfxManager = vfxGroup.getVfxManager();
        vfxManager.setBlendingEnabled(false);

        return vfxGroup;
    }
}
