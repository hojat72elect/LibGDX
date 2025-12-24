package com.talosvfx.talos.runtime.routine.misc;

import com.badlogic.gdx.utils.ObjectMap;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyBooleanWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyColorWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyFloatWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyGameAssetWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyGameObjectWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyStringWrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyType;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyVec2Wrapper;
import com.talosvfx.talos.runtime.scene.utils.propertyWrappers.PropertyWrapper;

public class PropertyTypeWrapperMapper {

    private static final ObjectMap<PropertyType, Class<? extends PropertyWrapper>> wrapperMap = new ObjectMap<>();

    static {

        wrapperMap.put(PropertyType.FLOAT, PropertyFloatWrapper.class);
        wrapperMap.put(PropertyType.VECTOR2, PropertyVec2Wrapper.class);
        wrapperMap.put(PropertyType.COLOR, PropertyColorWrapper.class);
        wrapperMap.put(PropertyType.ASSET, PropertyGameAssetWrapper.class);
        wrapperMap.put(PropertyType.BOOLEAN, PropertyBooleanWrapper.class);
        wrapperMap.put(PropertyType.STRING, PropertyStringWrapper.class);
        wrapperMap.put(PropertyType.GAME_OBJECT, PropertyGameObjectWrapper.class);
    }

    public static Class<? extends PropertyWrapper> getWrapperForPropertyType(PropertyType propertyType) {
        return wrapperMap.get(propertyType);
    }
}
