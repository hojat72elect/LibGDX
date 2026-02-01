package com.badlogic.ashley.core

import org.junit.Assert.*
import org.junit.Test

class FamilyTest {
    private class ComponentA : Component
    private class ComponentB : Component
    private class ComponentC : Component
    private class ComponentD : Component
    private class ComponentE : Component
    private class ComponentF : Component

    @Test
    fun validFamily() {
        assertNotNull(Family.all(ComponentA::class.java).get())
        assertNotNull(Family.one(ComponentB::class.java).get())
        assertNotNull(Family.exclude(ComponentC::class.java).get())
        assertNotNull(Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get())
    }

    @Test
    fun sameFamily() {
        val family1 = Family.all(ComponentA::class.java).get()
        val family2 = Family.all(ComponentA::class.java).get()
        val family3 = Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get()
        val family4 = Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get()
        val family5 = Family.all(ComponentA::class.java).one(ComponentB::class.java).get()
        val family6 = Family.all(ComponentA::class.java).one(ComponentB::class.java).get()

        assertEquals(family1, family2)
        assertEquals(family3, family4)
        assertEquals(family5, family6)
        assertEquals(family1.index, family2.index)
        assertEquals(family3.index, family4.index)
        assertEquals(family5.index, family6.index)
    }

    @Test
    fun differentFamily() {
        val family1 = Family.all(ComponentA::class.java).get()
        val family2 = Family.all(ComponentB::class.java).get()
        val family3 = Family.all(ComponentA::class.java).one(ComponentB::class.java).exclude(ComponentC::class.java).get()
        val family4 = Family.all(ComponentA::class.java).one(ComponentB::class.java).get()

        assertNotEquals(family1, family2)
        assertNotEquals(family3, family4)
        assertNotEquals(family1.index, family2.index)
        assertNotEquals(family3.index, family4.index)
    }

    @Test
    fun familyEquality() {
        val family1 = Family.all(ComponentA::class.java).get()
        val family2 = Family.all(ComponentA::class.java).get()
        val family3 = Family.all(ComponentB::class.java).get()

        assertEquals(family1, family2)
        assertNotEquals(family1, family3)
    }

    @Test
    fun entityMatch() {
        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentB())

        assertTrue(family.matches(entity))
    }

    @Test
    fun entityMismatch() {
        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentC())

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
    fun familyFiltering() {
        val family1 = Family.all(ComponentA::class.java, ComponentB::class.java).one(ComponentC::class.java, ComponentD::class.java).exclude(ComponentE::class.java, ComponentF::class.java).get()
        val entity = Entity()

        assertFalse(family1.matches(entity))

        entity.add(ComponentA())
        entity.add(ComponentB())

        assertFalse(family1.matches(entity))

        entity.add(ComponentC())

        assertTrue(family1.matches(entity))

        entity.add(ComponentE())

        assertFalse(family1.matches(entity))

        entity.remove(ComponentE::class.java)

        assertTrue(family1.matches(entity))

        entity.remove(ComponentC::class.java)

        assertFalse(family1.matches(entity))

        entity.add(ComponentD())

        assertTrue(family1.matches(entity))

        entity.add(ComponentF())

        assertFalse(family1.matches(entity))
    }
}
