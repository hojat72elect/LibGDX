package com.badlogic.gdx.ai;

/**
 * A logger that writes to the standard output.
 */
public class StdoutLogger implements Logger {

    public StdoutLogger() {
    }

    @Override
    public void debug(String tag, String message) {
        println("DEBUG", tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        println("DEBUG", tag, message, exception);
    }

    @Override
    public void info(String tag, String message) {
        println("INFO", tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        println("INFO", tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        println("ERROR", tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        println("ERROR", tag, message, exception);
    }

    private void println(String level, String tag, String message) {
        System.out.println(level + " " + tag + ": " + message);
    }

    private void println(String level, String tag, String message, Throwable exception) {
        println(level, tag, message);
        exception.printStackTrace();
    }
}
