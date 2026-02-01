package com.badlogic.ashley.core

import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class FamilyManagerTest {

    private class ComponentA : Component
    private class ComponentB : Component

    @Test
    fun entitiesForFamily() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java).get()
        val familyEntities = manager.getEntitiesFor(family)

        assertEquals(0, familyEntities.size())

        val entity1 = Entity()
        entity1.add(ComponentA())
        entities.add(entity1)
        manager.updateFamilyMembership(entity1)

        assertEquals(1, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))

        val entity2 = Entity()
        entity2.add(ComponentB())
        entities.add(entity2)
        manager.updateFamilyMembership(entity2)

        assertEquals(1, familyEntities.size())
        assertFalse(familyEntities.contains(entity2, true))

        val entity3 = Entity()
        entity3.add(ComponentA())
        entities.add(entity3)
        manager.updateFamilyMembership(entity3)

        assertEquals(2, familyEntities.size())
        assertTrue(familyEntities.contains(entity3, true))
    }

    @Test
    fun entityListener() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java).get()
        val listener = mock(EntityListener::class.java)
        manager.addEntityListener(family, 0, listener)

        val entity1 = Entity()
        entity1.add(ComponentA())
        entities.add(entity1)
        manager.updateFamilyMembership(entity1)

        verify(listener).entityAdded(entity1)

        val entity2 = Entity()
        entity2.add(ComponentB())
        entities.add(entity2)
        manager.updateFamilyMembership(entity2)

        verify(listener, never()).entityAdded(entity2)

        entity1.remove(ComponentA::class.java)
        manager.updateFamilyMembership(entity1)

        verify(listener).entityRemoved(entity1)
    }

    @Test
    fun removeEntityListener() {
        val entities = Array<Entity>()
        val immutableEntities = ImmutableArray(entities)
        val manager = FamilyManager(immutableEntities)

        val family = Family.all(ComponentA::class.java).get()
        val listener = mock(EntityListener::class.java)
        manager.addEntityListener(family, 0, listener)
        manager.removeEntityListener(listener)

        val entity = Entity()
        entity.add(ComponentA())
        entities.add(entity)
        manager.updateFamilyMembership(entity)

        verify(listener, never()).entityAdded(entity)
    }
}
