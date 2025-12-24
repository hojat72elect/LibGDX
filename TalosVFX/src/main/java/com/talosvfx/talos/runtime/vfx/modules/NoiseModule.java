
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.ScopePayload;
import com.talosvfx.talos.runtime.vfx.utils.SimplexNoise;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class NoiseModule extends AbstractModule {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int OUTPUT = 0;
    public float frequency = 20f;
    NumericalValue x;
    NumericalValue y;
    NumericalValue output;
    SimplexNoise noise = new SimplexNoise();

    @Override
    protected void defineSlots() {
        x = createInputSlot(X);
        y = createInputSlot(Y);

        output = createOutputSlot(OUTPUT);
    }

    @Override
    public void processCustomValues() {
        output.set(noiseFunction(x.getFloat(), y.getFloat()));
    }

    private float noiseFunction(float x, float y) {
        // normalize
        x = x - (int) x;
        y = y - (int) y;

        float particleSeed = getScope().getFloat(ScopePayload.PARTICLE_SEED);
        y = y * particleSeed;
        y = y - (int) y;

        float result = noise.query(x, y, frequency);

        //result = result * 0.5f + 0.5f; // bring to 0-1 range (actually no not needed)

        return result;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("frequency", getFrequency());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setFrequency(jsonData.getFloat("frequency", 20f));
    }
}
