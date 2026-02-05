package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MetaStyleDebugTest {

    @Test
    public void testMetaStyles() throws IOException {
        ArrayList<StyleBlock> styleBlocks = new ArrayList<>();

        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier normalStyle = new StyleIdentifier();
        normalStyle.name = "normalStyle";
        BasicIdentifier basic1 = new BasicIdentifier("color", "red");
        normalStyle.content.add(basic1);

        StyleIdentifier metaStyle = new StyleIdentifier();
        metaStyle.name = "metaStyle";
        metaStyle.metaStyle = true;
        BasicIdentifier basic2 = new BasicIdentifier("font", "Arial");
        metaStyle.content.add(basic2);

        block.styles.add(normalStyle);
        block.styles.add(metaStyle);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        // Write to a file in temp directory
        File outputFile = new File("D:/temp/meta_style_debug.txt");
        outputFile.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(outputFile)) {
            fw.write("=== Meta Styles Test ===\n");
            fw.write("JSON: [" + json + "]\n");
            fw.write("Length: " + json.length() + "\n");
            fw.write("Char by char:\n");
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                fw.write(i + ": '" + c + "' (" + (int) c + ")\n");
            }

            fw.write("\nExpected: \n");
            fw.write("{\n" +
                    "testBlock: {\n" +
                    "\tnormalStyle: {color: red }\n" +
                    "}\n" +
                    "\n}");
        }
    }
}
