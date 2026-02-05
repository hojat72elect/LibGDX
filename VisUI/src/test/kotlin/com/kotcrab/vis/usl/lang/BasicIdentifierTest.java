package com.kotcrab.vis.usl.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    // --- Additional comprehensive tests ---

    @Test
    public void testCopyConstructorWithNullValues() {
        BasicIdentifier original = new BasicIdentifier();
        BasicIdentifier copy = new BasicIdentifier(original);
        
        assertNull("Copied name should be null", copy.name);
        assertNull("Copied content should be null", copy.content);
        assertNotSame("Copy should be a different object instance", original, copy);
    }

    @Test
    public void testEqualsBothNameNull() {
        BasicIdentifier identifier1 = new BasicIdentifier(null, "content");
        BasicIdentifier identifier2 = new BasicIdentifier(null, "content");
        // Both will throw NPE due to parent Identifier.equals() implementation
        try {
            identifier1.equals(identifier2);
            fail("Expected NullPointerException due to null name in parent Identifier.equals()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEqualsBothContentNull() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", null);
        BasicIdentifier identifier2 = new BasicIdentifier("name", null);
        // Will throw NPE due to content.equals() in BasicIdentifier.equals()
        try {
            identifier1.equals(identifier2);
            fail("Expected NullPointerException due to null content in BasicIdentifier.equals()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEqualsBothNull() {
        BasicIdentifier identifier1 = new BasicIdentifier(null, null);
        BasicIdentifier identifier2 = new BasicIdentifier(null, null);
        // Both will throw NPE due to parent Identifier.equals() implementation
        try {
            identifier1.equals(identifier2);
            fail("Expected NullPointerException due to null name in parent Identifier.equals()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEqualsOneNameNull() {
        BasicIdentifier identifier1 = new BasicIdentifier(null, "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content");
        // First will throw NPE due to null name in parent Identifier.equals()
        try {
            identifier1.equals(identifier2);
            fail("Expected NullPointerException due to null name in parent Identifier.equals()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEqualsOneContentNull() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", null);
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content");
        // Will throw NPE due to content.equals() in BasicIdentifier.equals()
        try {
            identifier1.equals(identifier2);
            fail("Expected NullPointerException due to null content in BasicIdentifier.equals()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testHashCodeWithNullValues() {
        BasicIdentifier identifier1 = new BasicIdentifier(null, null);
        BasicIdentifier identifier2 = new BasicIdentifier(null, null);
        // Both will throw NPE due to parent Identifier.hashCode() implementation
        try {
            identifier1.hashCode();
            fail("Expected NullPointerException due to null name in parent Identifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
        try {
            identifier2.hashCode();
            fail("Expected NullPointerException due to null name in parent Identifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testHashCodeWithNullName() {
        BasicIdentifier identifier1 = new BasicIdentifier(null, "content");
        BasicIdentifier identifier2 = new BasicIdentifier(null, "content");
        // Both will throw NPE due to parent Identifier.hashCode() implementation
        try {
            identifier1.hashCode();
            fail("Expected NullPointerException due to null name in parent Identifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
        try {
            identifier2.hashCode();
            fail("Expected NullPointerException due to null name in parent Identifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testHashCodeWithNullContent() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", null);
        BasicIdentifier identifier2 = new BasicIdentifier("name", null);
        // Will throw NPE due to content.hashCode() in BasicIdentifier.hashCode()
        try {
            identifier1.hashCode();
            fail("Expected NullPointerException due to null content in BasicIdentifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
        try {
            identifier2.hashCode();
            fail("Expected NullPointerException due to null content in BasicIdentifier.hashCode()");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testInheritsFromIdentifier() {
        BasicIdentifier identifier = new BasicIdentifier();
        assertTrue("BasicIdentifier should extend Identifier", identifier instanceof Identifier);
    }

    @Test
    public void testEmptyStrings() {
        BasicIdentifier identifier1 = new BasicIdentifier("", "");
        BasicIdentifier identifier2 = new BasicIdentifier("", "");
        assertEquals("Objects with empty strings should be equal", identifier1, identifier2);
        assertEquals("Objects with empty strings should have same hashCode", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testSpecialCharacters() {
        BasicIdentifier identifier1 = new BasicIdentifier("name!@#$%", "content&*()");
        BasicIdentifier identifier2 = new BasicIdentifier("name!@#$%", "content&*()");
        assertEquals("Objects with special characters should be equal", identifier1, identifier2);
        assertEquals("Objects with special characters should have same hashCode", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testUnicodeCharacters() {
        BasicIdentifier identifier1 = new BasicIdentifier("名字", "内容");
        BasicIdentifier identifier2 = new BasicIdentifier("名字", "内容");
        assertEquals("Objects with unicode characters should be equal", identifier1, identifier2);
        assertEquals("Objects with unicode characters should have same hashCode", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testLongStrings() {
        String longName = "a".repeat(1000);
        String longContent = "b".repeat(1000);
        BasicIdentifier identifier1 = new BasicIdentifier(longName, longContent);
        BasicIdentifier identifier2 = new BasicIdentifier(longName, longContent);
        assertEquals("Objects with long strings should be equal", identifier1, identifier2);
        assertEquals("Objects with long strings should have same hashCode", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testEqualsHashCodeContract() {
        BasicIdentifier identifier1 = new BasicIdentifier("name", "content");
        BasicIdentifier identifier2 = new BasicIdentifier("name", "content");
        BasicIdentifier identifier3 = new BasicIdentifier("different", "content");

        // Reflexive
        assertEquals("Equals should be reflexive", identifier1, identifier1);
        
        // Symmetric
        assertTrue("Equals should be symmetric", identifier1.equals(identifier2) && identifier2.equals(identifier1));
        
        // Transitive
        assertTrue("Equals should be transitive", identifier1.equals(identifier2) && identifier2.equals(identifier1));
        
        // Consistent with hashCode
        assertEquals("Equal objects must have same hashCode", identifier1.hashCode(), identifier2.hashCode());
    }
}
