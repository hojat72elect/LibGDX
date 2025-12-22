package games.rednblack.editor.splash;

import com.badlogic.gdx.Game;

public class SplashScreenAdapter extends Game {

    private static final String prefix = "games.rednblack.editor.splash";
    public static final String UPDATE_SPLASH = prefix + ".UPDATE";
    public static final String CLOSE_SPLASH = prefix + ".CLOSE";

    private SplashScreen screen;
    private boolean isLoading = true;

    @Override
    public void create() {
        screen = new SplashScreen(isLoading);
        setScreen(screen);
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
    }

    public void loadedData() {
        screen.loadedData();
    }

    public void setProgressStatus(String status) {
        screen.setProgressStatus(status);
    }
}
