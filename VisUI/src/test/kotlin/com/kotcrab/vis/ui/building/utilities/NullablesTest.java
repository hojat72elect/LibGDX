package com.kotcrab.vis.ui.building.utilities;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class NullablesTest {
    @Test
    public void testIsNull() {
        Assert.assertTrue(Nullables.isNull(null));
        Assert.assertFalse(Nullables.isNull(new Object()));
    }

    @Test
    public void testIsNotNull() {
        Assert.assertFalse(Nullables.isNotNull(null));
        Assert.assertTrue(Nullables.isNotNull(new Object()));
    }

    @Test
    public void testGetOrElse() {
        Object obj = new Object();
        Object alternative = new Object();
        Assert.assertEquals(obj, Nullables.getOrElse(obj, alternative));
        Assert.assertEquals(alternative, Nullables.getOrElse(null, alternative));
    }

    @Test
    public void testExecuteIfNotNull() {
        final AtomicBoolean executed = new AtomicBoolean(false);
        Runnable command = new Runnable() {
            @Override
            public void run() {
                executed.set(true);
            }
        };

        Nullables.executeIfNotNull(null, command);
        Assert.assertFalse(executed.get());

        Nullables.executeIfNotNull(new Object(), command);
        Assert.assertTrue(executed.get());
    }

    @Test
    public void testAreEqual() {
        Assert.assertTrue(Nullables.areEqual(null, null));
        Assert.assertFalse(Nullables.areEqual(new Object(), null));
        Assert.assertFalse(Nullables.areEqual(null, new Object()));

        String s1 = "test";
        String s2 = "test";
        Assert.assertTrue(Nullables.areEqual(s1, s2));
    }

    @Test
    public void testIsAnyNull() {
        Assert.assertTrue(Nullables.isAnyNull(null, new Object()));
        Assert.assertTrue(Nullables.isAnyNull(new Object(), null));
        Assert.assertTrue(Nullables.isAnyNull(null, null));
        Assert.assertFalse(Nullables.isAnyNull(new Object(), new Object()));
    }

    @Test
    public void testAreAllNull() {
        Assert.assertTrue(Nullables.areAllNull(null, null));
        Assert.assertFalse(Nullables.areAllNull(null, new Object()));
        Assert.assertFalse(Nullables.areAllNull(new Object(), new Object()));
    }

    @Test
    public void testIsAnyNotNull() {
        Assert.assertTrue(Nullables.isAnyNotNull(null, new Object()));
        Assert.assertTrue(Nullables.isAnyNotNull(new Object(), new Object()));
        Assert.assertFalse(Nullables.isAnyNotNull(null, null));
    }

    @Test
    public void testAreAllNotNull() {
        Assert.assertTrue(Nullables.areAllNotNull(new Object(), new Object()));
        Assert.assertFalse(Nullables.areAllNotNull(null, new Object()));
        Assert.assertFalse(Nullables.areAllNotNull(null, null));
    }

    @Test
    public void testGetOrElseWithNullAlternative() {
        Object obj = new Object();
        Assert.assertEquals(obj, Nullables.getOrElse(obj, null));
        Assert.assertNull(Nullables.getOrElse(null, null));
    }

    @Test
    public void testAreEqualWithDifferentTypes() {
        String str = "test";
        Integer num = 42;
        Assert.assertFalse(Nullables.areEqual(str, num));
        Assert.assertFalse(Nullables.areEqual(num, str));
    }

    @Test
    public void testAreEqualWithSameReference() {
        Object obj = new Object();
        Assert.assertTrue(Nullables.areEqual(obj, obj));
    }

    @Test
    public void testIsAnyNullWithEmptyArray() {
        Assert.assertFalse(Nullables.isAnyNull());
    }

    @Test
    public void testIsAnyNullWithSingleElement() {
        Assert.assertTrue(Nullables.isAnyNull((Object) null));
        Assert.assertFalse(Nullables.isAnyNull(new Object()));
    }

    @Test
    public void testIsAnyNullWithMultipleElements() {
        Assert.assertTrue(Nullables.isAnyNull(new Object(), new Object(), null));
        Assert.assertTrue(Nullables.isAnyNull(null, new Object(), new Object()));
        Assert.assertFalse(Nullables.isAnyNull(new Object(), new Object(), new Object()));
    }

    @Test
    public void testAreAllNullWithEmptyArray() {
        Assert.assertTrue(Nullables.areAllNull());
    }

    @Test
    public void testAreAllNullWithSingleElement() {
        Assert.assertTrue(Nullables.areAllNull((Object) null));
        Assert.assertFalse(Nullables.areAllNull(new Object()));
    }

    @Test
    public void testAreAllNullWithMultipleElements() {
        Assert.assertTrue(Nullables.areAllNull(null, null, null));
        Assert.assertFalse(Nullables.areAllNull(null, null, new Object()));
        Assert.assertFalse(Nullables.areAllNull(new Object(), new Object(), new Object()));
    }

    @Test
    public void testIsAnyNotNullWithEmptyArray() {
        Assert.assertFalse(Nullables.isAnyNotNull());
    }

    @Test
    public void testIsAnyNotNullWithSingleElement() {
        Assert.assertFalse(Nullables.isAnyNotNull((Object) null));
        Assert.assertTrue(Nullables.isAnyNotNull(new Object()));
    }

    @Test
    public void testIsAnyNotNullWithMultipleElements() {
        Assert.assertTrue(Nullables.isAnyNotNull(null, null, new Object()));
        Assert.assertTrue(Nullables.isAnyNotNull(new Object(), null, null));
        Assert.assertFalse(Nullables.isAnyNotNull(null, null, null));
    }

    @Test
    public void testAreAllNotNullWithEmptyArray() {
        Assert.assertTrue(Nullables.areAllNotNull());
    }

    @Test
    public void testAreAllNotNullWithSingleElement() {
        Assert.assertFalse(Nullables.areAllNotNull((Object) null));
        Assert.assertTrue(Nullables.areAllNotNull(new Object()));
    }

    @Test
    public void testAreAllNotNullWithMultipleElements() {
        Assert.assertTrue(Nullables.areAllNotNull(new Object(), new Object(), new Object()));
        Assert.assertFalse(Nullables.areAllNotNull(new Object(), new Object(), null));
        Assert.assertFalse(Nullables.areAllNotNull(null, null, null));
    }
}
