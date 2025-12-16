package com.badlogic.ashley.benchmark.ashley.systems;

import com.badlogic.ashley.benchmark.ashley.components.StateComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class StateSystem extends IteratingSystem {
    private final ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);

    public StateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        sm.get(entity).time += deltaTime;
    }
}
