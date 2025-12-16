package com.badlogic.ashley.benchmark.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.ashley.benchmark.artemis.components.RemovalComponent;

public class RemovalSystem extends EntitySystem {

    public RemovalSystem() {
        super(Aspect.getAspectForAll(RemovalComponent.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        while (!entities.isEmpty()) {
            world.deleteEntity(entities.get(0));
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
