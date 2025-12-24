
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class InputModule extends AbstractModule {

    public static final int OUTPUT = 0;
    private NumericalValue outputValue;

    private int scopeKey;

    @Override
    protected void defineSlots() {
        outputValue = createOutputSlot(OUTPUT);
    }

    @Override
    public void processCustomValues() {
        NumericalValue value = getScope().get(scopeKey);
        outputValue.set(value);
    }

    public int getInput() {
        return this.scopeKey;
    }

    public void setInput(int scopeKey) {
        this.scopeKey = scopeKey;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("scopeKey", getInput(), int.class);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setInput(jsonData.getInt("scopeKey"));
    }
}
