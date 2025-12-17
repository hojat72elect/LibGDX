package com.badlogic.ashley.core

import com.badlogic.ashley.systems.IteratingSystem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FamilyTests {

    @Test
    fun validFamily() {
        assertNotNull(Family.all().get())
        assertNotNull(Family.all(ComponentA::class.java).get())
        assertNotNull(Family.all(ComponentB::class.java).get())
        assertNotNull(Family.all(ComponentC::class.java).get())
        assertNotNull(Family.all(ComponentA::class.java, ComponentB::class.java).get())
        assertNotNull(Family.all(ComponentA::class.java, ComponentC::class.java).get())
        assertNotNull(Family.all(ComponentB::class.java, ComponentA::class.java).get())
        assertNotNull(Family.all(ComponentB::class.java, ComponentC::class.java).get())
        assertNotNull(Family.all(ComponentC::class.java, ComponentA::class.java).get())
        assertNotNull(Family.all(ComponentC::class.java, ComponentB::class.java).get())
        assertNotNull(Family.all(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java).get())
        Family.all(ComponentA::class.java, ComponentB::class.java).get()
        assertNotNull(
            Family.one(ComponentC::class.java, ComponentD::class.java)
                .exclude(ComponentE::class.java, ComponentF::class.java).get()
        )
    }

    @Test
    fun sameFamily() {
        val family1 = Family.all(ComponentA::class.java).get()
        val family2 = Family.all(ComponentA::class.java).get()
        val family3 = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val family4 = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val family5 = Family.all(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java).get()
        val family6 = Family.all(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java).get()
        val family7 = Family.all(ComponentA::class.java, ComponentB::class.java)
            .one(ComponentC::class.java, ComponentD::class.java)
            .exclude(ComponentE::class.java, ComponentF::class.java)
            .get()
        val family8 = Family.all(ComponentA::class.java, ComponentB::class.java)
            .one(ComponentC::class.java, ComponentD::class.java)
            .exclude(ComponentE::class.java, ComponentF::class.java)
            .get()
        val family9 = Family.all().get()
        val family10 = Family.all().get()

        assertEquals(family1, family2)
        assertEquals(family2, family1)
        assertEquals(family3, family4)
        assertEquals(family4, family3)
        assertEquals(family5, family6)
        assertEquals(family6, family5)
        assertEquals(family7, family8)
        assertEquals(family8, family7)
        assertEquals(family9, family10)

        assertEquals(family1.index, family2.index)
        assertEquals(family3.index, family4.index)
        assertEquals(family5.index, family6.index)
        assertEquals(family7.index, family8.index)
        assertEquals(family9.index, family10.index)
    }

    @Test
    fun differentFamily() {
        val family1 = Family.all(ComponentA::class.java).get()
        val family2 = Family.all(ComponentB::class.java).get()
        val family3 = Family.all(ComponentC::class.java).get()
        val family4 = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val family5 = Family.all(ComponentA::class.java, ComponentC::class.java).get()
        val family6 = Family.all(ComponentB::class.java, ComponentA::class.java).get()
        val family7 = Family.all(ComponentB::class.java, ComponentC::class.java).get()
        val family8 = Family.all(ComponentC::class.java, ComponentA::class.java).get()
        val family9 = Family.all(ComponentC::class.java, ComponentB::class.java).get()
        val family10 = Family.all(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java).get()
        val family11 = Family.all(ComponentA::class.java, ComponentB::class.java).one(ComponentC::class.java, ComponentD::class.java)
            .exclude(ComponentE::class.java, ComponentF::class.java).get()
        val family12 = Family.all(ComponentC::class.java, ComponentD::class.java).one(ComponentE::class.java, ComponentF::class.java)
            .exclude(ComponentA::class.java, ComponentB::class.java).get()
        val family13 = Family.all().get()

        assertNotEquals(family1, family2)
        assertNotEquals(family1, family3)
        assertNotEquals(family1, family4)
        assertNotEquals(family1, family5)
        assertNotEquals(family1, family6)
        assertNotEquals(family1, family7)
        assertNotEquals(family1, family8)
        assertNotEquals(family1, family9)
        assertNotEquals(family1, family10)
        assertNotEquals(family1, family11)
        assertNotEquals(family1, family12)
        assertNotEquals(family1, family13)

        assertNotEquals(family10, family1)
        assertNotEquals(family10, family2)
        assertNotEquals(family10, family3)
        assertNotEquals(family10, family4)
        assertNotEquals(family10, family5)
        assertNotEquals(family10, family6)
        assertNotEquals(family10, family7)
        assertNotEquals(family10, family8)
        assertNotEquals(family10, family9)
        assertNotEquals(family11, family12)
        assertNotEquals(family10, family13)

        assertNotEquals(family1.index, family2.index)
        assertNotEquals(family1.index, family3.index)
        assertNotEquals(family1.index, family4.index)
        assertNotEquals(family1.index, family5.index)
        assertNotEquals(family1.index, family6.index)
        assertNotEquals(family1.index, family7.index)
        assertNotEquals(family1.index, family8.index)
        assertNotEquals(family1.index, family9.index)
        assertNotEquals(family1.index, family10.index)
        assertNotEquals(family11.index, family12.index)
        assertNotEquals(family1.index, family13.index)
    }

    @Test
    fun familyEqualityFiltering() {
        val family1 = Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get()
        val family2 = Family.all(ComponentB::class.java).one(ComponentC::class.java).exclude(ComponentA::class.java).get()
        val family3 = Family.all(ComponentC::class.java).one(ComponentA::class.java).exclude(ComponentB::class.java).get()
        val family4 = Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get()
        val family5 = Family.all(ComponentB::class.java).one(ComponentC::class.java).exclude(ComponentA::class.java).get()
        val family6 = Family.all(ComponentC::class.java).one(ComponentA::class.java).exclude(ComponentB::class.java).get()

        assertEquals(family1, family4)
        assertEquals(family2, family5)
        assertEquals(family3, family6)
        assertNotEquals(family1, family2)
        assertNotEquals(family1, family3)
    }

    @Test
    fun entityMatch() {
        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()

        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentB())

        assertTrue(family.matches(entity))

        entity.add(ComponentC())

        assertTrue(family.matches(entity))
    }

    @Test
    fun entityMismatch() {
        val family = Family.all(ComponentA::class.java, ComponentC::class.java).get()

        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentB())

        assertFalse(family.matches(entity))

        entity.remove(ComponentB::class.java)

        assertFalse(family.matches(entity))
    }

    @Test
    fun entityMatchThenMismatch() {
        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()

        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentB())

        assertTrue(family.matches(entity))

        entity.remove(ComponentA::class.java)

        assertFalse(family.matches(entity))
    }

    @Test
    fun entityMismatchThenMatch() {
        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()

        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentC())

        assertFalse(family.matches(entity))

        entity.add(ComponentB())

        assertTrue(family.matches(entity))
    }

    @Test
    fun testEmptyFamily() {
        val family = Family.all().get()
        val entity = Entity()
        assertTrue(family.matches(entity))
    }

    @Test
    fun familyFiltering() {
        val family1 = Family.all(ComponentA::class.java, ComponentB::class.java).one(ComponentC::class.java, ComponentD::class.java)
            .exclude(ComponentE::class.java, ComponentF::class.java).get()

        val family2 = Family.all(ComponentC::class.java, ComponentD::class.java).one(ComponentA::class.java, ComponentB::class.java)
            .exclude(ComponentE::class.java, ComponentF::class.java).get()

        val entity = Entity()

        assertFalse(family1.matches(entity))
        assertFalse(family2.matches(entity))

        entity.add(ComponentA())
        entity.add(ComponentB())

        assertFalse(family1.matches(entity))
        assertFalse(family2.matches(entity))

        entity.add(ComponentC())

        assertTrue(family1.matches(entity))
        assertFalse(family2.matches(entity))

        entity.add(ComponentD())

        assertTrue(family1.matches(entity))
        assertTrue(family2.matches(entity))

        entity.add(ComponentE())

        assertFalse(family1.matches(entity))
        assertFalse(family2.matches(entity))

        entity.remove(ComponentE::class.java)

        assertTrue(family1.matches(entity))
        assertTrue(family2.matches(entity))

        entity.remove(ComponentA::class.java)

        assertFalse(family1.matches(entity))
        assertTrue(family2.matches(entity))
    }

    @Test
    fun matchWithPooledEngine() {
        val engine = PooledEngine()

        engine.addSystem(TestSystemA())
        engine.addSystem(TestSystemB())

        val e = engine.createEntity()
        e.add(ComponentB())
        e.add(ComponentA())
        engine.addEntity(e)

        val f = Family.all(ComponentB::class.java).exclude(ComponentA::class.java).get()

        assertFalse(f.matches(e))

        engine.clearPools()
    }

    @Test
    fun matchWithPooledEngineInverse() {
        val engine = PooledEngine()

        engine.addSystem(TestSystemA())
        engine.addSystem(TestSystemB())

        val e = engine.createEntity()
        e.add(ComponentB())
        e.add(ComponentA())
        engine.addEntity(e)

        val f = Family.all(ComponentA::class.java).exclude(ComponentB::class.java).get()

        assertFalse(f.matches(e))
        engine.clearPools()
    }

    @Test
    fun matchWithoutSystems() {
        val engine = PooledEngine()

        val e = engine.createEntity()
        e.add(ComponentB())
        e.add(ComponentA())
        engine.addEntity(e)

        val f = Family.all(ComponentB::class.java).exclude(ComponentA::class.java).get()

        assertFalse(f.matches(e))
        engine.clearPools()
    }

    @Test
    fun matchWithComplexBuilding() {
        val family = Family.all(ComponentB::class.java).one(ComponentA::class.java).exclude(ComponentC::class.java).get()
        val entity = Entity().add(ComponentA())
        assertFalse(family.matches(entity))
        entity.add(ComponentB())
        assertTrue(family.matches(entity))
        entity.add(ComponentC())
        assertFalse(family.matches(entity))
    }

    private class ComponentA : Component
    private class ComponentB : Component
    private class ComponentC : Component
    private class ComponentD : Component
    private class ComponentE : Component
    private class ComponentF : Component

    internal class TestSystemA() : IteratingSystem(Family.all(ComponentA::class.java).get()) {
        override fun processEntity(e: Entity?, d: Float) {}
    }

    internal class TestSystemB() : IteratingSystem(Family.all(ComponentB::class.java).get()) {
        override fun processEntity(e: Entity?, d: Float) {}
    }
}
