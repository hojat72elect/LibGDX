package com.badlogic.gdx.ai.tests.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

/**
 *  */
public class Elsa implements Telegraph {

    // is she presently cooking?
    boolean cooking;
    Bob bob;
    // an instance of the state machine class
    private final StateMachine<Elsa, ElsaState> stateMachine;

    public Elsa() {
        this(null);
    }

    public Elsa(Bob bob) {
        stateMachine = new DefaultStateMachine<Elsa, ElsaState>(this, ElsaState.DO_HOUSE_WORK, ElsaState.GLOBAL_STATE);
        this.bob = bob;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    public void update(float delta) {
        stateMachine.update();
    }

    public StateMachine<Elsa, ElsaState> getStateMachine() {
        return stateMachine;
    }

    public Bob getBob() {
        return bob;
    }

    public void setBob(Bob bob) {
        this.bob = bob;
    }

    public boolean isCooking() {
        return cooking;
    }

    public void setCooking(boolean cooking) {
        this.cooking = cooking;
    }
}
