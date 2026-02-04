package com.crashinvaders.vfx.effects;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CompositeVfxEffectTest extends VfxEffectTestBase {

    private TestCompositeEffect effect;
    private VfxEffect childEffect;

    @Before
    public void setUp() {
        childEffect = mock(VfxEffect.class);
        effect = new TestCompositeEffect();
        effect.registerChild(childEffect);
    }

    @Test
    public void testResize() {
        effect.resize(100, 200);
        verify(childEffect).resize(100, 200);
    }

    @Test
    public void testRebind() {
        effect.rebind();
        verify(childEffect).rebind();
    }

    @Test
    public void testUpdate() {
        effect.update(0.5f);
        verify(childEffect).update(0.5f);
    }

    @Test
    public void testDispose() {
        effect.dispose();
        verify(childEffect).dispose();
    }

    @Test
    public void testUnregister() {
        effect.unregisterChild(childEffect);
        effect.update(1.0f);
        verify(childEffect, never()).update(1.0f);
    }

    private static class TestCompositeEffect extends CompositeVfxEffect {
        public void registerChild(VfxEffect effect) {
            register(effect);
        }

        public void unregisterChild(VfxEffect effect) {
            unregister(effect);
        }
    }
}
