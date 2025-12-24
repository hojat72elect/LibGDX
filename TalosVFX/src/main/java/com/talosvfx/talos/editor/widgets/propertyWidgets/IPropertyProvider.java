package com.talosvfx.talos.editor.widgets.propertyWidgets;

import com.badlogic.gdx.utils.Array;

public interface IPropertyProvider {
    Array<PropertyWidget> getListOfProperties();

    String getPropertyBoxTitle();

    int getPriority();

    Class<? extends IPropertyProvider> getType();
}
