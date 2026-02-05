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
            System.out.println("JSON output: " + json);
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
            System.out.println("JSON output: " + json);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
