package com.kotcrab.vis.usl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Debug test to understand lexer behavior
 */
public class LexerDebugTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testSimpleDebug() {
        LexerContext context = new LexerContext(tempFolder.getRoot());
        String usl = "name: content";
        try {
            Lexer.lexUsl(context, usl);
            System.out.println("Tokens: " + context.tokens.size());
            for (Token token : context.tokens) {
                System.out.println("Token: " + token.type + " content: " + token.content);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
