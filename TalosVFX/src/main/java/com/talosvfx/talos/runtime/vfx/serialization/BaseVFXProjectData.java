package com.talosvfx.talos.runtime.vfx.serialization;

import com.talosvfx.talos.runtime.utils.Supplier;
import com.talosvfx.talos.runtime.vfx.ParticleEffectDescriptor;


public abstract class BaseVFXProjectData {

    public BaseVFXProjectData() {
    }

    public abstract Supplier<ParticleEffectDescriptor> getDescriptorSupplier();
}
