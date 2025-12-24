
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.ColorModule;
import com.talosvfx.talos.runtime.vfx.modules.StaticValueModule;
import com.talosvfx.talos.runtime.vfx.modules.StripMeshGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.Vector2Module;

public class StripMeshGeneratorModuleWrapper extends ModuleWrapper<StripMeshGeneratorModule> {


    public StripMeshGeneratorModuleWrapper() {
        super();
    }

    @Override
    public void setModule(StripMeshGeneratorModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        addInputSlot("uvs", StripMeshGeneratorModule.UVS);
        addInputSlot("offset", StripMeshGeneratorModule.OFFSET);
        addInputSlot("colour", StripMeshGeneratorModule.COLOUR);
        addInputSlot("thickness", StripMeshGeneratorModule.THICKNESS);
        addInputSlot("transparency", StripMeshGeneratorModule.TRANSPARENCY);

        addOutputSlot("strip", StripMeshGeneratorModule.MODULE);
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {
        if (slot.getIndex() == StripMeshGeneratorModule.UVS) return Vector2Module.class;
        if (slot.getIndex() == StripMeshGeneratorModule.OFFSET) return Vector2Module.class;
        if (slot.getIndex() == StripMeshGeneratorModule.COLOUR) return ColorModule.class;
        if (slot.getIndex() == StripMeshGeneratorModule.TRANSPARENCY) return StaticValueModule.class;
        if (slot.getIndex() == StripMeshGeneratorModule.THICKNESS) return StaticValueModule.class;

        return null;
    }
}
