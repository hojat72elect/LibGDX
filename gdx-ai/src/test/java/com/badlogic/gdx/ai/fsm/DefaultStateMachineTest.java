package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.msg.Telegram;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for DefaultStateMachine
 */
public class DefaultStateMachineTest {

    private TestEntity owner;
    private TestState stateA;
    private TestState stateB;
    private TestState stateC;
    private TestState globalState;
    private DefaultStateMachine<TestEntity, TestState> stateMachine;

    @Before
    public void setUp() {
        owner = new TestEntity();
        stateA = new TestState("StateA");
        stateB = new TestState("StateB");
        stateC = new TestState("StateC");
        globalState = new TestState("GlobalState");
        stateMachine = new DefaultStateMachine<>();
    }

    @Test
    public void testConstructorNoArgs() {
        // Test no-args constructor
        DefaultStateMachine<TestEntity, TestState> sm = new DefaultStateMachine<>();
        Assert.assertNull("Owner should be null", sm.getOwner());
        Assert.assertNull("Current state should be null", sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwner() {
        // Test constructor with owner only
        DefaultStateMachine<TestEntity, TestState> sm = new DefaultStateMachine<>(owner);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertNull("Current state should be null", sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwnerAndInitialState() {
        // Test constructor with owner and initial state
        DefaultStateMachine<TestEntity, TestState> sm = new DefaultStateMachine<>(owner, stateA);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertEquals("Current state should be set", stateA, sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwnerInitialStateAndGlobalState() {
        // Test constructor with owner, initial state and global state
        DefaultStateMachine<TestEntity, TestState> sm = new DefaultStateMachine<>(owner, stateA, globalState);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertEquals("Current state should be set", stateA, sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertEquals("Global state should be set", globalState, sm.getGlobalState());
    }

    @Test
    public void testSetOwner() {
        // Test setting owner
        stateMachine.setOwner(owner);
        Assert.assertEquals("Owner should be set", owner, stateMachine.getOwner());

        TestEntity newOwner = new TestEntity();
        stateMachine.setOwner(newOwner);
        Assert.assertEquals("Owner should be updated", newOwner, stateMachine.getOwner());
    }

    @Test
    public void testSetInitialState() {
        // Test setting initial state
        stateMachine.setInitialState(stateA);
        Assert.assertEquals("Current state should be set", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null", stateMachine.getPreviousState());

        // Set another initial state
        stateMachine.setInitialState(stateB);
        Assert.assertEquals("Current state should be updated", stateB, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should still be null", stateMachine.getPreviousState());
    }

    @Test
    public void testSetGlobalState() {
        // Test setting global state
        stateMachine.setGlobalState(globalState);
        Assert.assertEquals("Global state should be set", globalState, stateMachine.getGlobalState());

        TestState newGlobalState = new TestState("NewGlobal");
        stateMachine.setGlobalState(newGlobalState);
        Assert.assertEquals("Global state should be updated", newGlobalState, stateMachine.getGlobalState());
    }

    @Test
    public void testGetCurrentState() {
        // Test getting current state
        Assert.assertNull("Current state should be null initially", stateMachine.getCurrentState());

        stateMachine.setInitialState(stateA);
        Assert.assertEquals("Current state should be returned", stateA, stateMachine.getCurrentState());
    }

    @Test
    public void testGetGlobalState() {
        // Test getting global state
        Assert.assertNull("Global state should be null initially", stateMachine.getGlobalState());

        stateMachine.setGlobalState(globalState);
        Assert.assertEquals("Global state should be returned", globalState, stateMachine.getGlobalState());
    }

    @Test
    public void testGetPreviousState() {
        // Test getting previous state
        Assert.assertNull("Previous state should be null initially", stateMachine.getPreviousState());

        stateMachine.setInitialState(stateA);
        Assert.assertNull("Previous state should still be null after setting initial state", stateMachine.getPreviousState());
    }

    @Test
    public void testUpdateWithNoStates() {
        // Test update with no states
        stateMachine.setOwner(owner);
        stateMachine.update(); // Should not throw exception

        Assert.assertEquals("Owner should not be null", owner, stateMachine.getOwner());
    }

    @Test
    public void testUpdateWithCurrentStateOnly() {
        // Test update with current state only
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);
        stateMachine.update();

        Assert.assertTrue("Current state should be updated", stateA.updateCalled);
        Assert.assertEquals("Update should be called with owner", owner, stateA.lastUpdateEntity);
        Assert.assertFalse("Global state should not be called", globalState.updateCalled);
    }

    @Test
    public void testUpdateWithGlobalStateOnly() {
        // Test update with global state only
        stateMachine.setOwner(owner);
        stateMachine.setGlobalState(globalState);
        stateMachine.update();

        Assert.assertTrue("Global state should be updated", globalState.updateCalled);
        Assert.assertEquals("Update should be called with owner", owner, globalState.lastUpdateEntity);
        Assert.assertFalse("Current state should not be called", stateA.updateCalled);
    }

    @Test
    public void testUpdateWithBothStates() {
        // Test update with both current and global states
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);
        stateMachine.setGlobalState(globalState);
        stateMachine.update();

        Assert.assertTrue("Global state should be updated", globalState.updateCalled);
        Assert.assertTrue("Current state should be updated", stateA.updateCalled);
        Assert.assertEquals("Global update should be called with owner", owner, globalState.lastUpdateEntity);
        Assert.assertEquals("Current update should be called with owner", owner, stateA.lastUpdateEntity);
    }

    @Test
    public void testChangeState() {
        // Test changing state
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Reset call tracking
        stateA.reset();
        stateB.reset();

        stateMachine.changeState(stateB);

        Assert.assertEquals("Current state should be changed", stateB, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be set", stateA, stateMachine.getPreviousState());
        Assert.assertTrue("Old state exit should be called", stateA.exitCalled);
        Assert.assertEquals("Exit should be called with owner", owner, stateA.lastExitEntity);
        Assert.assertTrue("New state enter should be called", stateB.enterCalled);
        Assert.assertEquals("Enter should be called with owner", owner, stateB.lastEnterEntity);
    }

    @Test
    public void testChangeStateFromNull() {
        // Test changing state from null
        stateMachine.setOwner(owner);

        stateMachine.changeState(stateA);

        Assert.assertEquals("Current state should be set", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null", stateMachine.getPreviousState());
        Assert.assertTrue("New state enter should be called", stateA.enterCalled);
        Assert.assertEquals("Enter should be called with owner", owner, stateA.lastEnterEntity);
        Assert.assertFalse("Exit should not be called (no previous state)", stateA.exitCalled);
    }

    @Test
    public void testChangeStateToNull() {
        // Test changing state to null
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Reset call tracking
        stateA.reset();

        stateMachine.changeState(null);

        Assert.assertNull("Current state should be null", stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be set", stateA, stateMachine.getPreviousState());
        Assert.assertTrue("Old state exit should be called", stateA.exitCalled);
        Assert.assertEquals("Exit should be called with owner", owner, stateA.lastExitEntity);
    }

    @Test
    public void testRevertToPreviousState() {
        // Test reverting to previous state
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Change to state B
        stateMachine.changeState(stateB);

        // Reset call tracking
        stateA.reset();
        stateB.reset();

        // Revert to previous state
        boolean result = stateMachine.revertToPreviousState();

        Assert.assertTrue("Revert should succeed", result);
        Assert.assertEquals("Current state should be previous state", stateA, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be updated", stateB, stateMachine.getPreviousState());
        Assert.assertTrue("Old state exit should be called", stateB.exitCalled);
        Assert.assertTrue("New state enter should be called", stateA.enterCalled);
    }

    @Test
    public void testRevertToPreviousStateWithNoPreviousState() {
        // Test reverting when there's no previous state
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        boolean result = stateMachine.revertToPreviousState();

        Assert.assertFalse("Revert should fail", result);
        Assert.assertEquals("Current state should be unchanged", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should still be null", stateMachine.getPreviousState());
    }

    @Test
    public void testRevertToPreviousStateWithNullPreviousState() {
        // Test reverting when previous state is null
        stateMachine.setOwner(owner);
        stateMachine.changeState(stateA); // previous state is null

        boolean result = stateMachine.revertToPreviousState();

        Assert.assertFalse("Revert should fail", result);
        Assert.assertEquals("Current state should be unchanged", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should still be null", stateMachine.getPreviousState());
    }

    @Test
    public void testIsInState() {
        // Test checking if in state
        stateMachine.setInitialState(stateA);

        Assert.assertTrue("Should be in initial state", stateMachine.isInState(stateA));
        Assert.assertFalse("Should not be in other state", stateMachine.isInState(stateB));

        stateMachine.changeState(stateB);
        Assert.assertFalse("Should not be in old state", stateMachine.isInState(stateA));
        Assert.assertTrue("Should be in new state", stateMachine.isInState(stateB));
    }

    @Test
    public void testIsInStateWithNullCurrentState() {
        // Test checking state when current state is null
        Assert.assertFalse("Should not be in state when current is null", stateMachine.isInState(stateA));
    }

    @Test
    public void testHandleMessageWithCurrentState() {
        // Test handling message with current state
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure state to handle message
        stateA.handleMessageResult = true;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertTrue("Message should be handled", result);
        Assert.assertTrue("Current state onMessage should be called", stateA.onMessageCalled);
        Assert.assertEquals("onMessage should be called with owner", owner, stateA.lastOnMessageEntity);
        Assert.assertEquals("onMessage should be called with telegram", telegram, stateA.lastOnMessageTelegram);
        Assert.assertFalse("Global state onMessage should not be called", globalState.onMessageCalled);
    }

    @Test
    public void testHandleMessageWithGlobalState() {
        // Test handling message with global state when current state doesn't handle it
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);
        stateMachine.setGlobalState(globalState);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure current state to not handle message, global state to handle it
        stateA.handleMessageResult = false;
        globalState.handleMessageResult = true;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertTrue("Message should be handled", result);
        Assert.assertTrue("Current state onMessage should be called", stateA.onMessageCalled);
        Assert.assertTrue("Global state onMessage should be called", globalState.onMessageCalled);
        Assert.assertEquals("Global onMessage should be called with owner", owner, globalState.lastOnMessageEntity);
        Assert.assertEquals("Global onMessage should be called with telegram", telegram, globalState.lastOnMessageTelegram);
    }

    @Test
    public void testHandleMessageWithNeitherStateHandling() {
        // Test handling message when neither state handles it
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);
        stateMachine.setGlobalState(globalState);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure both states to not handle message
        stateA.handleMessageResult = false;
        globalState.handleMessageResult = false;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertFalse("Message should not be handled", result);
        Assert.assertTrue("Current state onMessage should be called", stateA.onMessageCalled);
        Assert.assertTrue("Global state onMessage should be called", globalState.onMessageCalled);
    }

    @Test
    public void testHandleMessageWithNullCurrentState() {
        // Test handling message when current state is null
        stateMachine.setOwner(owner);
        stateMachine.setGlobalState(globalState);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure global state to handle message
        globalState.handleMessageResult = true;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertTrue("Message should be handled", result);
        Assert.assertFalse("Current state onMessage should not be called", stateA.onMessageCalled);
        Assert.assertTrue("Global state onMessage should be called", globalState.onMessageCalled);
    }

    @Test
    public void testHandleMessageWithNullGlobalState() {
        // Test handling message when global state is null
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure current state to not handle message
        stateA.handleMessageResult = false;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertFalse("Message should not be handled", result);
        Assert.assertTrue("Current state onMessage should be called", stateA.onMessageCalled);
    }

    @Test
    public void testHandleMessageWithBothStatesNull() {
        // Test handling message when both states are null
        stateMachine.setOwner(owner);

        Telegram telegram = new Telegram();
        telegram.message = 1;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertFalse("Message should not be handled", result);
    }

    @Test
    public void testMultipleStateChanges() {
        // Test multiple state changes
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Change to state B
        stateMachine.changeState(stateB);
        Assert.assertEquals("Current state should be B", stateB, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be A", stateA, stateMachine.getPreviousState());

        // Change to state C
        stateMachine.changeState(stateC);
        Assert.assertEquals("Current state should be C", stateC, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be B", stateB, stateMachine.getPreviousState());

        // Revert to previous state (B)
        stateMachine.revertToPreviousState();
        Assert.assertEquals("Current state should be B", stateB, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be C", stateC, stateMachine.getPreviousState());
    }

    @Test
    public void testStateMachineWithSameStateInstance() {
        // Test state machine behavior with the same state instance (singleton pattern)
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Change to the same state
        stateMachine.changeState(stateA);

        Assert.assertEquals("Current state should be the same", stateA, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be the same", stateA, stateMachine.getPreviousState());
        Assert.assertTrue("Exit should be called", stateA.exitCalled);
        Assert.assertTrue("Enter should be called", stateA.enterCalled);
    }

    /**
     * Test entity class
     */
    private static class TestEntity {}

    /**
     * Test state implementation
     */
    private static class TestState implements State<TestEntity> {
        public final String name;

        public boolean enterCalled = false;
        public boolean updateCalled = false;
        public boolean exitCalled = false;
        public boolean onMessageCalled = false;

        public TestEntity lastEnterEntity;
        public TestEntity lastUpdateEntity;
        public TestEntity lastExitEntity;
        public TestEntity lastOnMessageEntity;
        public Telegram lastOnMessageTelegram;

        public boolean handleMessageResult = false;

        public TestState(String name) {
            this.name = name;
        }

        public void reset() {
            enterCalled = false;
            updateCalled = false;
            exitCalled = false;
            onMessageCalled = false;
            lastEnterEntity = null;
            lastUpdateEntity = null;
            lastExitEntity = null;
            lastOnMessageEntity = null;
            lastOnMessageTelegram = null;
        }

        @Override
        public void enter(TestEntity entity) {
            enterCalled = true;
            lastEnterEntity = entity;
        }

        @Override
        public void update(TestEntity entity) {
            updateCalled = true;
            lastUpdateEntity = entity;
        }

        @Override
        public void exit(TestEntity entity) {
            exitCalled = true;
            lastExitEntity = entity;
        }

        @Override
        public boolean onMessage(TestEntity entity, Telegram telegram) {
            onMessageCalled = true;
            lastOnMessageEntity = entity;
            lastOnMessageTelegram = telegram;
            return handleMessageResult;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
