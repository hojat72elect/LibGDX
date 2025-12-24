
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.SingleParticlePointDataGeneratorModule;

public class SingleParticlePointDataGeneratorModuleWrapper extends ModuleWrapper<SingleParticlePointDataGeneratorModule> {


    public SingleParticlePointDataGeneratorModuleWrapper() {
        super();
    }

    @Override
    public void setModule(SingleParticlePointDataGeneratorModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        addOutputSlot("single", SingleParticlePointDataGeneratorModule.MODULE);
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {


        return null;
    }
}
