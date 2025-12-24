
package com.talosvfx.talos.editor.widgets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class DragPoint {

    public Vector3 position = new Vector3();
    public Vector3 origin = new Vector3();
    public Color color = new Color();
    public boolean drawOrigin = false;

    public boolean changed;


    public DragPoint() {

    }

    public DragPoint(float x, float y) {
        position.set(x, y, 0);
        drawOrigin = false;
        color.set(Color.ORANGE);
    }

    public DragPoint(float x, float y, float z) {
        position.set(x, y, z);
        drawOrigin = false;
        color.set(Color.ORANGE);
    }


    public void set(float x, float y) {
        position.set(x, y, 0);
    }

    public void set(float x, float y, float z) {
        position.set(x, y, z);
    }
}
