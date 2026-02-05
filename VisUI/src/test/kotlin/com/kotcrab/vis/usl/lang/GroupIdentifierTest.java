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
 * Unit tests for {@link GroupIdentifier}.
 */
public class GroupIdentifierTest {

    private GroupIdentifier groupIdentifier;
    private BasicIdentifier basicIdentifier1;
    private BasicIdentifier basicIdentifier2;
    private GroupIdentifier nestedGroup;

    @Before
    public void setUp() {
        groupIdentifier = new GroupIdentifier();
        basicIdentifier1 = new BasicIdentifier("basic1", "content1");
        basicIdentifier2 = new BasicIdentifier("basic2", "content2");
        nestedGroup = new GroupIdentifier();
        nestedGroup.name = "nested";
    }

    @Test
    public void testDefaultConstructor() {
        GroupIdentifier identifier = new GroupIdentifier();
        assertNull("Name should be null by default", identifier.name);
        assertNotNull("Inherits list should be initialized", identifier.inherits);
        assertNotNull("Content list should be initialized", identifier.content);
        assertTrue("Inherits list should be empty", identifier.inherits.isEmpty());
        assertTrue("Content list should be empty", identifier.content.isEmpty());
    }

    @Test
    public void testCopyConstructor() {
        // Setup original
        GroupIdentifier original = new GroupIdentifier();
        original.name = "original";
        original.inherits.add("parent1");
        original.inherits.add("parent2");
        original.content.add(new BasicIdentifier("child1", "value1"));
        original.content.add(nestedGroup);

        // Create copy
        GroupIdentifier copy = new GroupIdentifier(original);

        // Verify copy
        assertEquals("Copied name should match", original.name, copy.name);
        assertEquals("Inherits size should match", original.inherits.size(), copy.inherits.size());
        assertTrue("Inherits should contain same elements", copy.inherits.containsAll(original.inherits));
        assertEquals("Content size should match", original.content.size(), copy.content.size());

        // Verify deep copy of content
        BasicIdentifier copiedBasic = (BasicIdentifier) copy.content.get(0);
        assertEquals("Basic identifier should be copied", "child1", copiedBasic.name);
        assertEquals("Basic identifier content should be copied", "value1", copiedBasic.content);
        assertNotSame("Basic identifier should be new instance", original.content.get(0), copiedBasic);

        GroupIdentifier copiedGroup = (GroupIdentifier) copy.content.get(1);
        assertEquals("Nested group should be copied", "nested", copiedGroup.name);
        assertNotSame("Nested group should be new instance", original.content.get(1), copiedGroup);
    }

    @Test
    public void testCopyConstructorEmpty() {
        GroupIdentifier original = new GroupIdentifier();
        GroupIdentifier copy = new GroupIdentifier(original);

        assertTrue("Copy of empty should have empty inherits", copy.inherits.isEmpty());
        assertTrue("Copy of empty should have empty content", copy.content.isEmpty());
    }

    @Test
    public void testEqualsSameInstance() {
        GroupIdentifier identifier = new GroupIdentifier();
        assertEquals("Same instance should be equal", identifier, identifier);
    }

    @Test
    public void testEqualsNull() {
        GroupIdentifier identifier = new GroupIdentifier();
        assertNotEquals("Should not be equal to null", null, identifier);
    }

    @Test
    public void testEqualsDifferentClass() {
        GroupIdentifier identifier = new GroupIdentifier();
        assertNotEquals("Should not be equal to different class", "string", identifier);
    }

    @Test
    public void testEqualsSameName() {
        GroupIdentifier identifier1 = new GroupIdentifier();
        GroupIdentifier identifier2 = new GroupIdentifier();
        identifier1.name = "test";
        identifier2.name = "test";
        assertEquals("Identifiers with same name should be equal", identifier1, identifier2);
    }

    @Test
    public void testEqualsDifferentName() {
        GroupIdentifier identifier1 = new GroupIdentifier();
        GroupIdentifier identifier2 = new GroupIdentifier();
        identifier1.name = "test1";
        identifier2.name = "test2";
        assertFalse("Identifiers with different names should not be equal", identifier1.equals(identifier2));
    }

