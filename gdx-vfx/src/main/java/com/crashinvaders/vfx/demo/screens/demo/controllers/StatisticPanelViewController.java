package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.common.lml.CommonLmlParser;
import com.crashinvaders.vfx.common.viewcontroller.LmlViewController;
import com.crashinvaders.vfx.common.viewcontroller.ViewControllerManager;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;
import com.github.czyzby.lml.annotation.LmlActor;

public class StatisticPanelViewController extends LmlViewController {

    @LmlActor("lblFboSize")
    Label lblFboSize;
    @LmlActor("lblFps")
    Label lblFps;

    private VfxManager vfxManager;

    public StatisticPanelViewController(ViewControllerManager viewControllers, CommonLmlParser lmlParser) {
        super(viewControllers, lmlParser);
    }

    @Override
    public void onViewCreated(Group sceneRoot) {
        super.onViewCreated(sceneRoot);
        processLmlFields(this);

        VfxWidgetGroup vfxGroup = sceneRoot.findActor("vfxGroup");
        vfxManager = vfxGroup.getVfxManager();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateFpsView();
        updateFboSizeView();
    }

    public void updateFboSizeView() {
        lblFboSize.setText(vfxManager.getWidth() + "x" + vfxManager.getHeight());
    }

    public void updateFpsView() {
        int fps = Gdx.graphics.getFramesPerSecond();
        lblFps.setText(String.valueOf(fps));
    }
}