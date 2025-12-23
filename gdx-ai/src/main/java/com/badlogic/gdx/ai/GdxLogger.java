package com.badlogic.gdx.ai;

import com.badlogic.gdx.Gdx;

public class GdxLogger implements Logger {

    public GdxLogger() {
    }

    @Override
    public void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        Gdx.app.debug(tag, message, exception);
    }

    @Override
    public void info(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        Gdx.app.log(tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        Gdx.app.error(tag, message, exception);
    }
}
