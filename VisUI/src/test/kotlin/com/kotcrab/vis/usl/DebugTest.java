package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DebugTest {

    @Test
    public void testDebugOutput() {
        ArrayList<StyleBlock> styleBlocks = new ArrayList<>();

        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "testStyle";
        BasicIdentifier basic = new BasicIdentifier("color", "red");
        style.content.add(basic);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        assertNotNull("JSON should not be null", json);
        assertTrue("JSON should contain block name", json.contains("testBlock"));
        assertTrue("JSON should contain style name", json.contains("testStyle"));
        assertTrue("JSON should contain identifier", json.contains("color"));
        assertTrue("JSON should contain value", json.contains("red"));

        System.out.println("JSON output: " + json);
    }
}
