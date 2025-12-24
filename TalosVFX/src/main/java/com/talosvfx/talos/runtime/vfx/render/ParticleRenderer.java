
package com.talosvfx.talos.runtime.vfx.render;

import com.badlogic.gdx.graphics.Camera;
import com.talosvfx.talos.runtime.vfx.ParticleEffectInstance;
import com.talosvfx.talos.runtime.vfx.modules.MaterialModule;

public interface ParticleRenderer {

    Camera getCamera();

    boolean isPMA();

    void setPMA(boolean pma);

    void render(ParticleEffectInstance particleEffectInstance);

    void render(float[] verts, MaterialModule materialModule);

    void render(float[] verts, int vertCount, short[] tris, int triCount, MaterialModule materialModule);
}
