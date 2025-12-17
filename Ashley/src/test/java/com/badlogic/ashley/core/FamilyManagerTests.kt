package com.badlogic.ashley.core

import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class FamilyManagerTests {

    @Test
    fun entitiesForFamily() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = manager.getEntitiesFor(family)

        assertEquals(0, familyEntities.size())

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        entities.add(entity1)
        entities.add(entity2)
        entities.add(entity3)
        entities.add(entity4)

        manager.updateFamilyMembership(entity1)
        manager.updateFamilyMembership(entity2)
        manager.updateFamilyMembership(entity3)
        manager.updateFamilyMembership(entity4)

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entityForFamilyWithRemoval() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val entity = Entity()
        entity.add(ComponentA())

        entities.add(entity)

        manager.updateFamilyMembership(entity)

        val familyEntities = manager.getEntitiesFor(Family.all(ComponentA::class.java).get())

        assertEquals(1, familyEntities.size())
        assertTrue(familyEntities.contains(entity, true))

        entity.removing = true
        entities.removeValue(entity, true)

        manager.updateFamilyMembership(entity)
        entity.removing = false

        assertEquals(0, familyEntities.size())
        assertFalse(familyEntities.contains(entity, true))
    }

    @Test
    fun entitiesForFamilyAfter() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = manager.getEntitiesFor(family)

        assertEquals(0, familyEntities.size())

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        entities.add(entity1)
        entities.add(entity2)
        entities.add(entity3)
        entities.add(entity4)

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        manager.updateFamilyMembership(entity1)
        manager.updateFamilyMembership(entity2)
        manager.updateFamilyMembership(entity3)
        manager.updateFamilyMembership(entity4)

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entitiesForFamilyWithRemoval() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = manager.getEntitiesFor(family)

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        entities.add(entity1)
        entities.add(entity2)
        entities.add(entity3)
        entities.add(entity4)

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        manager.updateFamilyMembership(entity1)
        manager.updateFamilyMembership(entity2)
        manager.updateFamilyMembership(entity3)
        manager.updateFamilyMembership(entity4)

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))

        entity1.remove(ComponentA::class.java)
        entity3.removing = true
        entities.removeValue(entity3, true)

        manager.updateFamilyMembership(entity1)
        manager.updateFamilyMembership(entity3)

        entity3.removing = false

        assertEquals(1, familyEntities.size())
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity1, true))
        assertFalse(familyEntities.contains(entity3, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entitiesForFamilyWithRemovalAndFiltering() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val entitiesWithComponentAOnly = manager.getEntitiesFor(
            Family.all(ComponentA::class.java)
                .exclude(ComponentB::class.java).get()
        )

        val entitiesWithComponentB = manager.getEntitiesFor(Family.all(ComponentB::class.java).get())

        val entity1 = Entity()
        val entity2 = Entity()

        entities.add(entity1)
        entities.add(entity2)

        entity1.add(ComponentA())

        entity2.add(ComponentA())
        entity2.add(ComponentB())

        manager.updateFamilyMembership(entity1)
        manager.updateFamilyMembership(entity2)

        assertEquals(1, entitiesWithComponentAOnly.size())
        assertEquals(1, entitiesWithComponentB.size())

        entity2.remove(ComponentB::class.java)

        manager.updateFamilyMembership(entity2)

        assertEquals(2, entitiesWithComponentAOnly.size())
        assertEquals(0, entitiesWithComponentB.size())
    }

    @Test
    fun entityListenerThrows() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val listener: EntityListener = object : EntityListener {
            override fun entityAdded(entity: Entity) {
                throw GdxRuntimeException("throwing")
            }

            override fun entityRemoved(entity: Entity) {
                throw GdxRuntimeException("throwing")
            }
        }

        manager.addEntityListener(Family.all().get(), 0, listener)

        val entity = Entity()
        entities.add(entity)

        var thrown = false
        try {
            manager.updateFamilyMembership(entity)
        } catch (_: Exception) {
            thrown = true
        }

        assertTrue(thrown)
        assertFalse(manager.notifying())
    }

    private class ComponentA : Component
    private class ComponentB : Component
    private class ComponentC : Component
}
