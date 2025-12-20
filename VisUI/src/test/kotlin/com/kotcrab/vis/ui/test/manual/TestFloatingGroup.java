package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.FloatingGroup;

class TestFloatingGroup extends ApplicationAdapter {
    private Stage stage;

    @Override
    public void create() {
        VisUI.load();

        stage = new Stage(new ScreenViewport());
        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        TestWindow window = new TestWindow();
        TestCollapsible collapsible = new TestCollapsible();
        window.setKeepWithinParent(true);
        window.setPosition(110, 110);
        collapsible.setKeepWithinParent(true);
        collapsible.setPosition(200, 200);

        FloatingGroup floatingGroup = new FloatingGroup(1000, 600);
        floatingGroup.addActor(window);
        floatingGroup.addActor(collapsible);

        root.debugAll();
        root.left().bottom();
        root.add(floatingGroup).padLeft(100).padBottom(100);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        if (width == 0 && height == 0) return;
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}
