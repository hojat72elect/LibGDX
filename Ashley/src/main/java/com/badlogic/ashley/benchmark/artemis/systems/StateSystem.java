package com.badlogic.ashley.benchmark.artemis.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.ashley.benchmark.artemis.components.StateComponent;

public class StateSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<StateComponent> sm;

    public StateSystem() {
        super(Aspect.getAspectForAll(StateComponent.class));
    }

    @Override
    protected void initialize() {
        sm = world.getMapper(StateComponent.class);
    }

    @Override
    protected void process(Entity entity) {
        sm.get(entity).time += world.getDelta();
    }
}
