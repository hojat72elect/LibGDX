package com.kotcrab.vis.usl.lang;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link StyleIdentifier}.
 */
public class StyleIdentifierTest {

    private BasicIdentifier basicIdentifier;

    @Before
    public void setUp() {
        basicIdentifier = new BasicIdentifier("basic", "content");
        GroupIdentifier nestedGroup = new GroupIdentifier();
        nestedGroup.name = "nested";
    }

    @Test
    public void testDefaultConstructor() {
        StyleIdentifier identifier = new StyleIdentifier();
        assertNull("Name should be null by default", identifier.name);
        assertNotNull("Inherits list should be initialized", identifier.inherits);
        assertNotNull("Content list should be initialized", identifier.content);
        assertFalse("MetaStyle should be false by default", identifier.metaStyle);
        assertTrue("Inherits list should be empty", identifier.inherits.isEmpty());
        assertTrue("Content list should be empty", identifier.content.isEmpty());
    }

    @Test
    public void testCopyConstructor() {
        // Setup original
        StyleIdentifier original = new StyleIdentifier();
        original.name = "originalStyle";
        original.metaStyle = true;
        original.inherits.add("parentStyle");
        original.content.add(new BasicIdentifier("property", "value"));

        // Create copy
        StyleIdentifier copy = new StyleIdentifier(original);

        // Verify copy
        assertEquals("Copied name should match", original.name, copy.name);
        assertEquals("Copied metaStyle should match", original.metaStyle, copy.metaStyle);
        assertEquals("Inherits size should match", original.inherits.size(), copy.inherits.size());
        assertTrue("Inherits should contain same elements", copy.inherits.containsAll(original.inherits));
        assertEquals("Content size should match", original.content.size(), copy.content.size());

        // Verify deep copy
        BasicIdentifier copiedBasic = (BasicIdentifier) copy.content.get(0);
        assertEquals("Basic identifier should be copied", "property", copiedBasic.name);
        assertNotSame("Basic identifier should be new instance", original.content.get(0), copiedBasic);
    }

    @Test
    public void testCopyConstructorEmpty() {
        StyleIdentifier original = new StyleIdentifier();
        StyleIdentifier copy = new StyleIdentifier(original);

        assertFalse("Copy should have metaStyle false", copy.metaStyle);
        assertTrue("Copy of empty should have empty inherits", copy.inherits.isEmpty());
        assertTrue("Copy of empty should have empty content", copy.content.isEmpty());
    }

    @Test
    public void testMetaStyleDefault() {
        StyleIdentifier identifier = new StyleIdentifier();
        assertFalse("MetaStyle should be false by default", identifier.metaStyle);
    }

    @Test
    public void testMetaStyleModification() {
        StyleIdentifier identifier = new StyleIdentifier();
        identifier.metaStyle = true;
        assertTrue("MetaStyle should be modifiable to true", identifier.metaStyle);

        identifier.metaStyle = false;
        assertFalse("MetaStyle should be modifiable to false", identifier.metaStyle);
    }


    @Test
    public void testEqualsSameInstance() {
        StyleIdentifier identifier = new StyleIdentifier();
        assertEquals("Same instance should be equal", identifier, identifier);
    }

    @Test
    public void testEqualsNull() {
        StyleIdentifier identifier = new StyleIdentifier();
        assertNotEquals("Should not be equal to null", null, identifier);
    }

    @Test
    public void testEqualsDifferentClass() {
        StyleIdentifier identifier = new StyleIdentifier();
        assertNotEquals("Should not be equal to different class", "string", identifier);
    }

    @Test
    public void testEqualsInheritedBehavior() {
        StyleIdentifier identifier1 = new StyleIdentifier();
        StyleIdentifier identifier2 = new StyleIdentifier();
        identifier1.name = "test";
        identifier2.name = "test";
        identifier1.metaStyle = false;
        identifier2.metaStyle = false;
        assertEquals("Should inherit equals behavior from GroupIdentifier", identifier1, identifier2);
    }

    @Test
    public void testEqualsComplexScenario() {
        StyleIdentifier identifier1 = new StyleIdentifier();
        StyleIdentifier identifier2 = new StyleIdentifier();

        // Setup both with same properties
        identifier1.name = "style";
        identifier1.metaStyle = true;
        identifier1.inherits.add("base");
        identifier1.content.add(new BasicIdentifier("prop", "value"));

        identifier2.name = "style";
        identifier2.metaStyle = true;
        identifier2.inherits.add("base");
        identifier2.content.add(new BasicIdentifier("prop", "value"));

        assertEquals("Complex style identifiers should be equal", identifier1, identifier2);
    }

