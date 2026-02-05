package com.kotcrab.vis.usl.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link BasicIdentifier}.
 */
public class BasicIdentifierTest {

    @Test
    public void testDefaultConstructor() {
        BasicIdentifier identifier = new BasicIdentifier();
        assertNull("Name should be null by default", identifier.name);
        assertNull("Content should be null by default", identifier.content);
    }

    @Test
    public void testParameterizedConstructor() {
        BasicIdentifier identifier = new BasicIdentifier("testName", "testContent");
        assertEquals("Name should be set correctly", "testName", identifier.name);
        assertEquals("Content should be set correctly", "testContent", identifier.content);
    }

    @Test
    public void testCopyConstructor() {
        BasicIdentifier original = new BasicIdentifier("originalName", "originalContent");
        BasicIdentifier copy = new BasicIdentifier(original);

        assertEquals("Copied name should match original", original.name, copy.name);
        assertEquals("Copied content should match original", original.content, copy.content);
        assertNotSame("Copied object should be different instance", original, copy);
    }

    // --- Equals tests ---

    @Test
    public void testEqualsSameInstance() {
        BasicIdentifier identifier = new BasicIdentifier("name", "content");
        assertEquals("Same instance should be equal", identifier, identifier);
    }

    @Test
    public void testEqualsNull() {
        BasicIdentifier identifier = new BasicIdentifier("name", "content");
        assertNotEquals("Should not be equal to null", null, identifier);
    }

    @Test
    public void testEqualsDifferentClass() {
        BasicIdentifier identifier = new BasicIdentifier("name", "content");
        assertNotEquals("Should not be equal to different class", "string", identifier);
    }

    @Test
    public void testEqualsSameNameAndContent() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content");
        assertEquals("Should be equal with same name and content", identifier1, identifier2);
    }

    @Test
    public void testEqualsDifferentName() {
        BasicIdentifier identifier1 = new BasicIdentifier("name1", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name2", "content");
        assertNotEquals("Should not be equal with different names", identifier1, identifier2);
    }

    @Test
    public void testEqualsDifferentContent() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", "content1");
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content2");
        assertNotEquals("Should not be equal with different content", identifier1, identifier2);
    }

    @Test
    public void testEqualsOneNullContent() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name", null);
        assertNotEquals("Should not be equal when one has null content", identifier1, identifier2);
    }

    @Test
    public void testHashCodeConsistency() {
        BasicIdentifier identifier = new BasicIdentifier("name", "content");
        int hashCode1 = identifier.hashCode();
        int hashCode2 = identifier.hashCode();
        assertEquals("HashCode should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeEqualObjects() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content");
        assertEquals("Equal objects should have same hash code", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testHashCodeDifferentObjects() {
        BasicIdentifier identifier1 = new BasicIdentifier("name1", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name2", "content");
        assertNotEquals("Different objects should have different hash codes", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testInheritedNameField() {
        BasicIdentifier identifier = new BasicIdentifier("testName", "testContent");
        identifier.name = "modifiedName";
        assertEquals("Name field should be modifiable", "modifiedName", identifier.name);
    }

    @Test
    public void testContentField() {
        BasicIdentifier identifier = new BasicIdentifier();
        identifier.content = "testContent";
        assertEquals("Content field should be modifiable", "testContent", identifier.content);
    }
}
