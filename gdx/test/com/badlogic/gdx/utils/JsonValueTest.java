package com.badlogic.gdx.utils;

import org.junit.Assert;
import org.junit.Test;

public class JsonValueTest {

    @Test
    public void testAddingRemovedValue() {
        // Prepare two JSON objects
        JsonValue firstObject = new JsonValue(JsonValue.ValueType.object);
        JsonValue secondObject = new JsonValue(JsonValue.ValueType.object);

        firstObject.addChild("a", new JsonValue("A"));
        secondObject.addChild("b", new JsonValue("B"));
        secondObject.addChild("c", new JsonValue("C"));

        // Remove an item from one object and add it to the other
        JsonValue b = secondObject.remove("b");
        firstObject.addChild(b);

        // Check if both objects have the expected children
        Assert.assertNotNull(firstObject.get("a"));
        Assert.assertNotNull(firstObject.get("b"));
        Assert.assertNull(firstObject.get("c"));

        Assert.assertNull(secondObject.get("a"));
        Assert.assertNull(secondObject.get("b"));
        Assert.assertNotNull(secondObject.get("c"));
    }

    @Test
    public void testReplaceValue() {
        JsonValue object = new JsonValue(JsonValue.ValueType.object);

        object.addChild("a", new JsonValue("A"));
        object.addChild("b", new JsonValue("B"));
        object.addChild("c", new JsonValue("C"));
        object.addChild("d", new JsonValue("D"));

        object.setChild("b", new JsonValue("X"));
        object.setChild("c", new JsonValue("Y"));

        Assert.assertEquals("A", object.get("a").asString());
        Assert.assertEquals("X", object.get("b").asString());
        Assert.assertEquals("Y", object.get("c").asString());
        Assert.assertEquals("D", object.get("d").asString());

        object.setChild("a", new JsonValue("W"));
        JsonValue z = new JsonValue("Z");
        z.name = "d";
        object.setChild(z);

        Assert.assertEquals(object.get("a").parent(), object);
        Assert.assertEquals("W", object.get("a").asString());
        Assert.assertEquals("X", object.get("b").asString());
        Assert.assertEquals("Y", object.get("c").asString());
        Assert.assertEquals("Z", object.get("d").asString());
    }

    @Test
    public void testCopyConstructor() {
        JsonValue b = new JsonValue(JsonValue.ValueType.object);
        b.addChild("c", new JsonValue("C"));
        b.addChild("d", new JsonValue("D"));

        JsonValue object = new JsonValue(JsonValue.ValueType.object);
        object.addChild("a", new JsonValue("A"));
        object.addChild("b", b);
        object.addChild("e", new JsonValue(123));

        JsonValue copy = new JsonValue(object);

        Assert.assertEquals("A", object.get("a").asString());
        Assert.assertEquals(object.get("b"), b);
        Assert.assertEquals("C", object.get("b").get("c").asString());
        Assert.assertEquals("D", object.get("b").get("d").asString());
        Assert.assertEquals(object.get("b").parent(), object);
        Assert.assertEquals(123, object.get("e").asInt());
        Assert.assertNull(object.get("E"));
        Assert.assertEquals(123, object.getIgnoreCase("E").asInt());

        Assert.assertEquals("A", copy.get("a").asString());
        Assert.assertNotEquals(copy.get("b"), b);
        Assert.assertEquals("C", copy.get("b").get("c").asString());
        Assert.assertEquals("D", copy.get("b").get("d").asString());
        Assert.assertEquals(copy.get("b").parent(), copy);
        Assert.assertEquals(123, copy.get("e").asInt());
        Assert.assertNull(copy.get("E"));
        Assert.assertEquals(123, copy.getIgnoreCase("E").asInt());

        JsonValue bCopy = new JsonValue(copy.get("b"));
        Assert.assertNotEquals(b, bCopy);
        Assert.assertNotEquals(copy.get("b"), bCopy);
        Assert.assertEquals("C", bCopy.get("c").asString());
        Assert.assertEquals("D", bCopy.get("d").asString());
        Assert.assertEquals(bCopy.get("d").parent(), bCopy);
    }
}
