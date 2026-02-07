package com.kotcrab.vis.ui.widget;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Unit tests for {@link BusyBar}.
 */
public class BusyBarTest {

    @Test
    public void testPrefSizeUsesStyleValues() {
        BusyBar.BusyBarStyle style = new BusyBar.BusyBarStyle();
        style.segmentOverflow = 3;
        style.segmentWidth = 12;
        style.height = 7;

        BusyBar bar = new BusyBar(style);

        Assert.assertEquals(7f, bar.getPrefHeight(), 0.0001f);
        Assert.assertEquals(12f, bar.getPrefWidth(), 0.0001f);
        Assert.assertSame(style, bar.getStyle());
    }

    @Test
    public void testResetSegmentSetsExpectedPosition() throws Exception {
        BusyBar.BusyBarStyle style = new BusyBar.BusyBarStyle();
        style.segmentOverflow = 5;
        style.segmentWidth = 20;
        style.height = 4;

        BusyBar bar = new BusyBar(style);
        bar.resetSegment();

        Field segmentX = BusyBar.class.getDeclaredField("segmentX");
        segmentX.setAccessible(true);
        float value = (float) segmentX.get(bar);

        Assert.assertEquals(-25f, value, 0.0001f);
    }
}
