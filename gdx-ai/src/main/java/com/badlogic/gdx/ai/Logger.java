package com.badlogic.gdx.ai;

/**
 * The {@code Logger} interface provides an abstraction over logging facilities.
 */
public interface Logger {

    /**
     * Logs a debug message.
     *
     * @param tag     used to identify the source of the log message.
     * @param message the message to log.
     */
    void debug(String tag, String message);

    /**
     * Logs a debug message with the specified exception.
     *
     * @param tag       used to identify the source of the log message.
     * @param message   the message to log.
     * @param exception the exception to log.
     */
    void debug(String tag, String message, Throwable exception);

    /**
     * Logs an info message.
     *
     * @param tag     used to identify the source of the log message.
     * @param message the message to log.
     */
    void info(String tag, String message);

    /**
     * Logs an info message with the specified exception.
     *
     * @param tag       used to identify the source of the log message.
     * @param message   the message to log.
     * @param exception the exception to log.
     */
    void info(String tag, String message, Throwable exception);

    /**
     * Logs an error message.
     *
     * @param tag     used to identify the source of the log message.
     * @param message the message to log.
     */
    void error(String tag, String message);

    /**
     * Logs an error message with the specified exception.
     *
     * @param tag       used to identify the source of the log message.
     * @param message   the message to log.
     * @param exception the exception to log.
     */
    void error(String tag, String message, Throwable exception);
}
