package com.badlogic.ashley.core;

import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Uniquely identifies a {@link Component} sub-class. It assigns them an index which is used internally for fast comparison and
 * retrieval. See {@link Family} and {@link Entity}. ComponentType is a package protected class. You cannot instantiate a
 * ComponentType. They can only be accessed via {@link #getIndexFor(Class<? extends Component>)}. Each component class will always
 * return the same instance of ComponentType.
 */
public final class ComponentType {
    private static final ObjectMap<Class<? extends Component>, ComponentType> assignedComponentTypes = new ObjectMap<>();
    private static int typeIndex = 0;

    private final int index;

    private ComponentType() {
        index = typeIndex++;
    }

    /**
     * @param componentType The {@link Component} class
     * @return A ComponentType matching the Component Class
     */
    public static ComponentType getFor(Class<? extends Component> componentType) {
        ComponentType type = assignedComponentTypes.get(componentType);

        if (type == null) {
            type = new ComponentType();
            assignedComponentTypes.put(componentType, type);
        }

        return type;
    }

    /**
     * Quick helper method. The same could be done via {ComponentType.getFor(Class<? extends Component>)}.
     *
     * @param componentType The {@link Component} class
     * @return The index for the specified {@link Component} Class
     */
    public static int getIndexFor(Class<? extends Component> componentType) {
        return getFor(componentType).getIndex();
    }

    /**
     * @param componentTypes list of {@link Component} classes
     * @return Bits representing the collection of components for quick comparison and matching. See
     * {Family.getFor(Bits, Bits, Bits)}.
     */
    @SafeVarargs
    public static Bits getBitsFor(Class<? extends Component>... componentTypes) {
        Bits bits = new Bits();

        for (Class<? extends Component> componentType : componentTypes) {
            bits.set(ComponentType.getIndexFor(componentType));
        }

        return bits;
    }

    /**
     * @return This ComponentType's unique index
     */
    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ComponentType other = (ComponentType) obj;
        return index == other.index;
    }
}
