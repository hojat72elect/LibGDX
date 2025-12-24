
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.values.ModuleValue;

public class DrawableModule extends AbstractModule {

    public static final int MATERIAL_IN = 0;
    public static final int MATERIAL_OUT = 1;

    public static final int POINT_GENERATOR = 2;
    public static final int MESH_GENERATOR = 3;

    ModuleValue<MaterialModule> materialModule;
    ModuleValue<MaterialModule> outSlot;

    ModuleValue<ParticlePointDataGeneratorModule> pointGenerator;
    ModuleValue<MeshGeneratorModule> meshGenerator;


    public DrawableModule() {

    }

    @Override
    protected void defineSlots() {
        materialModule = createInputSlot(MATERIAL_IN, new ModuleValue<MaterialModule>());
        outSlot = createOutputSlot(MATERIAL_OUT, new ModuleValue<MaterialModule>());

        pointGenerator = createInputSlot(POINT_GENERATOR, new ModuleValue<ParticlePointDataGeneratorModule>());
        meshGenerator = createInputSlot(MESH_GENERATOR, new ModuleValue<MeshGeneratorModule>());
    }

    @Override
    public void processCustomValues() {
        fetchInputSlotValue(MATERIAL_IN);
        MaterialModule module = materialModule.getModule();
        outSlot.setModule(module);
    }


    @Override
    public void write(Json json) {
        super.write(json);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
    }

    public MaterialModule getMaterialModule() {
        return outSlot.getModule();
    }

    public ParticlePointDataGeneratorModule getPointDataGenerator() {
        fetchInputSlotValue(POINT_GENERATOR);
        return pointGenerator.getModule();
    }

    public MeshGeneratorModule getMeshGenerator() {
        fetchInputSlotValue(MESH_GENERATOR);
        return meshGenerator.getModule();
    }
}
