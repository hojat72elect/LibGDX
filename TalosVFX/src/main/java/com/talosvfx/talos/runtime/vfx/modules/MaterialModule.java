
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class MaterialModule extends AbstractModule {

    public static final int MATERIAL_MODULE = 0;

    public MaterialModule() {

    }

    @Override
    protected void defineSlots() {
    }

    @Override
    public void processCustomValues() {

    }

    @Override
    public void write(Json json) {
        super.write(json);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
    }
}
