package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.AliasIdentifier;
import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.GroupIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class USLJsonWriterTest {

    private ArrayList<StyleBlock> styleBlocks;

    @Before
    public void setUp() {
        styleBlocks = new ArrayList<>();
    }

    @Test
    public void testGetJsonEmptyBlocks() {
        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        assertEquals("{\n\n}", json);
    }

    @Test
    public void testGetJsonSingleBlockSingleStyle() {
        StyleBlock block = createBasicStyleBlock("testBlock", "testStyle", "color", "red");
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\ttestStyle: {color: red }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonSingleBlockMultipleStyles() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style1 = createStyleIdentifier("style1", "color", "red");
        StyleIdentifier style2 = createStyleIdentifier("style2", "font", "Arial");

        block.styles.add(style1);
        block.styles.add(style2);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\tstyle1: {color: red },\n" +
                "\tstyle2: {font: Arial }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonMultipleBlocks() {
        StyleBlock block1 = createBasicStyleBlock("block1", "style1", "color", "red");
        StyleBlock block2 = createBasicStyleBlock("block2", "style2", "font", "Arial");

        styleBlocks.add(block1);
        styleBlocks.add(block2);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "block1: {\n" +
                "\tstyle1: {color: red }\n" +
                "},\n" +
                "block2: {\n" +
                "\tstyle2: {font: Arial }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonWithAliasIdentifier() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "aliasStyle";
        AliasIdentifier alias = new AliasIdentifier("someAlias");
        style.content.add(alias);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\taliasStyle: someAlias\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonWithGroupIdentifier() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "groupStyle";

        GroupIdentifier group = new GroupIdentifier();
        group.name = "group";
        BasicIdentifier basic1 = new BasicIdentifier("color", "red");
        BasicIdentifier basic2 = new BasicIdentifier("font", "Arial");

        group.content.add(basic1);
        group.content.add(basic2);
        style.content.add(group);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\tgroupStyle: {group: {color: red, font: Arial} }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonWithNestedGroupIdentifiers() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "nestedStyle";

        GroupIdentifier outerGroup = new GroupIdentifier();
        outerGroup.name = "outer";

        GroupIdentifier innerGroup = new GroupIdentifier();
        innerGroup.name = "inner";
        BasicIdentifier basic = new BasicIdentifier("color", "blue");
        innerGroup.content.add(basic);

        outerGroup.content.add(innerGroup);
        style.content.add(outerGroup);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\tnestedStyle: {outer: {inner: {color: blue}} }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonWithMixedIdentifiers() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "mixedStyle";

        BasicIdentifier basic = new BasicIdentifier("color", "red");
        GroupIdentifier group = new GroupIdentifier();
        group.name = "group";
        BasicIdentifier groupBasic = new BasicIdentifier("font", "Arial");
        group.content.add(groupBasic);
        AliasIdentifier alias = new AliasIdentifier("alias");

        style.content.add(basic);
        style.content.add(group);
        style.content.add(alias);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\tmixedStyle: {color: red, group: {font: Arial}, alias }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonSkipsMetaStyles() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier normalStyle = createStyleIdentifier("normalStyle", "color", "red");
        StyleIdentifier metaStyle = createStyleIdentifier("metaStyle", "font", "Arial");
        metaStyle.metaStyle = true;

        block.styles.add(normalStyle);
        block.styles.add(metaStyle);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\tnormalStyle: {color: red },\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonSkipsNullIdentifiers() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "testStyle";

        BasicIdentifier nullIdentifier = new BasicIdentifier("nullProp", "NULL");
        BasicIdentifier normalIdentifier = new BasicIdentifier("color", "red");

        style.content.add(nullIdentifier);
        style.content.add(normalIdentifier);

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\ttestStyle: {color: red }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    @Test
    public void testGetJsonWithEmptyStyleContent() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = new StyleIdentifier();
        style.name = "emptyStyle";

        block.styles.add(style);
        styleBlocks.add(block);

        USLJsonWriter writer = new USLJsonWriter(styleBlocks);
        String json = writer.getJson();

        String expected = "{\n" +
                "testBlock: {\n" +
                "\temptyStyle: { }\n" +
                "}\n" +
                "\n}";
        assertEquals(expected, json);
    }

    private StyleBlock createBasicStyleBlock(String blockName, String styleName, String identifierName, String identifierValue) {
        StyleBlock block = new StyleBlock();
        block.fullName = blockName;
        block.styles.add(createStyleIdentifier(styleName, identifierName, identifierValue));
        return block;
    }

    private StyleIdentifier createStyleIdentifier(String name, String identifierName, String identifierValue) {
        StyleIdentifier style = new StyleIdentifier();
        style.name = name;
        BasicIdentifier identifier = new BasicIdentifier(identifierName, identifierValue);
        style.content.add(identifier);
        return style;
    }
}
