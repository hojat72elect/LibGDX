
package com.talosvfx.talos.runtime.vfx.modules;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class Vector2Module extends AbstractModule {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int OUTPUT = 0;

    NumericalValue x;
    NumericalValue y;
    NumericalValue output;

    float defaultX, defaultY;

    @Override
    protected void defineSlots() {
        x = createInputSlot(X);
        y = createInputSlot(Y);

        output = createOutputSlot(OUTPUT);
    }

    @Override
    public void processCustomValues() {

        if (x.isEmpty()) x.set(defaultX);
        if (y.isEmpty()) y.set(defaultY);

        output.set(x, y);
    }

    public void setX(float x) {
        defaultX = x;
    }

    public void setY(float y) {
        defaultY = y;
    }

    public float getDefaultX() {
        return defaultX;
    }

    public float getDefaultY() {
        return defaultY;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("x", getDefaultX());
        json.writeValue("y", getDefaultY());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        defaultX = jsonData.getFloat("x", 0);
        defaultY = jsonData.getFloat("y", 0);
    }
}
