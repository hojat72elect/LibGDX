
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.EmConfigValue;

public class EmConfigModule extends AbstractModule {

    public static final int OUTPUT = 0;

    private EmConfigValue userValue;
    private EmConfigValue outputValue;

    @Override
    public void init() {
        super.init();

        userValue = new EmConfigValue();

        userValue.attached = false;
        userValue.continuous = true;
        userValue.additive = true;
        userValue.isBlendAdd = false;
        userValue.aligned = false;
        userValue.immortal = false;
        userValue.youngestInBack = true;
    }

    @Override
    protected void defineSlots() {
        outputValue = createOutputSlot(OUTPUT, new EmConfigValue());
    }

    @Override
    public void processCustomValues() {
        outputValue.set(userValue);
    }

    public EmConfigValue getUserValue() {
        return userValue;
    }


    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("additive", getUserValue().additive);
        json.writeValue("isBlendAdd", getUserValue().isBlendAdd);
        json.writeValue("attached", getUserValue().attached);
        json.writeValue("continuous", getUserValue().continuous);
        json.writeValue("aligned", getUserValue().aligned);
        json.writeValue("immortal", getUserValue().immortal);
        json.writeValue("youngestInFront", getUserValue().youngestInBack);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        getUserValue().additive = jsonData.getBoolean("additive");
        getUserValue().isBlendAdd = jsonData.getBoolean("isBlendAdd", false);
        getUserValue().attached = jsonData.getBoolean("attached");
        getUserValue().continuous = jsonData.getBoolean("continuous");
        getUserValue().aligned = jsonData.getBoolean("aligned");
        getUserValue().immortal = jsonData.getBoolean("immortal", false);
        getUserValue().youngestInBack = jsonData.getBoolean("youngestInFront", true);

        if (outputValue != null) {
            outputValue.set(getUserValue());
        }
    }
}
