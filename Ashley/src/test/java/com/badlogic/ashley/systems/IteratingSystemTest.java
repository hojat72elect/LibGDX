package com.badlogic.ashley.systems;

import static org.junit.Assert.assertEquals;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import org.junit.Test;

public class IteratingSystemTest {
    private static final float deltaTime = 0.16f;

    @Test
    public void shouldIterateEntitiesWithCorrectFamily() {
        final Engine engine = new Engine();

        final Family family = Family.all(ComponentA.class, ComponentB.class).get();
        final IteratingSystemMock system = new IteratingSystemMock(family);
        final Entity e = new Entity();

        engine.addSystem(system);
        engine.addEntity(e);

        // When entity has ComponentA
        e.add(new ComponentA());
        engine.update(deltaTime);

        assertEquals(0, system.numUpdates);

        // When entity has ComponentA and ComponentB
        system.numUpdates = 0;
        e.add(new ComponentB());
        engine.update(deltaTime);

        assertEquals(1, system.numUpdates);

        // When entity has ComponentA, ComponentB and ComponentC
        system.numUpdates = 0;
        e.add(new ComponentC());
        engine.update(deltaTime);

        assertEquals(1, system.numUpdates);

        // When entity has ComponentB and ComponentC
        system.numUpdates = 0;
        e.remove(ComponentA.class);
        e.add(new ComponentC());
        engine.update(deltaTime);

        assertEquals(0, system.numUpdates);
    }

    @Test
    public void entityRemovalWhileIterating() {
        Engine engine = new Engine();
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(SpyComponent.class, IndexComponent.class).get());
        ComponentMapper<SpyComponent> sm = ComponentMapper.getFor(SpyComponent.class);

        engine.addSystem(new IteratingRemovalSystem());

        final int numEntities = 10;

        for (int i = 0; i < numEntities; ++i) {
            Entity e = new Entity();
            e.add(new SpyComponent());

            IndexComponent in = new IndexComponent();
            in.index = i + 1;

            e.add(in);

            engine.addEntity(e);
        }

        engine.update(deltaTime);

        assertEquals(numEntities / 2, entities.size());

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            assertEquals(1, sm.get(e).updates);
        }
    }

    @Test
    public void componentRemovalWhileIterating() {
        Engine engine = new Engine();
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(SpyComponent.class, IndexComponent.class).get());
        ComponentMapper<SpyComponent> sm = ComponentMapper.getFor(SpyComponent.class);

        engine.addSystem(new IteratingComponentRemovalSystem());

        final int numEntities = 10;

        for (int i = 0; i < numEntities; ++i) {
            Entity e = new Entity();
            e.add(new SpyComponent());

            IndexComponent in = new IndexComponent();
            in.index = i + 1;

            e.add(in);

            engine.addEntity(e);
        }

        engine.update(deltaTime);

        assertEquals(numEntities / 2, entities.size());

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            assertEquals(1, sm.get(e).updates);
        }
    }

    @Test
    public void processingUtilityFunctions() {
        final Engine engine = new Engine();

        final IteratingSystemMock system = new IteratingSystemMock(Family.all().get());

        engine.addSystem(system);

        engine.update(deltaTime);
        assertEquals(1, system.numStartProcessing);
        assertEquals(1, system.numEndProcessing);

        engine.update(deltaTime);
        assertEquals(2, system.numStartProcessing);
        assertEquals(2, system.numEndProcessing);
    }

    private static class ComponentA implements Component {
    }

    private static class ComponentB implements Component {
    }

    private static class ComponentC implements Component {
    }

    private static class IteratingSystemMock extends IteratingSystem {
        public int numUpdates;
        public int numStartProcessing;
        public int numEndProcessing;

        public IteratingSystemMock(Family family) {
            super(family);
        }

        @Override
        public void startProcessing() {
            ++numStartProcessing;
        }

        @Override
        public void processEntity(Entity entity, float deltaTime) {
            ++numUpdates;
        }

        @Override
        public void endProcessing() {
            ++numEndProcessing;
        }
    }

    private static class SpyComponent implements Component {
        public int updates = 0;
    }

    private static class IndexComponent implements Component {
        public int index = 0;
    }

    private static class IteratingComponentRemovalSystem extends IteratingSystem {

        private final ComponentMapper<SpyComponent> sm;
        private final ComponentMapper<IndexComponent> im;

        public IteratingComponentRemovalSystem() {
            super(Family.all(SpyComponent.class, IndexComponent.class).get());

            sm = ComponentMapper.getFor(SpyComponent.class);
            im = ComponentMapper.getFor(IndexComponent.class);
        }

        @Override
        public void processEntity(Entity entity, float deltaTime) {
            int index = im.get(entity).index;
            if (index % 2 == 0) {
                entity.remove(SpyComponent.class);
                entity.remove(IndexComponent.class);
            } else {
                sm.get(entity).updates++;
            }
        }
    }

    private static class IteratingRemovalSystem extends IteratingSystem {
        private final ComponentMapper<SpyComponent> sm;
        private final ComponentMapper<IndexComponent> im;

        public IteratingRemovalSystem() {
            super(Family.all(SpyComponent.class, IndexComponent.class).get());

            sm = ComponentMapper.getFor(SpyComponent.class);
            im = ComponentMapper.getFor(IndexComponent.class);
        }

        @Override
        public void addedToEngine(Engine engine) {
            super.addedToEngine(engine);
        }

        @Override
        public void processEntity(Entity entity, float deltaTime) {
            int index = im.get(entity).index;
            if (index % 2 == 0) {
                getEngine().removeEntity(entity);
            } else {
                sm.get(entity).updates++;
            }
        }
    }
}
