package com.badlogic.ashley.benchmark.artemis.components;

import com.artemis.Component;

public class StateComponent extends Component {
    public float time = 0.0f;
    private int state = 0;

    public int get() {
        return state;
    }

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }
}
