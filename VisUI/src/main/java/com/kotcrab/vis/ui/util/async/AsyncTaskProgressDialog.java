package com.kotcrab.vis.ui.util.async;

import com.kotcrab.vis.ui.Locales.CommonText;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.async.AsyncTask.Status;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Dialog used to display progress of {@link AsyncTask} as standard VisUI window. Shows progress bar and status
 * of currently executed task.
 */
public class AsyncTaskProgressDialog extends VisWindow {
    private final AsyncTask task;

    /**
     * Creates new dialog, note that task will be automatically started. Created dialog must be manually added to stage,
     * preferably with {@link VisWindow#fadeIn()} animation.
     *
     * @param title title used as window title
     * @param task  task to be executed
     */
    public AsyncTaskProgressDialog(String title, AsyncTask task) {
        super(title);
        this.task = task;
        setModal(true);

        TableUtils.setSpacingDefaults(this);

        final VisLabel statusLabel = new VisLabel(CommonText.PLEASE_WAIT.get());
        final VisProgressBar progressBar = new VisProgressBar(0, 100, 1, false);

        defaults().padLeft(6).padRight(6);

        add(statusLabel).padTop(6).left().row();
        add(progressBar).width(300).padTop(6).padBottom(6);

        task.addListener(new AsyncTaskListener() {
            @Override
            public void progressChanged(int newProgressPercent) {
                progressBar.setValue(newProgressPercent);
            }

            @Override
            public void messageChanged(String message) {
                statusLabel.setText(message);
            }

            @Override
            public void finished() {
                fadeOut();
            }

            @Override
            public void failed(String message, Exception exception) {
                Dialogs.showErrorDialog(getStage(), exception.getMessage() == null ? CommonText.UNKNOWN_ERROR_OCCURRED.get() : exception.getMessage(), exception);
            }
        });

        pack();
        centerWindow();

        task.execute();
    }

    public AsyncTask getTask() {
        return task;
    }

    public void addListener(AsyncTaskListener listener) {
        task.addListener(listener);
    }

    public Status getStatus() {
        return task.getStatus();
    }
}
