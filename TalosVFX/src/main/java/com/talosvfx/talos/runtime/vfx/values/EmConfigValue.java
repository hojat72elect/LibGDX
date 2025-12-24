
package com.talosvfx.talos.runtime.vfx.values;

public class EmConfigValue extends Value {

    public boolean attached = false;
    public boolean continuous = true;
    public boolean aligned = false;
    public boolean additive = true;
    public boolean isBlendAdd = false;
    public boolean immortal = false;
    public boolean youngestInBack = true;

    @Override
    public void set(Value value) {
        set((EmConfigValue) value);
    }

    public void set(EmConfigValue from) {
        this.attached = from.attached;
        this.continuous = from.continuous;
        this.aligned = from.aligned;
        this.additive = from.additive;
        this.isBlendAdd = from.isBlendAdd;
        this.immortal = from.immortal;
        this.youngestInBack = from.youngestInBack;
    }
}
