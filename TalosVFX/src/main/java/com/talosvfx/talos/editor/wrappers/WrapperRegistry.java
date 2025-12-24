
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.utils.ObjectMap;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;

public class WrapperRegistry<T extends AbstractModule, U extends ModuleWrapper<T>> {

    public static ObjectMap<Class, Class> map = new ObjectMap<>();

    public static <T extends AbstractModule, U extends ModuleWrapper<T>> Class<U> get(Class<T> moduleClass) {
        return map.get(moduleClass);
    }

    public static <T extends AbstractModule, U extends ModuleWrapper<T>> void reg(Class<T> moduleClass, Class<U> wrapperClass) {
        map.put(moduleClass, wrapperClass);
    }
}
