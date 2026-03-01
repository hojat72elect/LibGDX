package com.badlogic.gdx.ai.fsm;

import com.badlogic.gdx.ai.msg.Telegram;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for StackStateMachine
 */
public class StackStateMachineTest {

    private TestEntity owner;
    private TestState stateA;
    private TestState stateB;
    private TestState stateC;
    private TestState globalState;
    private StackStateMachine<TestEntity, TestState> stateMachine;

    @Before
    public void setUp() {
        owner = new TestEntity();
        stateA = new TestState("StateA");
        stateB = new TestState("StateB");
        stateC = new TestState("StateC");
        globalState = new TestState("GlobalState");
        stateMachine = new StackStateMachine<>();
    }

    @Test
    public void testConstructorNoArgs() {
        // Test no-args constructor
        StackStateMachine<TestEntity, TestState> sm = new StackStateMachine<>();
        Assert.assertNull("Owner should be null", sm.getOwner());
        Assert.assertNull("Current state should be null", sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwner() {
        // Test constructor with owner only
        StackStateMachine<TestEntity, TestState> sm = new StackStateMachine<>(owner);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertNull("Current state should be null", sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwnerAndInitialState() {
        // Test constructor with owner and initial state
        StackStateMachine<TestEntity, TestState> sm = new StackStateMachine<>(owner, stateA);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertEquals("Current state should be set", stateA, sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertNull("Global state should be null", sm.getGlobalState());
    }

    @Test
    public void testConstructorWithOwnerInitialStateAndGlobalState() {
        // Test constructor with owner, initial state and global state
        StackStateMachine<TestEntity, TestState> sm = new StackStateMachine<>(owner, stateA, globalState);
        Assert.assertEquals("Owner should be set", owner, sm.getOwner());
        Assert.assertEquals("Current state should be set", stateA, sm.getCurrentState());
        Assert.assertNull("Previous state should be null", sm.getPreviousState());
        Assert.assertEquals("Global state should be set", globalState, sm.getGlobalState());
    }

    @Test
    public void testSetInitialState() {
        // Test setting initial state with empty stack
        stateMachine.setInitialState(stateA);
        Assert.assertEquals("Current state should be set", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null", stateMachine.getPreviousState());

        // Set another initial state (should clear stack)
        stateMachine.setInitialState(stateB);
        Assert.assertEquals("Current state should be updated", stateB, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null", stateMachine.getPreviousState());
    }

    @Test
    public void testSetInitialStateWithExistingStack() {
        // Test setting initial state when stack has states
        stateMachine.setInitialState(stateA);
        stateMachine.changeState(stateB);
        stateMachine.changeState(stateC);

        // Reset call tracking
        stateA.reset();
        stateB.reset();
        stateC.reset();

        stateMachine.setInitialState(stateA);

        Assert.assertEquals("Current state should be set", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null", stateMachine.getPreviousState());
        Assert.assertFalse("State A enter should not be called (no transition)", stateA.enterCalled);
        Assert.assertFalse("State B exit should not be called", stateB.exitCalled);
        Assert.assertFalse("State C exit should not be called", stateC.exitCalled);
    }

    @Test
    public void testGetCurrentState() {
        // Test getting current state
        Assert.assertNull("Current state should be null initially", stateMachine.getCurrentState());

        stateMachine.setInitialState(stateA);
        Assert.assertEquals("Current state should be returned", stateA, stateMachine.getCurrentState());
    }

    @Test
    public void testGetPreviousStateWithEmptyStack() {
        // Test getting previous state when stack is empty
        Assert.assertNull("Previous state should be null initially", stateMachine.getPreviousState());

        stateMachine.setInitialState(stateA);
        Assert.assertNull("Previous state should be null after setting initial state", stateMachine.getPreviousState());
    }

    @Test
    public void testGetPreviousStateWithSingleState() {
        // Test getting previous state with one state in stack
        stateMachine.setInitialState(stateA);
        stateMachine.changeState(stateB);

        Assert.assertEquals("Previous state should be stateA", stateA, stateMachine.getPreviousState());
    }

    @Test
    public void testGetPreviousStateWithMultipleStates() {
        // Test getting previous state with multiple states in stack
        stateMachine.setInitialState(stateA);
        stateMachine.changeState(stateB);
        stateMachine.changeState(stateC);

        Assert.assertEquals("Previous state should be stateB", stateB, stateMachine.getPreviousState());
    }

    @Test
    public void testChangeState() {
        // Test changing state (should push current to stack)
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Reset call tracking
        stateA.reset();
        stateB.reset();

        stateMachine.changeState(stateB);

        Assert.assertEquals("Current state should be changed", stateB, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be stateA", stateA, stateMachine.getPreviousState());
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
        Assert.assertNull("Previous state should be null (stack empty after pop)", stateMachine.getPreviousState());
        Assert.assertTrue("Old state exit should be called", stateB.exitCalled);
        Assert.assertTrue("New state enter should be called", stateA.enterCalled);
    }

    @Test
    public void testRevertToPreviousStateWithEmptyStack() {
        // Test reverting when stack is empty
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        boolean result = stateMachine.revertToPreviousState();

        Assert.assertFalse("Revert should fail", result);
        Assert.assertEquals("Current state should be unchanged", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should still be null", stateMachine.getPreviousState());
    }

    @Test
    public void testRevertToPreviousStateWithMultipleStates() {
        // Test reverting when there are multiple states in stack
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Change to state B, then C
        stateMachine.changeState(stateB);
        stateMachine.changeState(stateC);

        // Reset call tracking
        stateA.reset();
        stateB.reset();
        stateC.reset();

        // Revert to previous state (B)
        boolean result = stateMachine.revertToPreviousState();

        Assert.assertTrue("Revert should succeed", result);
        Assert.assertEquals("Current state should be stateB", stateB, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be stateA", stateA, stateMachine.getPreviousState());
        Assert.assertTrue("Old state exit should be called", stateC.exitCalled);
        Assert.assertTrue("New state enter should be called", stateB.enterCalled);
    }

    @Test
    public void testMultipleStateChanges() {
        // Test multiple state changes to verify stack behavior
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
        Assert.assertEquals("Previous state should be A", stateA, stateMachine.getPreviousState());
    }

    @Test
    public void testStateStackBehavior() {
        // Test that state stack works correctly with multiple pushes and pops
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Reset call tracking
        stateA.reset();
        stateB.reset();
        stateC.reset();

        // Push multiple states
        stateMachine.changeState(stateB);
        stateMachine.changeState(stateC);
        stateMachine.changeState(stateA); // Push A on top of C

        // Verify stack order: A (bottom), B, C, A (top)
        // Current should be A, previous should be C
        Assert.assertEquals("Current state should be A", stateA, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be C", stateC, stateMachine.getPreviousState());

        // Revert should pop A and go to C
        stateMachine.revertToPreviousState();
        Assert.assertEquals("Current state should be C", stateC, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be B", stateB, stateMachine.getPreviousState());
    }

    @Test
    public void testUpdateWithStates() {
        // Test that update works with states (inherited from DefaultStateMachine)
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);
        stateMachine.setGlobalState(globalState);

        stateMachine.update();

        Assert.assertTrue("Current state should be updated", stateA.updateCalled);
        Assert.assertEquals("Update should be called with owner", owner, stateA.lastUpdateEntity);
        Assert.assertTrue("Global state should be updated", globalState.updateCalled);
        Assert.assertEquals("Global update should be called with owner", owner, globalState.lastUpdateEntity);
    }

    @Test
    public void testHandleMessage() {
        // Test that message handling works (inherited from DefaultStateMachine)
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        stateMachine.changeState(stateB);

        // Reset call tracking
        stateA.reset();
        stateB.reset();

        Telegram telegram = new Telegram();
        telegram.message = 1;

        // Configure current state to handle message
        stateB.handleMessageResult = true;

        boolean result = stateMachine.handleMessage(telegram);

        Assert.assertTrue("Message should be handled", result);
        Assert.assertTrue("Current state onMessage should be called", stateB.onMessageCalled);
        Assert.assertEquals("onMessage should be called with owner", owner, stateB.lastOnMessageEntity);
        Assert.assertEquals("onMessage should be called with telegram", telegram, stateB.lastOnMessageTelegram);
    }

    @Test
    public void testIsInState() {
        // Test that state checking works (inherited from DefaultStateMachine)
        stateMachine.setInitialState(stateA);

        Assert.assertTrue("Should be in initial state", stateMachine.isInState(stateA));
        Assert.assertFalse("Should not be in other state", stateMachine.isInState(stateB));

        stateMachine.changeState(stateB);
        Assert.assertFalse("Should not be in old state", stateMachine.isInState(stateA));
        Assert.assertTrue("Should be in new state", stateMachine.isInState(stateB));
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

    @Test
    public void testStackClearOnSetInitialState() {
        // Test that setting initial state clears the stack
        stateMachine.setOwner(owner);
        stateMachine.setInitialState(stateA);

        // Add some states to stack
        stateMachine.changeState(stateB);
        stateMachine.changeState(stateC);

        Assert.assertEquals("Current state should be C", stateC, stateMachine.getCurrentState());
        Assert.assertEquals("Previous state should be B", stateB, stateMachine.getPreviousState());

        // Set initial state (should clear stack)
        stateMachine.setInitialState(stateA);

        Assert.assertEquals("Current state should be A", stateA, stateMachine.getCurrentState());
        Assert.assertNull("Previous state should be null (stack cleared)", stateMachine.getPreviousState());
    }

    private static class TestEntity {
    }

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
