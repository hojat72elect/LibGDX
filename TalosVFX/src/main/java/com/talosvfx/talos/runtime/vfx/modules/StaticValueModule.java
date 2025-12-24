
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class StaticValueModule extends AbstractModule {

    public static final int OUTPUT = 0;

    private NumericalValue staticValue;
    private NumericalValue outputValue;

    @Override
    protected void defineSlots() {
        outputValue = createOutputSlot(OUTPUT);

        staticValue = new NumericalValue();
        staticValue.set(1f);
    }

    @Override
    public void processCustomValues() {
        outputValue.set(staticValue);
    }

    public float getStaticValue() {
        return staticValue.getFloat();
    }

    public void setStaticValue(float val) {
        staticValue.set(val);
    }

    public NumericalValue getOutputValue() {
        return outputValue;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("value", getStaticValue());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setStaticValue(jsonData.getFloat("value"));
    }
}