    @Test
    public void testHashCodeConsistency() {
        StyleIdentifier identifier = new StyleIdentifier();
        identifier.name = "test";
        identifier.metaStyle = true;
        int hashCode1 = identifier.hashCode();
        int hashCode2 = identifier.hashCode();
        assertEquals("HashCode should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeEqualObjects() {
        StyleIdentifier identifier1 = new StyleIdentifier();
        StyleIdentifier identifier2 = new StyleIdentifier();
        identifier1.name = "test";
        identifier1.metaStyle = true;
        identifier2.name = "test";
        identifier2.metaStyle = true;
        assertEquals("Equal objects should have same hash code", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testHashCodeDifferentMetaStyle() {
        StyleIdentifier identifier1 = new StyleIdentifier();
        StyleIdentifier identifier2 = new StyleIdentifier();
        identifier1.name = "test";
        identifier1.metaStyle = true;
        identifier2.name = "test";
        identifier2.metaStyle = false;
        assertNotEquals("Different metaStyle should result in different hash codes",
                identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testInheritedGroupIdentifierFunctionality() {
        StyleIdentifier style = new StyleIdentifier();

        // Test inherits functionality
        style.inherits.add("parentStyle");
        assertEquals("Should inherit from GroupIdentifier", 1, style.inherits.size());
        assertTrue("Should contain inherited style", style.inherits.contains("parentStyle"));

        // Test content functionality
        style.content.add(basicIdentifier);
        assertEquals("Should contain content", 1, style.content.size());
        assertEquals("Should contain basic identifier", basicIdentifier, style.content.get(0));

        // Test name functionality
        style.name = "testStyle";
        assertEquals("Name should be set", "testStyle", style.name);
    }

    @Test
    public void testInheritedCopyConstructorBehavior() {
        StyleIdentifier original = new StyleIdentifier();
        original.name = "original";
        original.metaStyle = true;
        original.inherits.add("base1");
        original.inherits.add("base2");
        original.content.add(new BasicIdentifier("prop1", "value1"));

        // Add nested group
        GroupIdentifier nested = new GroupIdentifier();
        nested.name = "nested";
        nested.content.add(new BasicIdentifier("nestedProp", "nestedValue"));
        original.content.add(nested);

        StyleIdentifier copy = new StyleIdentifier(original);

        // Verify all inherited properties are copied correctly
        assertEquals("Name should be copied", original.name, copy.name);
        assertEquals("MetaStyle should be copied", original.metaStyle, copy.metaStyle);
        assertEquals("Inherits should be copied", original.inherits.size(), copy.inherits.size());
        assertEquals("Content should be copied", original.content.size(), copy.content.size());

        // Verify deep copy of nested structures
        GroupIdentifier copiedNested = (GroupIdentifier) copy.content.get(1);
        assertEquals("Nested group should be copied", nested.name, copiedNested.name);
        assertNotSame("Nested group should be new instance", nested, copiedNested);
    }

    @Test
    public void testStyleWithIdentifierContent() {
        StyleIdentifier style = new StyleIdentifier();
        style.name = "mainStyle";
        style.metaStyle = false;

        // Add basic identifier
        BasicIdentifier basic = new BasicIdentifier("color", "red");
        style.content.add(basic);

        // Add nested group
        GroupIdentifier subStyle = new GroupIdentifier();
        subStyle.name = "subStyle";
        style.content.add(subStyle);

        assertEquals("Should contain both items", 2, style.content.size());
        assertTrue("Should contain basic identifier", style.content.contains(basic));
        assertTrue("Should contain nested group", style.content.contains(subStyle));
    }

    @Test
    public void testMetaStyleWithIdentifierContent() {
        StyleIdentifier metaStyle = new StyleIdentifier();
        metaStyle.name = "metaStyle";
        metaStyle.metaStyle = true;

        metaStyle.inherits.add("baseMeta");
        metaStyle.content.add(new BasicIdentifier("metaProperty", "metaValue"));

        assertTrue("MetaStyle should be true", metaStyle.metaStyle);
        assertEquals("Should inherit from base", 1, metaStyle.inherits.size());
        assertEquals("Should contain meta property", 1, metaStyle.content.size());
    }
}
