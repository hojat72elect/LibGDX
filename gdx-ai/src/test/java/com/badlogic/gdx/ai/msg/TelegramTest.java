package com.badlogic.gdx.ai.msg;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TelegramTest {

    private TestTelegraph testSender;
    private TestTelegraph testReceiver;
    private TestTelegraph testSender2;
    private TestTelegraph testReceiver2;

    @Before
    public void setUp() {
        testSender = new TestTelegraph("sender1");
        testReceiver = new TestTelegraph("receiver1");
        testSender2 = new TestTelegraph("sender2");
        testReceiver2 = new TestTelegraph("receiver2");
    }

    @After
    public void tearDown() {
        testSender = null;
        testReceiver = null;
        testSender2 = null;
        testReceiver2 = null;
    }

    @Test
    public void testConstructor() {
        Telegram telegram = new Telegram();

        Assert.assertNull("Sender should be null after construction", telegram.sender);
        Assert.assertNull("Receiver should be null after construction", telegram.receiver);
        Assert.assertEquals("Message should be 0 after construction", 0, telegram.message);
        Assert.assertEquals("Return receipt status should be RETURN_RECEIPT_UNNEEDED",
                Telegram.RETURN_RECEIPT_UNNEEDED, telegram.returnReceiptStatus);
        Assert.assertNull("Extra info should be null after construction", telegram.extraInfo);
        Assert.assertEquals("Timestamp should be 0 after construction", 0.0f, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testConstants() {
        Assert.assertEquals("RETURN_RECEIPT_UNNEEDED should be 0", 0, Telegram.RETURN_RECEIPT_UNNEEDED);
        Assert.assertEquals("RETURN_RECEIPT_NEEDED should be 1", 1, Telegram.RETURN_RECEIPT_NEEDED);
        Assert.assertEquals("RETURN_RECEIPT_SENT should be 2", 2, Telegram.RETURN_RECEIPT_SENT);
    }

    @Test
    public void testGetTimestamp() {
        Telegram telegram = new Telegram();

        // Test initial timestamp
        Assert.assertEquals("Initial timestamp should be 0", 0.0f, telegram.getTimestamp(), 0.001f);

        // Test after setting timestamp
        telegram.setTimestamp(123.45f);
        Assert.assertEquals("Timestamp should be 123.45", 123.45f, telegram.getTimestamp(), 0.001f);

        // Test negative timestamp
        telegram.setTimestamp(-10.5f);
        Assert.assertEquals("Timestamp should be -10.5", -10.5f, telegram.getTimestamp(), 0.001f);

        // Test zero timestamp
        telegram.setTimestamp(0.0f);
        Assert.assertEquals("Timestamp should be 0", 0.0f, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testSetTimestamp() {
        Telegram telegram = new Telegram();

        // Test setting various timestamp values
        telegram.setTimestamp(100.0f);
        Assert.assertEquals("Timestamp should be 100.0", 100.0f, telegram.getTimestamp(), 0.001f);

        telegram.setTimestamp(Float.MAX_VALUE);
        Assert.assertEquals("Timestamp should be Float.MAX_VALUE", Float.MAX_VALUE, telegram.getTimestamp(), 0.001f);

        telegram.setTimestamp(Float.MIN_VALUE);
        Assert.assertEquals("Timestamp should be Float.MIN_VALUE", Float.MIN_VALUE, telegram.getTimestamp(), 0.001f);

        telegram.setTimestamp(Float.POSITIVE_INFINITY);
        Assert.assertEquals("Timestamp should be POSITIVE_INFINITY", Float.POSITIVE_INFINITY, telegram.getTimestamp(), 0.001f);

        telegram.setTimestamp(Float.NEGATIVE_INFINITY);
        Assert.assertEquals("Timestamp should be NEGATIVE_INFINITY", Float.NEGATIVE_INFINITY, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testReset() {
        Telegram telegram = new Telegram();

        // Set all fields to non-default values
        telegram.sender = testSender;
        telegram.receiver = testReceiver;
        telegram.message = 42;
        telegram.returnReceiptStatus = Telegram.RETURN_RECEIPT_NEEDED;
        telegram.extraInfo = "test info";
        telegram.setTimestamp(123.45f);

        // Verify fields are set
        Assert.assertEquals("Sender should be testSender", testSender, telegram.sender);
        Assert.assertEquals("Receiver should be testReceiver", testReceiver, telegram.receiver);
        Assert.assertEquals("Message should be 42", 42, telegram.message);
        Assert.assertEquals("Return receipt status should be RETURN_RECEIPT_NEEDED",
                Telegram.RETURN_RECEIPT_NEEDED, telegram.returnReceiptStatus);
        Assert.assertEquals("Extra info should be 'test info'", "test info", telegram.extraInfo);
        Assert.assertEquals("Timestamp should be 123.45", 123.45f, telegram.getTimestamp(), 0.001f);

        // Reset the telegram
        telegram.reset();

        // Verify all fields are reset to defaults
        Assert.assertNull("Sender should be null after reset", telegram.sender);
        Assert.assertNull("Receiver should be null after reset", telegram.receiver);
        Assert.assertEquals("Message should be 0 after reset", 0, telegram.message);
        Assert.assertEquals("Return receipt status should be RETURN_RECEIPT_UNNEEDED",
                Telegram.RETURN_RECEIPT_UNNEEDED, telegram.returnReceiptStatus);
        Assert.assertNull("Extra info should be null after reset", telegram.extraInfo);
        Assert.assertEquals("Timestamp should be 0 after reset", 0.0f, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testResetMultipleTimes() {
        Telegram telegram = new Telegram();

        // Set fields and reset multiple times
        telegram.sender = testSender;
        telegram.message = 100;
        telegram.setTimestamp(50.0f);

        telegram.reset();
        telegram.reset(); // Reset again

        // Should still be in default state
        Assert.assertNull("Sender should be null after multiple resets", telegram.sender);
        Assert.assertEquals("Message should be 0 after multiple resets", 0, telegram.message);
        Assert.assertEquals("Timestamp should be 0 after multiple resets", 0.0f, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testCompareTo() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();
        Telegram telegram3 = new Telegram();

        // Set different timestamps
        telegram1.setTimestamp(10.0f);
        telegram2.setTimestamp(20.0f);
        telegram3.setTimestamp(10.0f);

        // Test comparison based on timestamp
        Assert.assertTrue("telegram1 should be less than telegram2", telegram1.compareTo(telegram2) < 0);
        Assert.assertTrue("telegram2 should be greater than telegram1", telegram2.compareTo(telegram1) > 0);

        // Test equal timestamps (but different objects)
        // Make them different by setting different senders so they're not equal
        telegram1.sender = testSender;
        telegram3.sender = testSender2; // Different sender

        int result = telegram1.compareTo(telegram3);
        // Since they have the same timestamp but are different objects, compareTo will return 1
        // because (10.0f - 10.0f < 0) is false, so it returns 1
        Assert.assertEquals("telegrams with same timestamp but different objects should return 1", 1, result);

        // Test same object
        Assert.assertEquals("telegram should be equal to itself", 0, telegram1.compareTo(telegram1));

        // Test negative timestamps
        telegram1.setTimestamp(-10.0f);
        telegram2.setTimestamp(-5.0f);
        Assert.assertTrue("telegram1 should be less than telegram2", telegram1.compareTo(telegram2) < 0);

        // Test zero timestamps
        telegram1.setTimestamp(0.0f);
        telegram2.setTimestamp(0.0f);
        Assert.assertTrue("telegrams with same zero timestamp should not be equal (unless same object)",
                telegram1.compareTo(telegram2) != 0);
    }

    @Test
    public void testCompareToWithEqualObjects() {
        Telegram telegram = new Telegram();
        telegram.setTimestamp(100.0f);

        // Same object should return 0
        Assert.assertEquals("Same object should compare as equal", 0, telegram.compareTo(telegram));
    }

    @Test
    public void testHashCode() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Set same values
        telegram1.sender = testSender;
        telegram1.receiver = testReceiver;
        telegram1.message = 42;
        telegram1.setTimestamp(123.45f);

        telegram2.sender = testSender;
        telegram2.receiver = testReceiver;
        telegram2.message = 42;
        telegram2.setTimestamp(123.45f);

        // Equal objects should have same hash code
        Assert.assertEquals("Equal telegrams should have same hash code", telegram1.hashCode(), telegram2.hashCode());

        // Different message should have different hash code
        telegram2.message = 43;
        Assert.assertNotEquals("Telegrams with different messages should have different hash codes",
                telegram1.hashCode(), telegram2.hashCode());
        telegram2.message = 42;

        // Different sender should have different hash code
        telegram2.sender = testSender2;
        Assert.assertNotEquals("Telegrams with different senders should have different hash codes",
                telegram1.hashCode(), telegram2.hashCode());
        telegram2.sender = testSender;

        // Different receiver should have different hash code
        telegram2.receiver = testReceiver2;
        Assert.assertNotEquals("Telegrams with different receivers should have different hash codes",
                telegram1.hashCode(), telegram2.hashCode());
        telegram2.receiver = testReceiver;

        // Different timestamp should have different hash code
        telegram2.setTimestamp(543.21f);
        Assert.assertNotEquals("Telegrams with different timestamps should have different hash codes",
                telegram1.hashCode(), telegram2.hashCode());
    }

    @Test
    public void testHashCodeWithNulls() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Both telegrams have null fields (default state)
        Assert.assertEquals("Telegrams with null fields should have same hash code", telegram1.hashCode(), telegram2.hashCode());

        // One with null sender, one with non-null sender
        telegram1.sender = testSender;
        Assert.assertNotEquals("Telegram with non-null sender should have different hash code",
                telegram1.hashCode(), telegram2.hashCode());

        // Reset and test with null receiver
        telegram1.reset();
        telegram1.receiver = testReceiver;
        Assert.assertNotEquals("Telegram with non-null receiver should have different hash code",
                telegram1.hashCode(), telegram2.hashCode());
    }

    @Test
    public void testEquals() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Test same object
        Assert.assertTrue("Telegram should be equal to itself", telegram1.equals(telegram1));

        // Test equal objects with default values
        Assert.assertTrue("Telegrams with default values should be equal", telegram1.equals(telegram2));

        // Set same values
        telegram1.sender = testSender;
        telegram1.receiver = testReceiver;
        telegram1.message = 42;
        telegram1.setTimestamp(123.45f);

        telegram2.sender = testSender;
        telegram2.receiver = testReceiver;
        telegram2.message = 42;
        telegram2.setTimestamp(123.45f);

        Assert.assertTrue("Telegrams with same values should be equal", telegram1.equals(telegram2));
        Assert.assertTrue("Equals should be symmetric", telegram2.equals(telegram1));
    }

    @Test
    public void testEqualsWithNulls() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Both have null fields - should be equal
        Assert.assertTrue("Telegrams with null fields should be equal", telegram1.equals(telegram2));

        // One with null sender, one with non-null sender
        telegram1.sender = testSender;
        Assert.assertFalse("Telegram with non-null sender should not equal telegram with null sender",
                telegram1.equals(telegram2));

        // Reset and test with null receiver
        telegram1.reset();
        telegram1.receiver = testReceiver;
        Assert.assertFalse("Telegram with non-null receiver should not equal telegram with null receiver",
                telegram1.equals(telegram2));
    }

    @Test
    public void testNotEquals() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Set base values
        telegram1.sender = testSender;
        telegram1.receiver = testReceiver;
        telegram1.message = 42;
        telegram1.setTimestamp(123.45f);

        telegram2.sender = testSender;
        telegram2.receiver = testReceiver;
        telegram2.message = 42;
        telegram2.setTimestamp(123.45f);

        // Test different sender
        telegram2.sender = testSender2;
        Assert.assertFalse("Telegrams with different senders should not be equal", telegram1.equals(telegram2));
        telegram2.sender = testSender;

        // Test different receiver
        telegram2.receiver = testReceiver2;
        Assert.assertFalse("Telegrams with different receivers should not be equal", telegram1.equals(telegram2));
        telegram2.receiver = testReceiver;

        // Test different message
        telegram2.message = 43;
        Assert.assertFalse("Telegrams with different messages should not be equal", telegram1.equals(telegram2));
        telegram2.message = 42;

        // Test different timestamp
        telegram2.setTimestamp(543.21f);
        Assert.assertFalse("Telegrams with different timestamps should not be equal", telegram1.equals(telegram2));
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        Telegram telegram = new Telegram();

        // Test equality with null
        Assert.assertFalse("Telegram should not be equal to null", telegram.equals(null));

        // Test equality with different class
        Assert.assertFalse("Telegram should not be equal to string", telegram.equals("not a telegram"));
        Assert.assertFalse("Telegram should not be equal to integer", telegram.equals(42));
    }

    @Test
    public void testEqualsWithFloatPrecision() {
        Telegram telegram1 = new Telegram();
        Telegram telegram2 = new Telegram();

        // Test with very close timestamps
        telegram1.setTimestamp(1.234567f);
        telegram2.setTimestamp(1.234568f);

        Assert.assertFalse("Telegrams with slightly different timestamps should not be equal",
                telegram1.equals(telegram2));

        // Test with exactly same timestamps
        telegram2.setTimestamp(1.234567f);
        Assert.assertTrue("Telegrams with exactly same timestamps should be equal",
                telegram1.equals(telegram2));
    }

    @Test
    public void testFieldAccess() {
        Telegram telegram = new Telegram();

        // Test direct field access (since fields are public)
        telegram.sender = testSender;
        telegram.receiver = testReceiver;
        telegram.message = 100;
        telegram.returnReceiptStatus = Telegram.RETURN_RECEIPT_NEEDED;
        telegram.extraInfo = "test data";

        Assert.assertEquals("Sender field should be accessible", testSender, telegram.sender);
        Assert.assertEquals("Receiver field should be accessible", testReceiver, telegram.receiver);
        Assert.assertEquals("Message field should be accessible", 100, telegram.message);
        Assert.assertEquals("Return receipt status field should be accessible",
                Telegram.RETURN_RECEIPT_NEEDED, telegram.returnReceiptStatus);
        Assert.assertEquals("Extra info field should be accessible", "test data", telegram.extraInfo);

        // Test that returnReceiptStatus can be set to any valid constant
        telegram.returnReceiptStatus = Telegram.RETURN_RECEIPT_SENT;
        Assert.assertEquals("Return receipt status should be RETURN_RECEIPT_SENT",
                Telegram.RETURN_RECEIPT_SENT, telegram.returnReceiptStatus);
    }

    @Test
    public void testPoolableInterface() {
        // Telegram implements Poolable, so it should have a reset() method
        Telegram telegram = new Telegram();

        // Verify it's properly implementing Poolable
        Assert.assertTrue("Telegram should implement Poolable", telegram instanceof com.badlogic.gdx.utils.Pool.Poolable);

        // Test that reset() works as expected for pooling
        telegram.sender = testSender;
        telegram.receiver = testReceiver;
        telegram.message = 999;
        telegram.returnReceiptStatus = Telegram.RETURN_RECEIPT_SENT;
        telegram.extraInfo = new Object();
        telegram.setTimestamp(999.9f);

        // Reset should prepare it for reuse in pool
        telegram.reset();

        Assert.assertNull("Sender should be null for pool reuse", telegram.sender);
        Assert.assertNull("Receiver should be null for pool reuse", telegram.receiver);
        Assert.assertEquals("Message should be 0 for pool reuse", 0, telegram.message);
        Assert.assertEquals("Return receipt status should be RETURN_RECEIPT_UNNEEDED for pool reuse",
                Telegram.RETURN_RECEIPT_UNNEEDED, telegram.returnReceiptStatus);
        Assert.assertNull("Extra info should be null for pool reuse", telegram.extraInfo);
        Assert.assertEquals("Timestamp should be 0 for pool reuse", 0.0f, telegram.getTimestamp(), 0.001f);
    }

    @Test
    public void testComparableInterface() {
        // Telegram implements Comparable, so it should have a compareTo() method
        Telegram telegram = new Telegram();

        // Verify it's properly implementing Comparable
        Assert.assertTrue("Telegram should implement Comparable", telegram instanceof Comparable);

        // Test that compareTo works with Comparable interface
        Comparable<Telegram> comparable = telegram;
        telegram.setTimestamp(100.0f);

        Telegram other = new Telegram();
        other.setTimestamp(200.0f);

        Assert.assertTrue("compareTo should work through Comparable interface", comparable.compareTo(other) < 0);
    }

    /**
     * Test implementation of Telegraph for testing purposes.
     */
    private static class TestTelegraph implements Telegraph {
        private final String name;

        public TestTelegraph(String name) {
            this.name = name;
        }

        @Override
        public boolean handleMessage(Telegram msg) {
            return true; // Always handle messages for testing
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestTelegraph that = (TestTelegraph) obj;
            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "TestTelegraph{" + name + "}";
        }
    }
}
