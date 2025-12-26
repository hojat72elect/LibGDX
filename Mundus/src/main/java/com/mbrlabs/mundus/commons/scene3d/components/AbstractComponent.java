package com.mbrlabs.mundus.commons.scene3d.components;

import com.mbrlabs.mundus.commons.scene3d.GameObject;

public abstract class AbstractComponent implements Component {

    public GameObject gameObject;
    protected Type type;

    public AbstractComponent(GameObject go) {
        this.gameObject = go;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public GameObject getGameObject() {
        return this.gameObject;
    }

    @Override
    public void remove() {
        gameObject.removeComponent(this);
    }
}
