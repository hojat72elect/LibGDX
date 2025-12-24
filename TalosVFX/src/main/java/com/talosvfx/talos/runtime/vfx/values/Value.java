
package com.talosvfx.talos.runtime.vfx.values;

public abstract class Value {

    private boolean isEmpty;

    private boolean addition;

    public boolean isAddition() {
        return addition;
    }

    public void setAddition(boolean addition) {
        this.addition = addition;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public abstract void set(Value value);
}
