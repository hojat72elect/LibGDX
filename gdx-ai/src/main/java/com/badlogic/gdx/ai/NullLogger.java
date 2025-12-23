package com.badlogic.gdx.ai;

/**
 * A logger that never logs.
 */
public class NullLogger implements Logger {

    public NullLogger() {
    }

    @Override
    public void debug(String tag, String message) {
        // Do nothing
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        // Do nothing
    }

    @Override
    public void info(String tag, String message) {
        // Do nothing
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        // Do nothing
    }

    @Override
    public void error(String tag, String message) {
        // Do nothing
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        // Do nothing
    }
}
