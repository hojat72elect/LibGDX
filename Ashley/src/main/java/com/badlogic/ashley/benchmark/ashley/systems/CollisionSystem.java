package com.badlogic.ashley.benchmark.ashley.systems;

import com.badlogic.ashley.benchmark.ashley.components.RadiusComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class CollisionSystem extends EntitySystem {
    ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RadiusComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

    }
}
