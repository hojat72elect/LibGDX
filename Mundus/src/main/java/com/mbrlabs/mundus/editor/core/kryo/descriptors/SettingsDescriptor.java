package com.mbrlabs.mundus.editor.core.kryo.descriptors;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.mbrlabs.mundus.editor.core.registry.KeyboardLayout;

/**
 * Created by marcus on 07/06/16.
 */
public class SettingsDescriptor {

    @Tag(0)
    private String fbxConvBinary = "";
    @Tag(1)
    private KeyboardLayout keyboardLayout;

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
}
