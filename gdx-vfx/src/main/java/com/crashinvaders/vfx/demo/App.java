package com.crashinvaders.vfx.demo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.crashinvaders.vfx.common.PrioritizedInputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.crashinvaders.vfx.common.scene2d.actions.ActAction;
import com.crashinvaders.vfx.common.scene2d.actions.CustomTargetAction;
import com.crashinvaders.vfx.common.scene2d.actions.MoveByPathAction;
import com.crashinvaders.vfx.common.scene2d.actions.OptionalAction;
import com.crashinvaders.vfx.common.scene2d.actions.OriginAlignAction;
import com.crashinvaders.vfx.common.scene2d.actions.PostAction;
import com.crashinvaders.vfx.common.scene2d.actions.RemoveChildAction;
import com.crashinvaders.vfx.common.scene2d.actions.TimeModulationAction;
import com.crashinvaders.vfx.common.scene2d.actions.TransformAction;
import com.crashinvaders.vfx.common.scene2d.actions.UnfocusAction;
import com.crashinvaders.vfx.demo.screens.demo.DemoScreen;

public class App extends Game {

    private static App instance;

    static {
//		ShaderProgram.pedantic = false;
    }

    private final PrioritizedInputMultiplexer inputMultiplexer;

    private Screen mainScreen;

    public App() {
        inputMultiplexer = new PrioritizedInputMultiplexer();
        inputMultiplexer.setMaxPointers(Integer.MAX_VALUE);
        inputMultiplexer.addProcessor(new GlobalInputHandler(), -Integer.MAX_VALUE);
    }

    public static App inst() {
        if (instance == null) {
            throw new NullPointerException("App is not initialized yet!");
        }
        return instance;
    }

    @Override
    public void create() {
        Actions.registerAction(ActAction.class, ActAction::new);
        Actions.registerAction(CustomTargetAction.class, CustomTargetAction::new);
        Actions.registerAction(MoveByPathAction.class, MoveByPathAction::new);
        Actions.registerAction(OptionalAction.class, OptionalAction::new);
        Actions.registerAction(OriginAlignAction.class, OriginAlignAction::new);
        Actions.registerAction(PostAction.class, PostAction::new);
        Actions.registerAction(RemoveChildAction.class, RemoveChildAction::new);
        Actions.registerAction(TimeModulationAction.class, TimeModulationAction::new);
        Actions.registerAction(TransformAction.class, TransformAction::new);
        Actions.registerAction(UnfocusAction.class, UnfocusAction::new);

        instance = this;

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(inputMultiplexer);

        mainScreen = new DemoScreen();
//		mainScreen = new ExampleScreen();
        setScreen(mainScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        mainScreen.dispose();
    }

    //region Accessors
    public PrioritizedInputMultiplexer getInput() {
        return inputMultiplexer;
    }
    //endregion

    private void restartApp() {
        dispose();
        create();
    }

    private class GlobalInputHandler extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.F8:
                    // Restart the app.
                    restartApp();
                    return true;
                default:
                    return super.keyDown(keycode);
            }
        }
    }
}
