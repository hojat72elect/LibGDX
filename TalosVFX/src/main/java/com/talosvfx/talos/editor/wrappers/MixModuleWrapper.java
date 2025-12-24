
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.runtime.vfx.modules.MixModule;

public class MixModuleWrapper extends ModuleWrapper<MixModule> {

    @Override
    protected float reportPrefWidth() {
        return 180;
    }

    @Override
    protected void configureSlots() {

        addInputSlot("Value One", MixModule.VAL1);
        addInputSlot("mix ratio (0..1)", MixModule.ALPHA);
        addInputSlot("Value Two", MixModule.VAL2);

        addOutputSlot("result", 0);
    }
}
