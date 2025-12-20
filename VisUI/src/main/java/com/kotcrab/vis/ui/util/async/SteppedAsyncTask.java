package com.kotcrab.vis.ui.util.async;

/**
 * {@link AsyncTask} that performs fixed numbers of steps, provides convenient methods to calculate and update task progress.
 */
public abstract class SteppedAsyncTask extends AsyncTask {
    private int step;
    private int totalSteps;

    public SteppedAsyncTask(String threadName) {
        super(threadName);
    }

    /**
     * Sets total numbers ot steps this task will have to perform, usually called at the beginning of {@link #doInBackground()}.
     *
     * @see #nextStep()
     */
    protected void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
        this.step = 0;
        setProgressPercent(0);
    }

    /**
     * Advances task to next step and updates its percent progress.
     */
    protected void nextStep() {
        setProgressPercent(++step * 100 / totalSteps);
    }
}
