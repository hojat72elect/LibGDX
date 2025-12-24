
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.CurveModule;
import com.talosvfx.talos.runtime.vfx.modules.GradientColorModule;
import com.talosvfx.talos.runtime.vfx.modules.ParticleModule;
import com.talosvfx.talos.runtime.vfx.modules.StaticValueModule;
import com.talosvfx.talos.runtime.vfx.modules.Vector3Module;

public class ParticleModuleWrapper extends ModuleWrapper<ParticleModule> {


    public ParticleModuleWrapper() {
        super();
    }

    @Override
    public void setModule(ParticleModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {

        addSeparator(true);

        addInputSlot("life", ParticleModule.LIFE);

        addSeparator(true);

        addInputSlot("start pos", ParticleModule.SPAWN_POSITION);
        addInputSlot("start rot", ParticleModule.SPAWN_ROTATION);
        addInputSlot("start velocity", ParticleModule.INITIAL_VELOCITY);
        addInputSlot("start rot velocity", ParticleModule.INITIAL_SPIN_VELOCITY);


        addSeparator(true);


        addInputSlot("velocity over time", ParticleModule.VELOCITY_OVER_TIME);
        addInputSlot("rot velocity over time", ParticleModule.SPIN_OVER_TIME);
        addInputSlot("gravity", ParticleModule.GRAVITY);
        addInputSlot("forces", ParticleModule.FORCES);
        addInputSlot("drag", ParticleModule.DRAG);

        addInputSlot("color", ParticleModule.COLOR);
        addInputSlot("transparency", ParticleModule.TRANSPARENCY);

        addInputSlot("pivot", ParticleModule.PIVOT);
        addInputSlot("position override", ParticleModule.POSITION_OVERRIDE);
        addInputSlot("rotation override", ParticleModule.ROTATION_OVERRIDE);
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {

        if (slot.getIndex() == ParticleModule.LIFE) return StaticValueModule.class;
        if (slot.getIndex() == ParticleModule.PIVOT) return Vector3Module.class;
        if (slot.getIndex() == ParticleModule.COLOR) return GradientColorModule.class;
        if (slot.getIndex() == ParticleModule.TRANSPARENCY) return CurveModule.class;

        /*
        //Mode
        if(slot.getIndex() == ParticleModule.SPAWN_POSITION) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.SPAWN_ROTATION) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();

        if(slot.getIndex() == ParticleModule.INITIAL_VELOCITY) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.INITIAL_SPIN_VELOCITY) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.VELOCITY_OVER_TIME) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.SPIN_OVER_TIME) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.DRAG) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.GRAVITY) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.FORCES) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();

        if(slot.getIndex() == ParticleModule.POSITION_OVERRIDE) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
        if(slot.getIndex() == ParticleModule.ROTATION_OVERRIDE) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();
         */

        return null;
    }
}
