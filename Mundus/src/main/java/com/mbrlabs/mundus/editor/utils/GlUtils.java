package com.mbrlabs.mundus.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import org.lwjgl.opengl.GL11;

public class GlUtils {

    public static void clearScreen(Color color) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
    }

    /**
     * OpenGL functionality, that goes beyond OpenGL ES.
     * <p>
     * It's not 'unsafe' to run this on a desktop, but this kind of
     * functionality uses raw LWJGL and is not available on mobile devices or in
     * the browser.
     */
    public static class Unsafe {

        public static void polygonModeFill() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }

        public static void polygonModeWireframe() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }
    }
}
