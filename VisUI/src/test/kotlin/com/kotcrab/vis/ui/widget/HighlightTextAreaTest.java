package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.util.highlight.BaseHighlighter;
import com.kotcrab.vis.ui.util.highlight.Highlight;
import com.kotcrab.vis.ui.util.highlight.HighlightRule;
import com.kotcrab.vis.ui.util.highlight.Highlighter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HighlightTextAreaTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }

        if (Gdx.app == null) {
            Clipboard clipboard = mock(Clipboard.class);
            Gdx.app = (Application) Proxy.newProxyInstance(
                    Application.class.getClassLoader(),
                    new Class[]{Application.class},
                    (proxy, method, args) -> {
                        if ("getClipboard".equals(method.getName())) return clipboard;
                        return null;
                    });
        }
    }

    private VisTextField.VisTextFieldStyle style;

    @Before
    public void setUp() {
        style = new VisTextField.VisTextFieldStyle();
        style.font = newTestFont();
        style.fontColor = Color.WHITE;
        style.cursor = mock(Drawable.class);
        style.selection = mock(Drawable.class);
    }

    @Test
    public void testCalculateOffsetsWithoutHighlighter_createsDefaultChunks() throws Exception {
        TestableHighlightTextArea area = new TestableHighlightTextArea("a\nb", style);
        area.setSize(1000, 1000);

        area.callCalculateOffsets();

        List<ChunkView> chunks = readChunks(area);
        assertEquals(2, chunks.size());

        assertEquals("a", chunks.get(0).text);
        assertEquals(Color.WHITE, chunks.get(0).color);

        assertEquals("b", chunks.get(1).text);
        assertEquals(Color.WHITE, chunks.get(1).color);
    }

    @Test
    public void testSingleHighlightWithinLine() throws Exception {
        TestableHighlightTextArea area = new TestableHighlightTextArea("hello world", style);
        area.setSize(1000, 1000);

        Highlighter highlighter = new Highlighter();
        highlighter.word(Color.RED, "world");
        area.setHighlighter(highlighter);

        area.callCalculateOffsets();

        List<ChunkView> chunks = readChunks(area);
        assertEquals(2, chunks.size());
        assertEquals("hello ", chunks.get(0).text);
        assertEquals(Color.WHITE, chunks.get(0).color);
        assertEquals("world", chunks.get(1).text);
        assertEquals(Color.RED, chunks.get(1).color);

        assertTrue(chunks.get(0).offsetX <= chunks.get(1).offsetX);
    }

    @Test
    public void testOverlappingHighlights_earlierStartHasPriority() throws Exception {
        TestableHighlightTextArea area = new TestableHighlightTextArea("testing", style);
        area.setSize(1000, 1000);

        BaseHighlighter highlighter = new BaseHighlighter();
        highlighter.addRule(new HighlightRule() {
            @Override
            public void process(HighlightTextArea textArea, com.badlogic.gdx.utils.Array<Highlight> highlights) {
                highlights.add(new Highlight(Color.RED, 0, 7));
                highlights.add(new Highlight(Color.BLUE, 2, 4));
            }
        });
        area.setHighlighter(highlighter);

        area.callCalculateOffsets();

        List<ChunkView> chunks = readChunks(area);
        assertEquals(1, chunks.size());
        assertEquals("testing", chunks.get(0).text);
        assertEquals(Color.RED, chunks.get(0).color);
    }

    @Test
    public void testHighlightSpanningMultipleLines_carriesHighlight() throws Exception {
        // text indices: 0 a, 1 b, 2 \n, 3 c, 4 d
        TestableHighlightTextArea area = new TestableHighlightTextArea("ab\ncd", style);
        area.setSize(1000, 1000);

        BaseHighlighter highlighter = new BaseHighlighter();
        highlighter.addRule(new HighlightRule() {
            @Override
            public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
                highlights.add(new Highlight(Color.RED, 1, 4));
            }
        });
        area.setHighlighter(highlighter);

        area.callCalculateOffsets();

        List<ChunkView> chunks = readChunks(area);
        assertEquals(4, chunks.size());

        assertEquals("a", chunks.get(0).text);
        assertEquals(Color.WHITE, chunks.get(0).color);

        assertEquals("b", chunks.get(1).text);
        assertEquals(Color.RED, chunks.get(1).color);

        assertEquals("c", chunks.get(2).text);
        assertEquals(Color.RED, chunks.get(2).color);

        assertEquals("d", chunks.get(3).text);
        assertEquals(Color.WHITE, chunks.get(3).color);
    }

    private static BitmapFont newTestFont() {
        Texture mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(1);
        when(mockTexture.getHeight()).thenReturn(1);

        TextureRegion mockRegion = mock(TextureRegion.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };

        return new BitmapFont(fontData, Array.with(mockRegion), true);
    }

    private static List<ChunkView> readChunks(HighlightTextArea area) throws Exception {
        Field renderChunksField = HighlightTextArea.class.getDeclaredField("renderChunks");
        renderChunksField.setAccessible(true);

        @SuppressWarnings("unchecked")
        com.badlogic.gdx.utils.Array<Object> renderChunks = (com.badlogic.gdx.utils.Array<Object>) renderChunksField.get(area);
        assertNotNull(renderChunks);

        List<ChunkView> result = new ArrayList<>();
        for (Object chunk : renderChunks) {
            Class<?> chunkClass = chunk.getClass();

            Field textField = chunkClass.getDeclaredField("text");
            Field colorField = chunkClass.getDeclaredField("color");
            Field offsetXField = chunkClass.getDeclaredField("offsetX");
            Field lineIndexField = chunkClass.getDeclaredField("lineIndex");

            textField.setAccessible(true);
            colorField.setAccessible(true);
            offsetXField.setAccessible(true);
            lineIndexField.setAccessible(true);

            result.add(new ChunkView(
                    (String) textField.get(chunk),
                    (Color) colorField.get(chunk),
                    (Float) offsetXField.get(chunk),
                    (Integer) lineIndexField.get(chunk)
            ));
        }

        return result;
    }

    private static final class ChunkView {
        final String text;
        final Color color;
        final float offsetX;
        final int lineIndex;

        private ChunkView(String text, Color color, float offsetX, int lineIndex) {
            this.text = text;
            this.color = color;
            this.offsetX = offsetX;
            this.lineIndex = lineIndex;
        }
    }

    private static class TestableHighlightTextArea extends HighlightTextArea {
        public TestableHighlightTextArea(String text, VisTextFieldStyle style) {
            super(text, style);
        }

        public void callCalculateOffsets() {
            calculateOffsets();
        }
    }
}
