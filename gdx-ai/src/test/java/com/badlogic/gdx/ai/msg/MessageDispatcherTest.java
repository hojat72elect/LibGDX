package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.utils.Pool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageDispatcherTest {

    private MessageDispatcher dispatcher;
    private DefaultTimepiece timepiece;
    private TestTelegraph listener1;
    private TestTelegraph listener2;
    private TestTelegraph sender;
    private TestTelegramProvider provider;
    private TestPool telegramPool;

    @Before
    public void setUp() {
        // Set up GdxAI logger and timepiece to avoid null pointer exceptions
        GdxAI.setLogger(new com.badlogic.gdx.ai.Logger() {
            @Override
            public void debug(String tag, String message) {
                // Suppress debug output during tests
            }

            @Override
            public void debug(String tag, String message, Throwable exception) {
                // Suppress debug output during tests
            }

            @Override
            public void info(String tag, String message) {
                // Suppress info output during tests
            }

            @Override
            public void info(String tag, String message, Throwable exception) {
                // Suppress info output during tests
            }

            @Override
            public void error(String tag, String message) {
                // Suppress error output during tests
            }

            @Override
            public void error(String tag, String message, Throwable exception) {
                // Suppress error output during tests
            }
        });

        timepiece = new DefaultTimepiece();
        GdxAI.setTimepiece(timepiece);

        telegramPool = new TestPool();
        dispatcher = new MessageDispatcher(telegramPool);

        listener1 = new TestTelegraph("Listener1");
        listener2 = new TestTelegraph("Listener2");
        sender = new TestTelegraph("Sender");
        provider = new TestTelegramProvider();
    }

    @After
    public void tearDown() {
        dispatcher = null;
        timepiece = null;
        GdxAI.setTimepiece(null);
        GdxAI.setLogger(null);
    }

    // Constructor tests
    @Test
    public void testConstructorNoArgs() {
        MessageDispatcher md = new MessageDispatcher();
        Assert.assertNotNull("Dispatcher should be created", md);
        Assert.assertFalse("Debug should be disabled by default", md.isDebugEnabled());
    }

    @Test
    public void testConstructorWithPool() {
        MessageDispatcher md = new MessageDispatcher(telegramPool);
        Assert.assertNotNull("Dispatcher should be created", md);
        Assert.assertFalse("Debug should be disabled by default", md.isDebugEnabled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPool() {
        new MessageDispatcher(null);
    }

    // Debug mode tests
    @Test
    public void testDebugEnabled() {
        Assert.assertFalse("Debug should be disabled by default", dispatcher.isDebugEnabled());

        dispatcher.setDebugEnabled(true);
        Assert.assertTrue("Debug should be enabled", dispatcher.isDebugEnabled());

        dispatcher.setDebugEnabled(false);
        Assert.assertFalse("Debug should be disabled", dispatcher.isDebugEnabled());
    }

    // Listener management tests
    @Test
    public void testAddListener() {
        dispatcher.addListener(listener1, 100);

        // Verify listener was added by sending a message
        dispatcher.dispatchMessage(100);
        Assert.assertEquals("Listener1 should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Message should be correct", 100, listener1.getReceivedMessages().get(0).message);
    }

    @Test
    public void testAddListeners() {
        dispatcher.addListeners(listener1, 100, 200, 300);

        // Verify all messages were registered
        dispatcher.dispatchMessage(100);
        dispatcher.dispatchMessage(200);
        dispatcher.dispatchMessage(300);

        Assert.assertEquals("Listener1 should receive 3 messages", 3, listener1.getReceivedMessages().size());
    }

    @Test
    public void testAddListenerWithProvider() {
        dispatcher.addProvider(provider, 100);
        dispatcher.addListener(listener1, 100);

        // Provider should send message when listener is added
        Assert.assertEquals("Listener1 should receive message from provider", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Message should be from provider", 100, listener1.getReceivedMessages().get(0).message);
        Assert.assertEquals("Extra info should be from provider", "ProviderInfo", listener1.getReceivedMessages().get(0).extraInfo);
    }

    @Test
    public void testRemoveListener() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(100);
        Assert.assertEquals("Listener1 should receive message", 1, listener1.getReceivedMessages().size());

        dispatcher.removeListener(listener1, 100);
        listener1.clearReceivedMessages();
        dispatcher.dispatchMessage(100);
        Assert.assertEquals("Listener1 should not receive message after removal", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testRemoveListeners() {
        dispatcher.addListeners(listener1, 100, 200);
        dispatcher.dispatchMessage(100);
        dispatcher.dispatchMessage(200);
        Assert.assertEquals("Listener1 should receive 2 messages", 2, listener1.getReceivedMessages().size());

        dispatcher.removeListener(listener1, 100, 200);
        listener1.clearReceivedMessages();
        dispatcher.dispatchMessage(100);
        dispatcher.dispatchMessage(200);
        Assert.assertEquals("Listener1 should not receive messages after removal", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClearListeners() {
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener2, 100);
        dispatcher.clearListeners(100);

        dispatcher.dispatchMessage(100);
        Assert.assertEquals("Listener1 should not receive message", 0, listener1.getReceivedMessages().size());
        Assert.assertEquals("Listener2 should not receive message", 0, listener2.getReceivedMessages().size());
    }

    @Test
    public void testClearListenersMultiple() {
        dispatcher.addListeners(listener1, 100, 200);
        dispatcher.clearListeners(100, 200);

        dispatcher.dispatchMessage(100);
        dispatcher.dispatchMessage(200);
        Assert.assertEquals("Listener1 should not receive messages", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClearAllListeners() {
        dispatcher.addListeners(listener1, 100, 200);
        dispatcher.addListener(listener2, 300);
        dispatcher.clearListeners();

        dispatcher.dispatchMessage(100);
        dispatcher.dispatchMessage(200);
        dispatcher.dispatchMessage(300);
        Assert.assertEquals("Listener1 should not receive messages", 0, listener1.getReceivedMessages().size());
        Assert.assertEquals("Listener2 should not receive messages", 0, listener2.getReceivedMessages().size());
    }

    // Provider management tests
    @Test
    public void testAddProvider() {
        dispatcher.addProvider(provider, 100);
        dispatcher.addListener(listener1, 100);

        Assert.assertEquals("Listener1 should receive message from provider", 1, listener1.getReceivedMessages().size());
    }

    @Test
    public void testAddProviders() {
        dispatcher.addProviders(provider, 100, 200);
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener1, 200);

        Assert.assertEquals("Listener1 should receive 2 messages from providers", 2, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClearProviders() {
        dispatcher.addProvider(provider, 100);
        dispatcher.clearProviders(100);
        dispatcher.addListener(listener1, 100);

        Assert.assertEquals("Listener1 should not receive message from cleared provider", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClearProvidersMultiple() {
        dispatcher.addProviders(provider, 100, 200);
        dispatcher.clearProviders(100, 200);
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener1, 200);

        Assert.assertEquals("Listener1 should not receive messages", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClearAllProviders() {
        dispatcher.addProviders(provider, 100, 200);
        dispatcher.clearProviders();
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener1, 200);

        Assert.assertEquals("Listener1 should not receive messages", 0, listener1.getReceivedMessages().size());
    }

    // Message dispatching tests - immediate messages
    @Test
    public void testDispatchMessageJustCode() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(100);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Message should be correct", 100, listener1.getReceivedMessages().get(0).message);
    }

    @Test
    public void testDispatchMessageWithSender() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(sender, 100);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Sender should be correct", sender, listener1.getReceivedMessages().get(0).sender);
    }

    @Test
    public void testDispatchMessageWithSenderAndReturnReceipt() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(sender, 100, true);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Sender should be correct", sender, listener1.getReceivedMessages().get(0).sender);

        // Sender should receive return receipt
        Assert.assertEquals("Sender should receive return receipt", 1, sender.getReceivedMessages().size());
        Assert.assertEquals("Return receipt should have correct message", 100, sender.getReceivedMessages().get(0).message);
        Assert.assertEquals("Return receipt should have dispatcher as sender", dispatcher, sender.getReceivedMessages().get(0).sender);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDispatchMessageWithNullSenderAndReturnReceipt() {
        dispatcher.dispatchMessage(null, 100, true);
    }

    @Test
    public void testDispatchMessageWithExtraInfo() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(100, "ExtraInfo");

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);
    }

    @Test
    public void testDispatchMessageWithSenderAndExtraInfo() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(sender, 100, "ExtraInfo");

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Sender should be correct", sender, listener1.getReceivedMessages().get(0).sender);
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);
    }

    @Test
    public void testDispatchMessageWithSenderExtraInfoAndReturnReceipt() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(sender, 100, "ExtraInfo", true);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);

        // Sender should receive return receipt
        Assert.assertEquals("Sender should receive return receipt", 1, sender.getReceivedMessages().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDispatchMessageWithNullSenderExtraInfoAndReturnReceipt() {
        dispatcher.dispatchMessage(null, 100, "ExtraInfo", true);
    }

    @Test
    public void testDispatchMessageToSpecificReceiver() {
        // Note: receiver doesn't need to be registered
        dispatcher.dispatchMessage(sender, listener1, 100);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Message should be correct", 100, listener1.getReceivedMessages().get(0).message);
        Assert.assertEquals("Sender should be correct", sender, listener1.getReceivedMessages().get(0).sender);
    }

    @Test
    public void testDispatchMessageToSpecificReceiverWithReturnReceipt() {
        dispatcher.dispatchMessage(sender, listener1, 100, true);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());

        // Sender should receive return receipt
        Assert.assertEquals("Sender should receive return receipt", 1, sender.getReceivedMessages().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDispatchMessageToSpecificReceiverWithNullSenderAndReturnReceipt() {
        dispatcher.dispatchMessage(null, listener1, 100, true);
    }

    @Test
    public void testDispatchMessageToSpecificReceiverWithExtraInfo() {
        dispatcher.dispatchMessage(sender, listener1, 100, "ExtraInfo");

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);
    }

    @Test
    public void testDispatchMessageToSpecificReceiverWithAllParameters() {
        dispatcher.dispatchMessage(sender, listener1, 100, "ExtraInfo", true);

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);

        // Sender should receive return receipt
        Assert.assertEquals("Sender should receive return receipt", 1, sender.getReceivedMessages().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDispatchMessageToSpecificReceiverWithNullSenderAndAllParameters() {
        dispatcher.dispatchMessage(null, listener1, 100, "ExtraInfo", true);
    }

    // Broadcast to multiple listeners
    @Test
    public void testBroadcastToMultipleListeners() {
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener2, 100);
        dispatcher.dispatchMessage(100);

        Assert.assertEquals("Listener1 should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Listener2 should receive message", 1, listener2.getReceivedMessages().size());
    }

    @Test
    public void testBroadcastWithNullReceiver() {
        dispatcher.addListener(listener1, 100);
        dispatcher.addListener(listener2, 100);
        dispatcher.dispatchMessage(sender, null, 100);

        Assert.assertEquals("Listener1 should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Listener2 should receive message", 1, listener2.getReceivedMessages().size());
    }

    // Delayed message tests
    @Test
    public void testDelayedMessage() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100);

        // Message should not be delivered immediately
        Assert.assertEquals("Listener should not receive message immediately", 0, listener1.getReceivedMessages().size());

        // Update time to pass delay
        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should receive message after delay", 1, listener1.getReceivedMessages().size());
    }

    @Test
    public void testDelayedMessageWithSender() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, sender, 100);

        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Sender should be correct", sender, listener1.getReceivedMessages().get(0).sender);
    }

    @Test
    public void testDelayedMessageWithExtraInfo() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100, "ExtraInfo");

        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("Extra info should be correct", "ExtraInfo", listener1.getReceivedMessages().get(0).extraInfo);
    }

    @Test
    public void testMultipleDelayedMessages() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100, "Message1");
        dispatcher.dispatchMessage(0.2f, 100, "Message2");
        dispatcher.dispatchMessage(0.05f, 100, "Message3");

        // Update time to pass first delay
        timepiece.update(0.06f);
        dispatcher.update();
        Assert.assertEquals("Listener should receive first message", 1, listener1.getReceivedMessages().size());
        Assert.assertEquals("First message should be Message3", "Message3", listener1.getReceivedMessages().get(0).extraInfo);

        // Update time to pass second delay
        timepiece.update(0.05f);
        dispatcher.update();
        Assert.assertEquals("Listener should receive second message", 2, listener1.getReceivedMessages().size());
        Assert.assertEquals("Second message should be Message1", "Message1", listener1.getReceivedMessages().get(1).extraInfo);

        // Update time to pass third delay
        timepiece.update(0.09f);
        dispatcher.update();
        Assert.assertEquals("Listener should receive third message", 3, listener1.getReceivedMessages().size());
        Assert.assertEquals("Third message should be Message2", "Message2", listener1.getReceivedMessages().get(2).extraInfo);
    }

    @Test
    public void testDelayedMessageWithReturnReceipt() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, sender, 100, true);

        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());

        // Sender should receive return receipt
        Assert.assertEquals("Sender should receive return receipt", 1, sender.getReceivedMessages().size());
    }

    @Test
    public void testDelayedMessageToSpecificReceiver() {
        dispatcher.dispatchMessage(0.1f, sender, listener1, 100);

        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should receive message", 1, listener1.getReceivedMessages().size());
    }

    // Queue management tests
    @Test
    public void testClearQueue() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100, "Message1");
        dispatcher.dispatchMessage(0.2f, 100, "Message2");

        dispatcher.clearQueue();

        // Update time to pass delays
        timepiece.update(0.25f);
        dispatcher.update();

        Assert.assertEquals("Listener should not receive messages after clear", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testClear() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100, "Message1");
        dispatcher.addProvider(provider, 200);

        dispatcher.clear();

        // Update time to pass delay
        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertEquals("Listener should not receive message after clear", 0, listener1.getReceivedMessages().size());

        // Try to add listener - should work since providers are cleared
        dispatcher.addListener(listener1, 200);
        Assert.assertEquals("Listener should not receive provider message after clear", 0, listener1.getReceivedMessages().size());
    }

    // Scan queue tests
    @Test
    public void testScanQueue() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, sender, 100, "ExtraInfo", true);
        dispatcher.dispatchMessage(0.2f, sender, 100, "ExtraInfo2", false);

        TestPendingMessageCallback callback = new TestPendingMessageCallback();
        dispatcher.scanQueue(callback);

        Assert.assertEquals("Should report 2 pending messages", 2, callback.getReportedMessages().size());

        TestPendingMessageCallback.PendingMessage msg1 = callback.getReportedMessages().get(0);
        Assert.assertEquals("First message should have correct delay", 0.1f, msg1.delay, 0.001f);
        Assert.assertEquals("First message should have correct sender", sender, msg1.sender);
        Assert.assertNull("First message should have correct receiver", msg1.receiver); // broadcast
        Assert.assertEquals("First message should have correct message", 100, msg1.message);
        Assert.assertEquals("First message should have correct extra info", "ExtraInfo", msg1.extraInfo);
        Assert.assertEquals("First message should have correct return receipt status", Telegram.RETURN_RECEIPT_NEEDED, msg1.returnReceiptStatus);
    }

    // Telegraph interface tests
    @Test
    public void testHandleMessage() {
        // MessageDispatcher implements Telegraph but always returns false
        Telegram telegram = new Telegram();
        telegram.message = 100;

        Assert.assertFalse("MessageDispatcher should always return false for handleMessage", dispatcher.handleMessage(telegram));
    }

    // Pool usage tests
    @Test
    public void testPoolUsage() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(100);

        Assert.assertTrue("Pool should have been used", telegramPool.wasUsed());
    }

    @Test
    public void testDelayedMessagePoolUsage() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0.1f, 100);

        timepiece.update(0.15f);
        dispatcher.update();

        Assert.assertTrue("Pool should have been used for delayed message", telegramPool.wasUsed());
    }

    // Edge cases and error conditions
    @Test
    public void testMessageToUnregisteredListener() {
        // Listener not registered for this message type
        dispatcher.addListener(listener1, 200);
        dispatcher.dispatchMessage(100);

        Assert.assertEquals("Listener should not receive unregistered message", 0, listener1.getReceivedMessages().size());
    }

    @Test
    public void testMessageWithNoListeners() {
        dispatcher.dispatchMessage(100);
        // Should not throw exception
    }

    @Test
    public void testListenerHandlesMessage() {
        TestTelegraph handlingListener = new TestTelegraph("HandlingListener") {
            @Override
            public boolean handleMessage(Telegram msg) {
                // Call parent to store the message data
                super.handleMessage(msg);
                // Return true to indicate message was handled
                return true;
            }
        };

        dispatcher.addListener(handlingListener, 100);
        dispatcher.dispatchMessage(100);

        Assert.assertEquals("Handling listener should receive message", 1, handlingListener.getReceivedMessages().size());
    }

    @Test
    public void testListenerDoesNotHandleMessage() {
        TestTelegraph nonHandlingListener = new TestTelegraph("NonHandlingListener") {
            @Override
            public boolean handleMessage(Telegram msg) {
                // Call parent to store the message data
                super.handleMessage(msg);
                // Return false to indicate message was not handled
                return false;
            }
        };

        dispatcher.addListener(nonHandlingListener, 100);
        dispatcher.dispatchMessage(100);

        Assert.assertEquals("Non-handling listener should still receive message", 1, nonHandlingListener.getReceivedMessages().size());
    }

    @Test
    public void testZeroDelayMessage() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(0f, 100);

        // Zero delay should be treated as immediate
        Assert.assertEquals("Listener should receive zero-delay message immediately", 1, listener1.getReceivedMessages().size());
    }

    @Test
    public void testNegativeDelayMessage() {
        dispatcher.addListener(listener1, 100);
        dispatcher.dispatchMessage(-0.1f, 100);

        // Negative delay should be treated as immediate
        Assert.assertEquals("Listener should receive negative-delay message immediately", 1, listener1.getReceivedMessages().size());
    }

    // Test helper classes

    // Simple data class to store telegram information (since Telegram objects are pooled and reused)
    private static class TelegramData {
        public final Telegraph sender;
        public final Telegraph receiver;
        public final int message;
        public final Object extraInfo;
        public final int returnReceiptStatus;
        public final float timestamp;

        public TelegramData(Telegram telegram) {
            this.sender = telegram.sender;
            this.receiver = telegram.receiver;
            this.message = telegram.message;
            this.extraInfo = telegram.extraInfo;
            this.returnReceiptStatus = telegram.returnReceiptStatus;
            this.timestamp = telegram.getTimestamp();
        }
    }

    private static class TestTelegraph implements Telegraph {
        private final String name;
        private final List<TelegramData> receivedMessages = new ArrayList<>();

        public TestTelegraph(String name) {
            this.name = name;
        }

        @Override
        public boolean handleMessage(Telegram msg) {
            // Create a copy of the telegram data since the Telegram object will be pooled and reused
            TelegramData data = new TelegramData(msg);
            receivedMessages.add(data);
            return true;
        }

        public List<TelegramData> getReceivedMessages() {
            return receivedMessages;
        }

        public void clearReceivedMessages() {
            receivedMessages.clear();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestTelegramProvider implements TelegramProvider {
        @Override
        public Object provideMessageInfo(int msg, Telegraph receiver) {
            if (msg == 100) {
                return "ProviderInfo";
            }
            if (msg == 200) {
                return "ProviderInfo200";
            }
            return null;
        }
    }

    private static class TestPool extends Pool<Telegram> {
        private boolean used = false;

        public TestPool() {
            super(16);
        }

        @Override
        protected Telegram newObject() {
            return new Telegram();
        }

        @Override
        public Telegram obtain() {
            used = true;
            return super.obtain();
        }

        public boolean wasUsed() {
            return used;
        }
    }

    private static class TestPendingMessageCallback implements MessageDispatcher.PendingMessageCallback {
        private final List<PendingMessage> reportedMessages = new ArrayList<>();

        @Override
        public void report(float delay, Telegraph sender, Telegraph receiver, int message, Object extraInfo, int returnReceiptStatus) {
            reportedMessages.add(new PendingMessage(delay, sender, receiver, message, extraInfo, returnReceiptStatus));
        }

        public List<PendingMessage> getReportedMessages() {
            return reportedMessages;
        }

        public static class PendingMessage {
            public final float delay;
            public final Telegraph sender;
            public final Telegraph receiver;
            public final int message;
            public final Object extraInfo;
            public final int returnReceiptStatus;

            public PendingMessage(float delay, Telegraph sender, Telegraph receiver, int message, Object extraInfo, int returnReceiptStatus) {
                this.delay = delay;
                this.sender = sender;
                this.receiver = receiver;
                this.message = message;
                this.extraInfo = extraInfo;
                this.returnReceiptStatus = returnReceiptStatus;
            }
        }
    }
}
