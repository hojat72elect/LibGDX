package com.crashinvaders.vfx.demo.screens.demo.controllers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.crashinvaders.vfx.common.viewcontroller.ViewController;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActionContainer;

public class ScreenBackgroundViewController implements ViewController, ActionContainer {
    private static final String TAG = ScreenBackgroundViewController.class.getSimpleName();

    private final LmlParser lmlParser;
    private final AssetManager assets;

    public ScreenBackgroundViewController(LmlParser lmlParser, AssetManager assets) {
        this.lmlParser = lmlParser;
        this.assets = assets;
        lmlParser.getData().addActionContainer(TAG, this);
    }

    @Override
    public void onViewCreated(Group sceneRoot) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float delta) {

    }

    @LmlAction("createCheckerboardDrawable")
    Drawable createCheckerboardDrawable() {
        Texture texture = assets.get("bg-transparency-tile.png");
        return new TiledDrawable(new TextureRegion(texture));
    }
}
