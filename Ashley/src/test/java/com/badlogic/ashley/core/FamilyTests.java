package com.badlogic.ashley.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.ashley.systems.IteratingSystem;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class FamilyTests {

    @Test
    public void validFamily() {
        assertNotNull(Family.all().get());
        assertNotNull(Family.all(ComponentA.class).get());
        assertNotNull(Family.all(ComponentB.class).get());
        assertNotNull(Family.all(ComponentC.class).get());
        assertNotNull(Family.all(ComponentA.class, ComponentB.class).get());
        assertNotNull(Family.all(ComponentA.class, ComponentC.class).get());
        assertNotNull(Family.all(ComponentB.class, ComponentA.class).get());
        assertNotNull(Family.all(ComponentB.class, ComponentC.class).get());
        assertNotNull(Family.all(ComponentC.class, ComponentA.class).get());
        assertNotNull(Family.all(ComponentC.class, ComponentB.class).get());
        assertNotNull(Family.all(ComponentA.class, ComponentB.class, ComponentC.class).get());
        Family.all(ComponentA.class, ComponentB.class).get();
        assertNotNull(Family.one(ComponentC.class, ComponentD.class)
                .exclude(ComponentE.class, ComponentF.class).get());
    }

    @Test
    public void sameFamily() {
        Family family1 = Family.all(ComponentA.class).get();
        Family family2 = Family.all(ComponentA.class).get();
        Family family3 = Family.all(ComponentA.class, ComponentB.class).get();
        Family family4 = Family.all(ComponentA.class, ComponentB.class).get();
        Family family5 = Family.all(ComponentA.class, ComponentB.class, ComponentC.class).get();
        Family family6 = Family.all(ComponentA.class, ComponentB.class, ComponentC.class).get();
        Family family7 = Family.all(ComponentA.class, ComponentB.class).one(ComponentC.class, ComponentD.class)
                .exclude(ComponentE.class, ComponentF.class).get();
        Family family8 = Family.all(ComponentA.class, ComponentB.class).one(ComponentC.class, ComponentD.class)
                .exclude(ComponentE.class, ComponentF.class).get();
        Family family9 = Family.all().get();
        Family family10 = Family.all().get();

        assertEquals(family1, family2);
        assertEquals(family2, family1);
        assertEquals(family3, family4);
        assertEquals(family4, family3);
        assertEquals(family5, family6);
        assertEquals(family6, family5);
        assertEquals(family7, family8);
        assertEquals(family8, family7);
        assertEquals(family9, family10);

        assertEquals(family1.getIndex(), family2.getIndex());
        assertEquals(family3.getIndex(), family4.getIndex());
        assertEquals(family5.getIndex(), family6.getIndex());
        assertEquals(family7.getIndex(), family8.getIndex());
        assertEquals(family9.getIndex(), family10.getIndex());
    }

    @Test
    public void differentFamily() {
        Family family1 = Family.all(ComponentA.class).get();
        Family family2 = Family.all(ComponentB.class).get();
        Family family3 = Family.all(ComponentC.class).get();
        Family family4 = Family.all(ComponentA.class, ComponentB.class).get();
        Family family5 = Family.all(ComponentA.class, ComponentC.class).get();
        Family family6 = Family.all(ComponentB.class, ComponentA.class).get();
        Family family7 = Family.all(ComponentB.class, ComponentC.class).get();
        Family family8 = Family.all(ComponentC.class, ComponentA.class).get();
        Family family9 = Family.all(ComponentC.class, ComponentB.class).get();
        Family family10 = Family.all(ComponentA.class, ComponentB.class, ComponentC.class).get();
        Family family11 = Family.all(ComponentA.class, ComponentB.class).one(ComponentC.class, ComponentD.class)
                .exclude(ComponentE.class, ComponentF.class).get();
        Family family12 = Family.all(ComponentC.class, ComponentD.class).one(ComponentE.class, ComponentF.class)
                .exclude(ComponentA.class, ComponentB.class).get();
        Family family13 = Family.all().get();

        assertNotEquals(family1, family2);
        assertNotEquals(family1, family3);
        assertNotEquals(family1, family4);
        assertNotEquals(family1, family5);
        assertNotEquals(family1, family6);
        assertNotEquals(family1, family7);
        assertNotEquals(family1, family8);
        assertNotEquals(family1, family9);
        assertNotEquals(family1, family10);
        assertNotEquals(family1, family11);
        assertNotEquals(family1, family12);
        assertNotEquals(family1, family13);

        assertNotEquals(family10, family1);
        assertNotEquals(family10, family2);
        assertNotEquals(family10, family3);
        assertNotEquals(family10, family4);
        assertNotEquals(family10, family5);
        assertNotEquals(family10, family6);
        assertNotEquals(family10, family7);
        assertNotEquals(family10, family8);
        assertNotEquals(family10, family9);
        assertNotEquals(family11, family12);
        assertNotEquals(family10, family13);

        assertNotEquals(family1.getIndex(), family2.getIndex());
        assertNotEquals(family1.getIndex(), family3.getIndex());
        assertNotEquals(family1.getIndex(), family4.getIndex());
        assertNotEquals(family1.getIndex(), family5.getIndex());
        assertNotEquals(family1.getIndex(), family6.getIndex());
        assertNotEquals(family1.getIndex(), family7.getIndex());
        assertNotEquals(family1.getIndex(), family8.getIndex());
        assertNotEquals(family1.getIndex(), family9.getIndex());
        assertNotEquals(family1.getIndex(), family10.getIndex());
        assertNotEquals(family11.getIndex(), family12.getIndex());
        assertNotEquals(family1.getIndex(), family13.getIndex());
    }

    @Test
    public void familyEqualityFiltering() {
        Family family1 = Family.all(ComponentA.class).one(ComponentB.class).exclude(ComponentC.class).get();
        Family family2 = Family.all(ComponentB.class).one(ComponentC.class).exclude(ComponentA.class).get();
        Family family3 = Family.all(ComponentC.class).one(ComponentA.class).exclude(ComponentB.class).get();
        Family family4 = Family.all(ComponentA.class).one(ComponentB.class).exclude(ComponentC.class).get();
        Family family5 = Family.all(ComponentB.class).one(ComponentC.class).exclude(ComponentA.class).get();
        Family family6 = Family.all(ComponentC.class).one(ComponentA.class).exclude(ComponentB.class).get();

        assertEquals(family1, family4);
        assertEquals(family2, family5);
        assertEquals(family3, family6);
        assertNotEquals(family1, family2);
        assertNotEquals(family1, family3);
    }

    @Test
    public void entityMatch() {
        Family family = Family.all(ComponentA.class, ComponentB.class).get();

        Entity entity = new Entity();
        entity.add(new ComponentA());
        entity.add(new ComponentB());

        assertTrue(family.matches(entity));

        entity.add(new ComponentC());

        assertTrue(family.matches(entity));
    }

    @Test
    public void entityMismatch() {
        Family family = Family.all(ComponentA.class, ComponentC.class).get();

        Entity entity = new Entity();
        entity.add(new ComponentA());
        entity.add(new ComponentB());

        assertFalse(family.matches(entity));

        entity.remove(ComponentB.class);

        assertFalse(family.matches(entity));
    }

    @Test
    public void entityMatchThenMismatch() {
        Family family = Family.all(ComponentA.class, ComponentB.class).get();

        Entity entity = new Entity();
        entity.add(new ComponentA());
        entity.add(new ComponentB());

        assertTrue(family.matches(entity));

        entity.remove(ComponentA.class);

        assertFalse(family.matches(entity));
    }

    @Test
    public void entityMismatchThenMatch() {
        Family family = Family.all(ComponentA.class, ComponentB.class).get();

        Entity entity = new Entity();
        entity.add(new ComponentA());
        entity.add(new ComponentC());

        assertFalse(family.matches(entity));

        entity.add(new ComponentB());

        assertTrue(family.matches(entity));
    }

    @Test
    public void testEmptyFamily() {
        Family family = Family.all().get();
        Entity entity = new Entity();
        assertTrue(family.matches(entity));
    }

    @Test
    public void familyFiltering() {
        Family family1 = Family.all(ComponentA.class, ComponentB.class).one(ComponentC.class, ComponentD.class)
                .exclude(ComponentE.class, ComponentF.class).get();

        Family family2 = Family.all(ComponentC.class, ComponentD.class).one(ComponentA.class, ComponentB.class)
                .exclude(ComponentE.class, ComponentF.class).get();

        Entity entity = new Entity();

        assertFalse(family1.matches(entity));
        assertFalse(family2.matches(entity));

        entity.add(new ComponentA());
        entity.add(new ComponentB());

        assertFalse(family1.matches(entity));
        assertFalse(family2.matches(entity));

        entity.add(new ComponentC());

        assertTrue(family1.matches(entity));
        assertFalse(family2.matches(entity));

        entity.add(new ComponentD());

        assertTrue(family1.matches(entity));
        assertTrue(family2.matches(entity));

        entity.add(new ComponentE());

        assertFalse(family1.matches(entity));
        assertFalse(family2.matches(entity));

        entity.remove(ComponentE.class);

        assertTrue(family1.matches(entity));
        assertTrue(family2.matches(entity));

        entity.remove(ComponentA.class);

        assertFalse(family1.matches(entity));
        assertTrue(family2.matches(entity));
    }

    @Test
    public void matchWithPooledEngine() {
        PooledEngine engine = new PooledEngine();

        engine.addSystem(new TestSystemA("A"));
        engine.addSystem(new TestSystemB("B"));

        Entity e = engine.createEntity();
        e.add(new ComponentB());
        e.add(new ComponentA());
        engine.addEntity(e);

        Family f = Family.all(ComponentB.class).exclude(ComponentA.class).get();

        assertFalse(f.matches(e));

        engine.clearPools();
    }

    @Test
    public void matchWithPooledEngineInverse() {
        PooledEngine engine = new PooledEngine();

        engine.addSystem(new TestSystemA("A"));
        engine.addSystem(new TestSystemB("B"));

        Entity e = engine.createEntity();
        e.add(new ComponentB());
        e.add(new ComponentA());
        engine.addEntity(e);

        Family f = Family.all(ComponentA.class).exclude(ComponentB.class).get();

        assertFalse(f.matches(e));
        engine.clearPools();
    }

    @Test
    public void matchWithoutSystems() {
        PooledEngine engine = new PooledEngine();

        Entity e = engine.createEntity();
        e.add(new ComponentB());
        e.add(new ComponentA());
        engine.addEntity(e);

        Family f = Family.all(ComponentB.class).exclude(ComponentA.class).get();

        assertFalse(f.matches(e));
        engine.clearPools();
    }

    @Test
    public void matchWithComplexBuilding() {
        Family family = Family.all(ComponentB.class).one(ComponentA.class).exclude(ComponentC.class).get();
        Entity entity = new Entity().add(new ComponentA());
        assertFalse(family.matches(entity));
        entity.add(new ComponentB());
        assertTrue(family.matches(entity));
        entity.add(new ComponentC());
        assertFalse(family.matches(entity));
    }

    private static class ComponentA implements Component {
    }

    private static class ComponentB implements Component {
    }

    private static class ComponentC implements Component {
    }

    private static class ComponentD implements Component {
    }

    private static class ComponentE implements Component {
    }

    private static class ComponentF implements Component {
    }

    static class TestSystemA extends IteratingSystem {

        public TestSystemA(String name) {
            super(Family.all(ComponentA.class).get());
        }

        @Override
        public void processEntity(Entity e, float d) {
        }
    }

    static class TestSystemB extends IteratingSystem {

        public TestSystemB(String name) {
            super(Family.all(ComponentB.class).get());
        }

        @Override
        public void processEntity(Entity e, float d) {
        }
    }
}
