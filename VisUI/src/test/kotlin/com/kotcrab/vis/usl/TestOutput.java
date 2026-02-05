package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;

import java.util.ArrayList;

public class TestOutput {
    public static void main(String[] args) {
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

        System.out.println("JSON output:");
        System.out.println("[" + json + "]");
        System.out.println("End of output");
    }
}
