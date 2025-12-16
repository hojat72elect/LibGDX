package com.badlogic.ashley.systems;

import static org.junit.Assert.assertEquals;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import org.junit.Test;

public class IntervalIteratingTest {
    private static final float deltaTime = 0.1f;

    @Test
    public void intervalSystem() {
        Engine engine = new Engine();
        IntervalIteratingSystemSpy intervalSystemSpy = new IntervalIteratingSystemSpy();
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(IntervalComponentSpy.class).get());
        ComponentMapper<IntervalComponentSpy> im = ComponentMapper.getFor(IntervalComponentSpy.class);

        engine.addSystem(intervalSystemSpy);

        for (int i = 0; i < 10; ++i) {
            Entity entity = new Entity();
            entity.add(new IntervalComponentSpy());
            engine.addEntity(entity);
        }

        for (int i = 1; i <= 10; ++i) {
            engine.update(deltaTime);

            for (int j = 0; j < entities.size(); ++j) {
                assertEquals(i / 2, im.get(entities.get(j)).numUpdates);
            }
        }
    }

    @Test
    public void processingUtilityFunctions() {
        final Engine engine = new Engine();

        final IntervalIteratingSystemSpy system = new IntervalIteratingSystemSpy();

        engine.addSystem(system);

        engine.update(deltaTime);
        assertEquals(0, system.numStartProcessing);
        assertEquals(0, system.numEndProcessing);

        engine.update(deltaTime);
        assertEquals(1, system.numStartProcessing);
        assertEquals(1, system.numEndProcessing);
    }

    private static class IntervalComponentSpy implements Component {
        public int numUpdates = 0;
    }

    private static class IntervalIteratingSystemSpy extends IntervalIteratingSystem {
        public int numStartProcessing;
        public int numEndProcessing;
        private final ComponentMapper<IntervalComponentSpy> im;


        public IntervalIteratingSystemSpy() {
            super(Family.all(IntervalComponentSpy.class).get(), deltaTime * 2.0f);

            im = ComponentMapper.getFor(IntervalComponentSpy.class);
        }

        @Override
        public void startProcessing() {
            numStartProcessing++;
        }

        @Override
        protected void processEntity(Entity entity) {
            im.get(entity).numUpdates++;
        }

        @Override
        public void endProcessing() {
            numEndProcessing++;
        }
    }
}
