
package com.badlogic.gdx.tests.lwjgl;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tests.ScrollPaneScrollBarsTest;
import com.badlogic.gdx.tests.utils.GdxTest;

public class LwjglDebugStarter {
    public static void main(String[] argv) {
        // this is only here for me to debug native code faster
// new SharedLibraryLoader("../../extensions/gdx-audio/libs/gdx-audio-natives.jar").load("gdx-audio");
// new SharedLibraryLoader("../../extensions/gdx-image/libs/gdx-image-natives.jar").load("gdx-image");
// new SharedLibraryLoader("../../extensions/gdx-freetype/libs/gdx-freetype-natives.jar").load("gdx-freetype");
// new SharedLibraryLoader("../../gdx/libs/gdx-natives.jar").load("gdx");

        GdxTest test = new ScrollPaneScrollBarsTest();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.r = config.g = config.b = config.a = 8;
// config.width = 320;
// config.height = 241;
        config.width = 960;
        config.height = 600;
// config.width = 1920;
// config.height = 1080;
// config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        new LwjglApplication(test, config);
    }
}
