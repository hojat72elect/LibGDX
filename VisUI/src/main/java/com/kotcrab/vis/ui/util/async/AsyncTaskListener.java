package com.kotcrab.vis.ui.util.async;

/**
 * Allows to listen to events occurring in {@link AsyncTask}.
 */
public interface AsyncTaskListener {
    /**
     * Called when task status message has changed.
     */
    void messageChanged(String message);

    /**
     * Called when task progress has changed.
     */
    void progressChanged(int newProgressPercent);

    /**
     * Called when task has finished executing. Finished will always called, even if some exception occurred during task
     * execution.
     */
    void finished();

    /**
     * Called when some error occurred during task execution.
     */
    void failed(String message, Exception exception);
}
