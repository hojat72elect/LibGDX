
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.utils.InterpolationMappings;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class InterpolationModule extends AbstractModule {

    public static final int ALPHA = 0;
    public static final int OUTPUT = 0;

    NumericalValue alpha;
    NumericalValue output;

    private Interpolation currentInterpolation = Interpolation.linear;

    @Override
    protected void defineSlots() {
        alpha = createAlphaInputSlot(ALPHA);

        output = createOutputSlot(OUTPUT);
    }

    @Override
    public void processCustomValues() {
        output.set(currentInterpolation.apply(alpha.getFloat()));
    }

    public Interpolation getInterpolation() {
        return this.currentInterpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.currentInterpolation = interpolation;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("interp", InterpolationMappings.getNameForInterpolation(getInterpolation()));
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        currentInterpolation = InterpolationMappings.getInterpolationForName(jsonData.getString("interp"));
    }
}
