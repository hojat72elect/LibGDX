package com.mbrlabs.mundus.editor.scene3d.components;

/**
 * Mouse pickable by color code picking system.
 */
public interface PickableComponent {

    void encodeRaypickColorId();

    void renderPick();
}
