package com.badlogic.ashley.systems;

import static org.junit.Assert.assertEquals;

import com.badlogic.ashley.core.Engine;

import org.junit.Test;

public class IntervalSystemTest {
    private static final float deltaTime = 0.1f;

    @Test
    public void intervalSystem() {
        Engine engine = new Engine();
        IntervalSystemSpy intervalSystemSpy = new IntervalSystemSpy();

        engine.addSystem(intervalSystemSpy);

        for (int i = 1; i <= 10; ++i) {
            engine.update(deltaTime);
            assertEquals(i / 2, intervalSystemSpy.numUpdates);
        }
    }

    @Test
    public void testGetInterval() {
        IntervalSystemSpy intervalSystemSpy = new IntervalSystemSpy();
        assertEquals(deltaTime * 2.0f, intervalSystemSpy.getInterval(), 0);
    }

    private static class IntervalSystemSpy extends IntervalSystem {
        public int numUpdates;

        public IntervalSystemSpy() {
            super(deltaTime * 2.0f);
        }

        @Override
        protected void updateInterval() {
            ++numUpdates;
        }
    }
}
