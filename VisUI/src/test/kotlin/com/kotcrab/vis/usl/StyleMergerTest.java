package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.lang.BasicIdentifier;
import com.kotcrab.vis.usl.lang.GroupIdentifier;
import com.kotcrab.vis.usl.lang.StyleBlock;
import com.kotcrab.vis.usl.lang.StyleIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StyleMergerTest {

    private ArrayList<StyleIdentifier> globalStyles;
    private ArrayList<StyleBlock> styleBlocks;
    private ArrayList<StyleBlock> styleBlocksOverride;

    @Before
    public void setUp() {
        globalStyles = new ArrayList<>();
        styleBlocks = new ArrayList<>();
        styleBlocksOverride = new ArrayList<>();
    }

    @Test
    public void testMergeEmptyLists() {
        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMergeBasicStyleBlock() {
        StyleBlock block = createBasicStyleBlock("testBlock", "testStyle", "color", "red");
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        assertEquals(1, result.size());
        assertEquals("testBlock", result.get(0).fullName);
        assertEquals(1, result.get(0).styles.size());
        assertEquals("testStyle", result.get(0).styles.get(0).name);
    }

    @Test
    public void testMergeGlobalStyles() {
        StyleIdentifier globalStyle = createStyleIdentifier(".globalStyle", "color", "blue");
        globalStyles.add(globalStyle);

        StyleBlock block = createStyleBlockWithInheritance("testBlock", "testStyle", ".globalStyle");
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        StyleIdentifier mergedStyle = result.get(0).styles.get(0);
        assertEquals(1, mergedStyle.content.size());
        assertEquals("color", mergedStyle.content.get(0).name);
        assertEquals("blue", ((BasicIdentifier) mergedStyle.content.get(0)).content);
    }

    @Test(expected = USLException.class)
    public void testMergeGlobalStylesNotFound() {
        StyleBlock block = createStyleBlockWithInheritance("testBlock", "testStyle", ".nonExistentGlobal");
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        merger.merge();
    }

    @Test
    public void testMergeBlocksOverrides() {
        StyleBlock originalBlock = createBasicStyleBlock("testBlock", "originalStyle", "color", "red");
        styleBlocks.add(originalBlock);

        StyleBlock overrideBlock = createBasicStyleBlock("testBlock", "overrideStyle", "color", "blue");
        styleBlocksOverride.add(overrideBlock);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).styles.size());
        assertEquals("originalStyle", result.get(0).styles.get(0).name);
        assertEquals("overrideStyle", result.get(0).styles.get(1).name);
    }

    @Test
    public void testMergeBlocksExtends() {
        StyleBlock baseBlock = createBasicStyleBlock("baseBlock", "baseStyle", "color", "green");
        StyleBlock extendedBlock = createBasicStyleBlock("extendedBlock", "extendedStyle", "font", "Arial");
        extendedBlock.extendsStyle = baseBlock;

        styleBlocks.add(extendedBlock);
        styleBlocks.add(baseBlock);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        assertEquals(2, result.size());
        StyleBlock extendedResult = result.get(0);
        assertEquals(2, extendedResult.styles.size());
    }

    @Test
    public void testMergeInnerStyles() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier baseStyle = createStyleIdentifier("baseStyle", "color", "red");
        StyleIdentifier inheritedStyle = createStyleIdentifier("inheritedStyle", "font", "Arial");
        inheritedStyle.inherits.add("baseStyle");

        block.styles.add(baseStyle);
        block.styles.add(inheritedStyle);
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        StyleIdentifier mergedInheritedStyle = result.get(0).styles.get(1);
        assertEquals(2, mergedInheritedStyle.content.size());
    }

    @Test(expected = USLException.class)
    public void testMergeInnerStylesNotFound() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = createStyleIdentifier("testStyle", "color", "red");
        style.inherits.add("nonExistentStyle");

        block.styles.add(style);
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        merger.merge();
    }

    @Test
    public void testMergeGroupIdentifiersWithGlobalStyles() {
        StyleIdentifier globalStyle = createStyleIdentifier(".globalGroup", "padding", "10");
        globalStyles.add(globalStyle);

        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier style = createStyleIdentifier("testStyle");
        GroupIdentifier group = new GroupIdentifier();
        group.name = "group";
        group.inherits.add(".globalGroup");
        style.content.add(group);

        block.styles.add(style);
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        GroupIdentifier mergedGroup = (GroupIdentifier) result.get(0).styles.get(0).content.get(0);
        assertEquals(1, mergedGroup.content.size());
        assertEquals("padding", mergedGroup.content.get(0).name);
    }

    @Test
    public void testMergeStylesContentNoDuplicates() {
        StyleBlock block = new StyleBlock();
        block.fullName = "testBlock";

        StyleIdentifier baseStyle = createStyleIdentifier("baseStyle", "color", "red");
        StyleIdentifier inheritedStyle = createStyleIdentifier("inheritedStyle", "color", "blue");
        inheritedStyle.inherits.add("baseStyle");

        block.styles.add(baseStyle);
        block.styles.add(inheritedStyle);
        styleBlocks.add(block);

        StyleMerger merger = new StyleMerger(globalStyles, styleBlocks, styleBlocksOverride);
        ArrayList<StyleBlock> result = merger.merge();

        StyleIdentifier mergedInheritedStyle = result.get(0).styles.get(1);
        assertEquals(1, mergedInheritedStyle.content.size());
        assertEquals("blue", ((BasicIdentifier) mergedInheritedStyle.content.get(0)).content);
    }

    private StyleBlock createBasicStyleBlock(String blockName, String styleName, String identifierName, String identifierValue) {
        StyleBlock block = new StyleBlock();
        block.fullName = blockName;
        block.styles.add(createStyleIdentifier(styleName, identifierName, identifierValue));
        return block;
    }

    private StyleBlock createStyleBlockWithInheritance(String blockName, String styleName, String inheritedStyle) {
        StyleBlock block = new StyleBlock();
        block.fullName = blockName;

        StyleIdentifier style = createStyleIdentifier(styleName);
        style.inherits.add(inheritedStyle);
        block.styles.add(style);
        return block;
    }

    private StyleIdentifier createStyleIdentifier(String name, String identifierName, String identifierValue) {
        StyleIdentifier style = new StyleIdentifier();
        style.name = name;
        BasicIdentifier identifier = new BasicIdentifier(identifierName, identifierValue);
        style.content.add(identifier);
        return style;
    }

    private StyleIdentifier createStyleIdentifier(String name) {
        StyleIdentifier style = new StyleIdentifier();
        style.name = name;
        return style;
    }
}
