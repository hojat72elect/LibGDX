package com.crashinvaders.vfx.effects;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;

/**
 * Any effect that is compatible with {@link com.crashinvaders.vfx.VfxManager}'s render chain, should implement this interface.
 */
public interface ChainVfxEffect extends VfxEffect {
    void render(VfxRenderContext context, VfxPingPongWrapper buffers);
}