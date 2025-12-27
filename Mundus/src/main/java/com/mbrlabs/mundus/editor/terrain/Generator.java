package com.mbrlabs.mundus.editor.terrain;

import com.mbrlabs.mundus.commons.terrain.Terrain;

public abstract class Generator<T extends Generator<T>> {

    protected Terrain terrain;
    protected float minHeight = 0;
    protected float maxHeight = 50;

    // X and Z Grid offsets
    protected int offsetX = 0;

    protected int offsetZ = 0;

    Generator(Terrain terrain) {
        this.terrain = terrain;
    }

    public T minHeight(float min) {
        this.minHeight = min;
        return (T) this;
    }

    public T maxHeight(float max) {
        this.maxHeight = max;
        return (T) this;
    }

    public T setTerrain(Terrain terrain) {
        this.terrain = terrain;
        return (T) this;
    }

    public T offset(int x, int z) {
        this.offsetX = x;
        this.offsetZ = z;
        return (T) this;
    }

    public abstract void terraform();
}
