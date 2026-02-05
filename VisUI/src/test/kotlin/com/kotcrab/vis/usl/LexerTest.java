package com.kotcrab.vis.usl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Lexer} based on actual USL format.
 */
public class LexerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private LexerContext context;

    @Before
    public void setUp() {
        context = new LexerContext(tempFolder.getRoot());
    }

    @Test
    public void testGlobalStyle() {
        String usl = ".globalStyle: { prop: value }";
        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertEquals(5, tokens.size());

        assertEquals(Token.Type.GLOBAL_STYLE, tokens.get(0).type);
        assertEquals("globalStyle", tokens.get(0).content);
        assertEquals(Token.Type.LCURL, tokens.get(1).type);
        assertEquals(Token.Type.IDENTIFIER, tokens.get(2).type);
        assertEquals("prop", tokens.get(2).content);
        assertEquals(Token.Type.IDENTIFIER_CONTENT, tokens.get(3).type);
        assertEquals("value", tokens.get(3).content);
        assertEquals(Token.Type.RCURL, tokens.get(4).type);
    }

    @Test
    public void testNestedStyles() {
        String usl = "#OuterClass: {\n  default: {\n    prop: value\n    nested: { innerProp: innerValue }\n  }\n}";
        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertEquals(13, tokens.size());

        assertEquals(Token.Type.STYLE_BLOCK, tokens.get(0).type);
        assertEquals("OuterClass", tokens.get(0).content);
        assertEquals(Token.Type.LCURL, tokens.get(1).type);
        assertEquals(Token.Type.IDENTIFIER, tokens.get(2).type);
        assertEquals("default", tokens.get(2).content);
        assertEquals(Token.Type.LCURL, tokens.get(3).type);
        assertEquals(Token.Type.IDENTIFIER, tokens.get(4).type);
        assertEquals("prop", tokens.get(4).content);
        assertEquals(Token.Type.IDENTIFIER_CONTENT, tokens.get(5).type);
        assertEquals("value", tokens.get(5).content);
        assertEquals(Token.Type.IDENTIFIER, tokens.get(6).type);
        assertEquals("nested", tokens.get(6).content);
        assertEquals(Token.Type.LCURL, tokens.get(7).type);
        assertEquals(Token.Type.IDENTIFIER, tokens.get(8).type);
        assertEquals("innerProp", tokens.get(8).content);
        assertEquals(Token.Type.IDENTIFIER_CONTENT, tokens.get(9).type);
        assertEquals("innerValue", tokens.get(9).content);
        assertEquals(Token.Type.RCURL, tokens.get(10).type);
        assertEquals(Token.Type.RCURL, tokens.get(11).type);
        assertEquals(Token.Type.RCURL, tokens.get(12).type);
    }

    @Test
    public void testIncludeSource() {
        Lexer.addIncludeSource("/test/path");
        // This test mainly verifies the method doesn't throw an exception
        assertTrue("Include source addition should not throw exception", true);
    }

    @Test(expected = USLException.class)
    public void testUnmatchedClosingBrace() {
        String usl = "#TestClass: {\n  default: { prop: value }\n}";
        Lexer.lexUsl(context, usl);
        // Add extra closing brace
        context.tokens.add(new Token(usl, 0, Token.Type.RCURL));
        Lexer.lexUsl(context, "}");
    }

    @Test(expected = USLException.class)
    public void testMissingBlockDefinitionEnd() {
        String usl = "#TestClass";
        Lexer.lexUsl(context, usl);
    }

    @Test(expected = USLException.class)
    public void testMissingGlobalStyleDefinitionEnd() {
        String usl = ".globalStyle";
        Lexer.lexUsl(context, usl);
    }

    @Test
    public void testEmptyString() {
        String usl = "";
        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertEquals("Empty string should produce no tokens", 0, tokens.size());
    }

    @Test
    public void testOnlyWhitespace() {
        String usl = "   \t\n   \r\n   ";
        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertEquals("Whitespace only should produce no tokens", 0, tokens.size());
    }

    @Test
    public void testOnlyComments() {
        String usl = "// This is a comment\n// Another comment";
        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertEquals("Comments only should produce no tokens", 0, tokens.size());
    }

    @Test
    public void testComplexRealWorldExample() {
        String usl = "package com.badlogic.gdx.scenes.scene2d.ui {\n" +
                "  #Button$ButtonStyle: {\n" +
                "    default: { down: default-round-down, up: default-round }\n" +
                "    toggle inherits default: { checked: default-round-down }\n" +
                "  }\n" +
                "}";

        Lexer.lexUsl(context, usl);

        List<Token> tokens = context.tokens;
        assertTrue("Should have multiple tokens", tokens.size() > 10);

        // Verify key tokens exist
        boolean hasPackage = false;
        boolean hasStyleBlock = false;
        boolean hasInherits = false;

        for (Token token : tokens) {
            switch (token.type) {
                case PACKAGE:
                    hasPackage = true;
                    break;
                case STYLE_BLOCK:
                    hasStyleBlock = true;
                    break;
                case INHERITS:
                    hasInherits = true;
                    break;
            }
        }

        assertTrue("Should have package token", hasPackage);
        assertTrue("Should have style block token", hasStyleBlock);
        assertTrue("Should have inherits token", hasInherits);
    }
}
