package com.kotcrab.vis.usl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Integration test to understand USL parsing behavior
 */
public class USLIntegrationTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testBasicUSLParsing() {
        try {
            String usl = "name: content";
            String json = USL.parse(tempFolder.getRoot(), usl);
            System.out.println("=== Basic USL Parsing ===");
            System.out.println("Input: " + usl);
            System.out.println("JSON output: [" + json + "]");
            System.out.println("Length: " + json.length());
            System.out.println("Contains braces: " + json.contains("{"));
            System.out.println("Contains name: " + json.contains("name"));
            System.out.println("Contains content: " + json.contains("content"));
            System.out.println("========================");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testStyleBlockUSL() {
        try {
            String usl = "#block {\n  name: content\n}";
            String json = USL.parse(tempFolder.getRoot(), usl);
            System.out.println("=== Style Block USL ===");
            System.out.println("Input: " + usl);
            System.out.println("JSON output: [" + json + "]");
            System.out.println("Length: " + json.length());
            System.out.println("Contains block: " + json.contains("block"));
            System.out.println("Contains name: " + json.contains("name"));
            System.out.println("Contains content: " + json.contains("content"));
            System.out.println("=======================");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
