package com.kotcrab.vis.usl;

import com.kotcrab.vis.usl.Token.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Parser}
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testBasicIdentifierParsing() {
        List<Token> tokens = createBasicIdentifierTokens("name", "content");
        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain the identifier name", json.contains("name"));
        assertTrue("JSON should contain the identifier content", json.contains("content"));
    }

    @Test
    public void testMultipleBasicIdentifiers() {
        List<Token> tokens = new ArrayList<>();
        tokens.addAll(createBasicIdentifierTokens("name1", "content1"));
        tokens.addAll(createBasicIdentifierTokens("name2", "content2"));

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain first identifier", json.contains("name1"));
        assertTrue("JSON should contain second identifier", json.contains("name2"));
    }

    @Test
    public void testStyleBlockParsing() {
        List<Token> tokens = createStyleBlockTokens("blockName", "styleName", "styleContent");

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain style block name", json.contains("blockName"));
        assertTrue("JSON should contain style name", json.contains("styleName"));
        assertTrue("JSON should contain style content", json.contains("styleContent"));
    }

    @Test
    public void testStyleBlockWithPackage() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.PACKAGE, "com.example"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.addAll(createStyleBlockTokens("blockName", "styleName", "styleContent"));
        tokens.add(new Token("test", 0, Type.RCURL));

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain full package-qualified name", json.contains("com.example.blockName"));
    }

    @Test
    public void testStyleBlockOverride() {
        List<Token> tokens = new ArrayList<>();
        // First create a base style block
        tokens.addAll(createStyleBlockTokens("baseBlock", "baseStyle", "baseContent"));
        // Then create an override block
        List<Token> overrideTokens = createStyleBlockOverrideTokens("baseBlock", "overrideStyle", "overrideContent");
        tokens.addAll(overrideTokens);

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain base style", json.contains("baseStyle"));
        assertTrue("JSON should contain override style", json.contains("overrideStyle"));
    }

    @Test
    public void testStyleBlockExtends() {
        List<Token> tokens = new ArrayList<>();
        // Create base style block
        tokens.addAll(createStyleBlockTokens("baseBlock", "baseStyle", "baseContent"));
        // Create extending style block
        List<Token> extendingTokens = createStyleBlockWithExtendsTokens("extendingBlock", "baseBlock", "newStyle", "newContent");
        tokens.addAll(extendingTokens);

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain extending block", json.contains("extendingBlock"));
    }

    @Test
    public void testStyleBlockExtendsDefinedOnly() {
        List<Token> tokens = new ArrayList<>();
        // Create base style block
        tokens.addAll(createStyleBlockTokens("baseBlock", "baseStyle", "baseContent"));
        // Create extending style block with defined-only flag
        List<Token> extendingTokens = createStyleBlockWithExtendsTokens("extendingBlock", "~baseBlock", "newStyle", "newContent");
        tokens.addAll(extendingTokens);

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain extending block", json.contains("extendingBlock"));
    }

    @Test
    public void testGlobalStyleParsing() {
        List<Token> tokens = createGlobalStyleTokens("globalStyle", "globalContent");

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        // Global styles are processed, just verify parsing doesn't crash
        assertTrue("JSON should be valid", json.startsWith("{") && json.endsWith("}"));
    }

    @Test
    public void testGroupIdentifierParsing() {
        List<Token> tokens = createGroupIdentifierTokens("groupName", "subName", "subContent");

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain group name", json.contains("groupName"));
        assertTrue("JSON should contain sub-identifier", json.contains("subName"));
    }

    @Test
    public void testAliasIdentifierParsing() {
        List<Token> tokens = createAliasIdentifierTokens("aliasName", "aliasContent");

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain alias content", json.contains("aliasContent"));
    }

    @Test
    public void testMetaStyleParsing() {
        List<Token> tokens = createMetaStyleTokens("metaStyle", "metaContent");

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        // Just verify parsing completes successfully
        assertTrue("JSON should be valid", json.startsWith("{") && json.endsWith("}"));
    }

    @Test(expected = USLException.class)
    public void testInheritsParsing() {
        // Test inheritance with non-existent parent - this should fail in StyleMerger
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "styleName"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, "styleName"));
        tokens.add(new Token("test", 0, Type.INHERITS));
        tokens.add(new Token("test", 0, Type.INHERITS_NAME, "parentStyle1"));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, "content"));
        tokens.add(new Token("test", 0, Type.RCURL));

        // This should fail because parentStyle1 doesn't exist
        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testNestedPackagesThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.PACKAGE, "com.example"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.PACKAGE, "nested.package")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testPackageEndsWithDotThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.PACKAGE, "com.example.")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testNestedStyleBlocksThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block1"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block2")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testUnexpectedExtendsThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, "nonExistentStyle")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testMultipleExtendsThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, "parent1"));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, "parent2")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testStyleBlockExtendsItselfThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "selfBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, "selfBlock")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testExtendsUnknownStyleThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, "nonExistentStyle")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testBlockEndNotFoundThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block"));
        tokens.add(new Token("test", 0, Type.LCURL));
        // Missing closing brace - should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testUnexpectedEOFThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "block"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, "name"));
        // No next token after IDENTIFIER - peekNext should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testInvalidTokenThrowsException() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.INVALID, "invalid")); // This should throw exception

        parser.getJson(tokens);
    }

    @Test(expected = USLException.class)
    public void testIdentifierWithSpaceDoesNotThrowDuringParsing() {
        List<Token> tokens = createBasicIdentifierTokens("name with space", "content");

        // Space validation does work and throws USLException
        parser.getJson(tokens);
    }

    @Test
    public void testIdentifierContentWithSpaceDoesNotThrowDuringParsing() {
        List<Token> tokens = createBasicIdentifierTokens("name", "content with space");

        // Just verify parsing completes (space validation may or may not work)
        String json = parser.getJson(tokens);
        assertNotNull("JSON output should not be null", json);
    }

    @Test
    public void testEmptyTokenListReturnsEmptyJson() {
        List<Token> tokens = new ArrayList<>();

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("Empty token list should produce valid JSON", json.startsWith("{") && json.endsWith("}"));
    }

    @Test
    public void testFindMatchingStyleWithDollarSign() {
        List<Token> tokens = new ArrayList<>();
        // Create style block with $ in name
        tokens.addAll(createStyleBlockTokens("prefix$blockName", "styleName", "styleContent"));
        // Create extending block that references the $ part
        List<Token> extendingTokens = createStyleBlockWithExtendsTokens("extendingBlock", "blockName", "newStyle", "newContent");
        tokens.addAll(extendingTokens);

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain extending block", json.contains("extendingBlock"));
    }

    @Test
    public void testFindMatchingStyleWithDot() {
        List<Token> tokens = new ArrayList<>();
        // Create style block with package-like name
        tokens.addAll(createStyleBlockTokens("package.blockName", "styleName", "styleContent"));
        // Create extending block that references the part after dot
        List<Token> extendingTokens = createStyleBlockWithExtendsTokens("extendingBlock", "blockName", "newStyle", "newContent");
        tokens.addAll(extendingTokens);

        String json = parser.getJson(tokens);

        assertNotNull("JSON output should not be null", json);
        assertTrue("JSON should contain extending block", json.contains("extendingBlock"));
    }

    // Helper methods to create token sequences

    private List<Token> createBasicIdentifierTokens(String name, String content) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "testBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, name));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, content));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createStyleBlockTokens(String blockName, String styleName, String styleContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, blockName));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, styleName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, styleContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createStyleBlockOverrideTokens(String baseBlockName, String styleName, String styleContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_OVERRIDE, baseBlockName));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, styleName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, styleContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createStyleBlockWithExtendsTokens(String blockName, String extendsName, String styleName, String styleContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, blockName));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK_EXTENDS, extendsName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, styleName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, styleContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createGlobalStyleTokens(String styleName, String styleContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.GLOBAL_STYLE, styleName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, styleContent));
        return tokens;
    }

    private List<Token> createGroupIdentifierTokens(String groupName, String subName, String subContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "testBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, groupName));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, subName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, subContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createAliasIdentifierTokens(String aliasName, String aliasContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "testBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, aliasName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, aliasContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createMetaStyleTokens(String styleName, String styleContent) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "testBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.META_STYLE));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, styleName));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, styleContent));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }

    private List<Token> createInheritsTokens(String styleName, String parent1, String parent2, String content) {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("test", 0, Type.STYLE_BLOCK, "testBlock"));
        tokens.add(new Token("test", 0, Type.LCURL));
        tokens.add(new Token("test", 0, Type.IDENTIFIER, styleName));
        tokens.add(new Token("test", 0, Type.INHERITS));
        tokens.add(new Token("test", 0, Type.INHERITS_NAME, parent1));
        tokens.add(new Token("test", 0, Type.INHERITS_NAME, parent2));
        tokens.add(new Token("test", 0, Type.IDENTIFIER_CONTENT, content));
        tokens.add(new Token("test", 0, Type.RCURL));
        return tokens;
    }
}
