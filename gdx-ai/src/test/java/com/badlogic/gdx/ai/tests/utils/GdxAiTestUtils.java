package com.badlogic.gdx.ai.tests.utils;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Utility class to launch tests.
 */
public final class GdxAiTestUtils {

    private GdxAiTestUtils() {
    }

    public static void launch(ApplicationListener test) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.r = config.g = config.b = config.a = 8;
        config.width = 960;
        config.height = 600;
        new LwjglApplication(test, config);
    }
}
