package com.crashinvaders.vfx.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommonUtilsTest {

    @Test
    public void testRandom() {
        Integer[] array = { 1, 2, 3, 4, 5 };
        Integer result = CommonUtils.random(array);
        assertNotNull(result);
        boolean found = false;
        for (Integer i : array) {
            if (i.equals(result)) {
                found = true;
                break;
            }
        }
        assertTrue("Result should be one of the array elements", found);
    }

    @Test
    public void testCompareComparable() {
        assertEquals(0, CommonUtils.compare(10, 10, true));
        assertEquals(0, CommonUtils.compare((Integer) null, null, true));

        // nullGreater = true
        assertTrue(CommonUtils.compare(null, 10, true) > 0);
        assertTrue(CommonUtils.compare(10, null, true) < 0);

        // nullGreater = false
        assertTrue(CommonUtils.compare(null, 10, false) < 0);
        assertTrue(CommonUtils.compare(10, null, false) > 0);

        assertTrue(CommonUtils.compare(5, 10, false) < 0);
        assertTrue(CommonUtils.compare(10, 5, false) > 0);
    }

    @Test
    public void testCompareInt() {
        assertEquals(-1, CommonUtils.compare(5, 10));
        assertEquals(0, CommonUtils.compare(10, 10));
        assertEquals(1, CommonUtils.compare(15, 10));
    }

    @Test
    public void testCompareFloat() {
        assertEquals(-1, CommonUtils.compare(5.5f, 10.5f));
        assertEquals(0, CommonUtils.compare(10.5f, 10.5f));
        assertEquals(1, CommonUtils.compare(15.5f, 10.5f));
    }

    @Test
    public void testCompareBoolean() {
        assertEquals(0, CommonUtils.compare(true, true));
        assertEquals(0, CommonUtils.compare(false, false));
        assertEquals(1, CommonUtils.compare(true, false));
        assertEquals(-1, CommonUtils.compare(false, true));
    }

    @Test
    public void testGetAlignFactorX() {
        assertEquals(0.0f, CommonUtils.getAlignFactorX(Align.left), 0.001f);
        assertEquals(1.0f, CommonUtils.getAlignFactorX(Align.right), 0.001f);
        assertEquals(0.5f, CommonUtils.getAlignFactorX(Align.center), 0.001f);
        assertEquals(0.5f, CommonUtils.getAlignFactorX(Align.top), 0.001f); // No X alignment info
    }

    @Test
    public void testGetAlignFactorY() {
        assertEquals(0.0f, CommonUtils.getAlignFactorY(Align.bottom), 0.001f);
        assertEquals(1.0f, CommonUtils.getAlignFactorY(Align.top), 0.001f);
        assertEquals(0.5f, CommonUtils.getAlignFactorY(Align.center), 0.001f);
        assertEquals(0.5f, CommonUtils.getAlignFactorY(Align.left), 0.001f); // No Y alignment info
    }

    @Test
    public void testStringEquals() {
        assertTrue(CommonUtils.stringEquals(null, null));
        assertTrue(CommonUtils.stringEquals("abc", "abc"));
        assertFalse(CommonUtils.stringEquals("abc", "def"));
        assertFalse(CommonUtils.stringEquals("abc", null));
        assertFalse(CommonUtils.stringEquals(null, "abc"));
    }

    @Test
    public void testParseHexColor() {
        // Since it uses a static tmpColor, we just check the values immediately

        // 3 digits
        Color c3 = CommonUtils.parseHexColor("f00");
        assertEquals(1f, c3.r, 0.001f);
        assertEquals(0f, c3.g, 0.001f);
        assertEquals(0f, c3.b, 0.001f);
        assertEquals(1f, c3.a, 0.001f);

        // 4 digits
        Color c4 = CommonUtils.parseHexColor("0f08");
        assertEquals(0f, c4.r, 0.001f);
        assertEquals(1f, c4.g, 0.001f);
        assertEquals(0f, c4.b, 0.001f);
        assertEquals(8f / 15f, c4.a, 0.001f);

        // 6 digits
        Color c6 = CommonUtils.parseHexColor("0000ff");
        assertEquals(0f, c6.r, 0.001f);
        assertEquals(0f, c6.g, 0.001f);
        assertEquals(1f, c6.b, 0.001f);
        assertEquals(1f, c6.a, 0.001f);

        // 8 digits
        Color c8 = CommonUtils.parseHexColor("ffffff80");
        assertEquals(1f, c8.r, 0.001f);
        assertEquals(1f, c8.g, 0.001f);
        assertEquals(1f, c8.b, 0.001f);
        assertEquals(128f / 255f, c8.a, 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHexColorInvalidLength() {
        CommonUtils.parseHexColor("ff");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHexColor3InvalidLength() {
        CommonUtils.parseHexColor3("f");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHexColor4InvalidLength() {
        CommonUtils.parseHexColor4("ff");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHexColor6InvalidLength() {
        CommonUtils.parseHexColor6("fff");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHexColor8InvalidLength() {
        CommonUtils.parseHexColor8("fffff");
    }
}
