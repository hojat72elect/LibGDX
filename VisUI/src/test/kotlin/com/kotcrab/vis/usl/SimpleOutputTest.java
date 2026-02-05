package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.AliasIdentifier;
import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.GroupIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SimpleOutputTest {

    @Test
    public void testFailingCases() throws IOException {
        ArrayList<StyleBlock> styleBlocks = new ArrayList<>();

        // Test 1: Group identifier
        styleBlocks.clear();
        StyleBlock block1 = new StyleBlock();
        block1.fullName = "testBlock";
        StyleIdentifier style1 = new StyleIdentifier();
        style1.name = "groupStyle";
        GroupIdentifier group = new GroupIdentifier();
        group.name = "group";
        BasicIdentifier basic1 = new BasicIdentifier("color", "red");
        BasicIdentifier basic2 = new BasicIdentifier("font", "Arial");
        group.content.add(basic1);
        group.content.add(basic2);
        style1.content.add(group);
        block1.styles.add(style1);
        styleBlocks.add(block1);

        USLJsonWriter writer1 = new USLJsonWriter(styleBlocks);
        String json1 = writer1.getJson();

        // Test 2: Nested group identifier
        styleBlocks.clear();
        StyleBlock block2 = new StyleBlock();
        block2.fullName = "testBlock";
        StyleIdentifier style2 = new StyleIdentifier();
        style2.name = "nestedStyle";
        GroupIdentifier outerGroup = new GroupIdentifier();
        outerGroup.name = "outer";
        GroupIdentifier innerGroup = new GroupIdentifier();
        innerGroup.name = "inner";
        BasicIdentifier basic3 = new BasicIdentifier("color", "blue");
        innerGroup.content.add(basic3);
        outerGroup.content.add(innerGroup);
        style2.content.add(outerGroup);
        block2.styles.add(style2);
        styleBlocks.add(block2);

        USLJsonWriter writer2 = new USLJsonWriter(styleBlocks);
        String json2 = writer2.getJson();

        // Test 3: Mixed identifiers
        styleBlocks.clear();
        StyleBlock block3 = new StyleBlock();
        block3.fullName = "testBlock";
        StyleIdentifier style3 = new StyleIdentifier();
        style3.name = "mixedStyle";
        BasicIdentifier basic4 = new BasicIdentifier("color", "red");
        GroupIdentifier group2 = new GroupIdentifier();
        group2.name = "group";
        BasicIdentifier basic5 = new BasicIdentifier("font", "Arial");
        group2.content.add(basic5);
        AliasIdentifier alias = new AliasIdentifier("alias");
        style3.content.add(basic4);
        style3.content.add(group2);
        style3.content.add(alias);
        block3.styles.add(style3);
        styleBlocks.add(block3);

        USLJsonWriter writer3 = new USLJsonWriter(styleBlocks);
        String json3 = writer3.getJson();

        // Test 4: Meta styles
        styleBlocks.clear();
        StyleBlock block4 = new StyleBlock();
        block4.fullName = "testBlock";
        StyleIdentifier normalStyle = new StyleIdentifier();
        normalStyle.name = "normalStyle";
        BasicIdentifier basic6 = new BasicIdentifier("color", "red");
        normalStyle.content.add(basic6);
        StyleIdentifier metaStyle = new StyleIdentifier();
        metaStyle.name = "metaStyle";
        metaStyle.metaStyle = true;
        BasicIdentifier basic7 = new BasicIdentifier("font", "Arial");
        metaStyle.content.add(basic7);
        block4.styles.add(normalStyle);
        block4.styles.add(metaStyle);
        styleBlocks.add(block4);

        USLJsonWriter writer4 = new USLJsonWriter(styleBlocks);
        String json4 = writer4.getJson();

        // Write to a file in temp directory
        File outputFile = new File("D:/temp/usl_failing_cases.txt");
        outputFile.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(outputFile)) {
            fw.write("=== Test 1: Group identifier ===\n");
            fw.write("JSON: [" + json1 + "]\n");
            fw.write("Length: " + json1.length() + "\n");
            fw.write("Char by char:\n");
            for (int i = 0; i < json1.length(); i++) {
                char c = json1.charAt(i);
                fw.write(i + ": '" + c + "' (" + (int) c + ")\n");
            }
            fw.write("\n");

            fw.write("=== Test 2: Nested group identifier ===\n");
            fw.write("JSON: [" + json2 + "]\n");
            fw.write("Length: " + json2.length() + "\n");
            fw.write("Char by char:\n");
            for (int i = 0; i < json2.length(); i++) {
                char c = json2.charAt(i);
                fw.write(i + ": '" + c + "' (" + (int) c + ")\n");
            }
            fw.write("\n");

            fw.write("=== Test 3: Mixed identifiers ===\n");
            fw.write("JSON: [" + json3 + "]\n");
            fw.write("Length: " + json3.length() + "\n");
            fw.write("Char by char:\n");
            for (int i = 0; i < json3.length(); i++) {
                char c = json3.charAt(i);
                fw.write(i + ": '" + c + "' (" + (int) c + ")\n");
            }
            fw.write("\n");

            fw.write("=== Test 4: Meta styles ===\n");
            fw.write("JSON: [" + json4 + "]\n");
            fw.write("Length: " + json4.length() + "\n");
            fw.write("Char by char:\n");
            for (int i = 0; i < json4.length(); i++) {
                char c = json4.charAt(i);
                fw.write(i + ": '" + c + "' (" + (int) c + ")\n");
            }
            fw.write("\n");
        }
    }
}
