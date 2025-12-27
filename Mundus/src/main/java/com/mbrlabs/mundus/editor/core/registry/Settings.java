package com.mbrlabs.mundus.editor.core.registry;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Global Mundus settings.
 */
public class Settings {

    private String fbxConvBinary = "";
    private KeyboardLayout keyboardLayout;
    private ObjectMap<String, String> customKeyboardShortcuts = new ObjectMap<>();

    public String getFbxConvBinary() {
        return fbxConvBinary;
    }

    public void setFbxConvBinary(String fbxConvBinary) {
        this.fbxConvBinary = fbxConvBinary;
    }

    public KeyboardLayout getKeyboardLayout() {
        return keyboardLayout;
    }

    public void setKeyboardLayout(KeyboardLayout keyboardLayout) {
        this.keyboardLayout = keyboardLayout;
    }

    public ObjectMap<String, String> getCustomKeyboardShortcuts() {
        return customKeyboardShortcuts;
    }

    public void setCustomKeyboardShortcuts(ObjectMap<String, String> customKeyboardShortcuts) {
        this.customKeyboardShortcuts = customKeyboardShortcuts;
    }
}
