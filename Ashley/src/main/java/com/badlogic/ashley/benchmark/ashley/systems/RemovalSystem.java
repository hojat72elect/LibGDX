package com.badlogic.ashley.benchmark.ashley.systems;

import com.badlogic.ashley.benchmark.ashley.components.RemovalComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class RemovalSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RemovalComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        while (entities.size() > 0) {
            getEngine().removeEntity(entities.get(0));
        }
    }
}