    @Test
    public void testHashCodeConsistency() {
        GroupIdentifier identifier = new GroupIdentifier();
        identifier.name = "test";
        int hashCode1 = identifier.hashCode();
        int hashCode2 = identifier.hashCode();
        assertEquals("HashCode should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeEqualObjects() {
        GroupIdentifier identifier1 = new GroupIdentifier();
        GroupIdentifier identifier2 = new GroupIdentifier();
        identifier1.name = "test";
        identifier2.name = "test";
        assertEquals("Equal objects should have same hash code", identifier1.hashCode(), identifier2.hashCode());
    }

    @Test
    public void testAddBasicIdentifier() {
        groupIdentifier.content.add(basicIdentifier1);
        assertEquals("Should contain one basic identifier", 1, groupIdentifier.content.size());
        assertEquals("Should contain the added identifier", basicIdentifier1, groupIdentifier.content.get(0));
    }

    @Test
    public void testAddNestedGroup() {
        groupIdentifier.content.add(nestedGroup);
        assertEquals("Should contain one nested group", 1, groupIdentifier.content.size());
        assertEquals("Should contain the added group", nestedGroup, groupIdentifier.content.get(0));
    }

    @Test
    public void testAddMultipleContent() {
        groupIdentifier.content.add(basicIdentifier1);
        groupIdentifier.content.add(basicIdentifier2);
        groupIdentifier.content.add(nestedGroup);
        assertEquals("Should contain three items", 3, groupIdentifier.content.size());
    }

    @Test
    public void testAddInherits() {
        groupIdentifier.inherits.add("parent1");
        groupIdentifier.inherits.add("parent2");
        assertEquals("Should contain two inherits", 2, groupIdentifier.inherits.size());
        assertTrue("Should contain parent1", groupIdentifier.inherits.contains("parent1"));
        assertTrue("Should contain parent2", groupIdentifier.inherits.contains("parent2"));
    }

    @Test
    public void testRemoveInherits() {
        groupIdentifier.inherits.add("parent1");
        groupIdentifier.inherits.add("parent2");
        groupIdentifier.inherits.remove("parent1");
        assertEquals("Should contain one inherit", 1, groupIdentifier.inherits.size());
        assertFalse("Should not contain parent1", groupIdentifier.inherits.contains("parent1"));
        assertTrue("Should still contain parent2", groupIdentifier.inherits.contains("parent2"));
    }

    @Test
    public void testDeepCopyWithComplexStructure() {
        // Create complex structure
        GroupIdentifier original = new GroupIdentifier();
        original.name = "complex";
        original.inherits.add("base1");
        original.inherits.add("base2");

        BasicIdentifier basic1 = new BasicIdentifier("prop1", "value1");
        BasicIdentifier basic2 = new BasicIdentifier("prop2", "value2");
        original.content.add(basic1);
        original.content.add(basic2);

        GroupIdentifier nested = new GroupIdentifier();
        nested.name = "nested";
        nested.inherits.add("nestedBase");
        nested.content.add(new BasicIdentifier("nestedProp", "nestedValue"));
        original.content.add(nested);

        // Copy and verify
        GroupIdentifier copy = new GroupIdentifier(original);

        // Modify original
        original.name = "modified";
        original.inherits.add("newBase");
        basic1.content = "modifiedValue";
        nested.name = "modifiedNested";

        // Verify copy is unaffected
        assertEquals("Copy name should be unchanged", "complex", copy.name);
        assertEquals("Copy inherits should be unchanged", 2, copy.inherits.size());
        assertFalse("Copy should not have new base", copy.inherits.contains("newBase"));

        BasicIdentifier copiedBasic1 = (BasicIdentifier) copy.content.get(0);
        assertEquals("Copied basic content should be unchanged", "value1", copiedBasic1.content);

        GroupIdentifier copiedNested = (GroupIdentifier) copy.content.get(2);
        assertEquals("Copied nested name should be unchanged", "nested", copiedNested.name);
    }
}
