
package com.talosvfx.talos.editor.serialization;

import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.editor.ParticleEmitterWrapper;

public class VFXEditorState {

    public Array<ParticleEmitterWrapper> activeWrappers = new Array<>();

    public void reset() {
        activeWrappers.clear();
    }
}
