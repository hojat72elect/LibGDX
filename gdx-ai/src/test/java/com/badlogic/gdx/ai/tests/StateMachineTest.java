package com.badlogic.gdx.ai.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.tests.fsm.Bob;
import com.badlogic.gdx.ai.tests.fsm.Elsa;
import com.badlogic.gdx.ai.tests.utils.GdxAiTestUtils;

/**
 * A simple test to demonstrate state machines combined with message handling.
 */
public class StateMachineTest extends ApplicationAdapter {

    Bob bob;
    Elsa elsa;
    float elapsedTime;

    public static void main(String[] argv) {
        GdxAiTestUtils.launch(new StateMachineTest());
    }

    @Override
    public void create() {

        elapsedTime = 0;

        // Create Bob and his wife
        bob = new Bob();
        elsa = new Elsa(bob);
        bob.setElsa(elsa);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        elapsedTime += delta;

        // Update time
        GdxAI.getTimepiece().update(delta);

        if (elapsedTime > 0.8f) {
            // Update Bob and his wife
            bob.update(elapsedTime);
            elsa.update(elapsedTime);

            // Dispatch any delayed messages
            MessageManager.getInstance().update();

            elapsedTime = 0;
        }
    }
}
