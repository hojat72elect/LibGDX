package games.rednblack.h2d.common;

public interface ProgressHandler {
    void progressStarted();

    void progressChanged(float value);

    void progressComplete();

    void progressFailed();
}
