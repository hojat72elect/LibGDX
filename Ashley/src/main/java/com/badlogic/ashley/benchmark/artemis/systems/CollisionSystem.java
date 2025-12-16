package com.badlogic.ashley.benchmark.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.ashley.benchmark.artemis.components.PositionComponent;
import com.badlogic.ashley.benchmark.artemis.components.RadiusComponent;

public class CollisionSystem extends EntitySystem {

    public CollisionSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, RadiusComponent.class));
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

    }
}
