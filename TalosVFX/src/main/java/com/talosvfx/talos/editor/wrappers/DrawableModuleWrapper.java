
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.DrawableModule;
import com.talosvfx.talos.runtime.vfx.modules.MaterialModule;
import com.talosvfx.talos.runtime.vfx.modules.QuadMeshGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.SingleParticlePointDataGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.SpriteMaterialModule;

public class DrawableModuleWrapper extends ModuleWrapper<DrawableModule> {


    public DrawableModuleWrapper() {
        super();
    }

    @Override
    public void setModule(DrawableModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        addInputSlot("material", MaterialModule.MATERIAL_MODULE);

        addInputSlot("point", DrawableModule.POINT_GENERATOR);
        addInputSlot("mesh", DrawableModule.MESH_GENERATOR);
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {

        if (slot.getIndex() == DrawableModule.MATERIAL_IN) return SpriteMaterialModule.class;
        if (slot.getIndex() == DrawableModule.POINT_GENERATOR) return SingleParticlePointDataGeneratorModule.class;
        if (slot.getIndex() == DrawableModule.MESH_GENERATOR) return QuadMeshGeneratorModule.class;

        return null;
    }
}
