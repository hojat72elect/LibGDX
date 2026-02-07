package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.util.highlight.BaseHighlighter;
import com.kotcrab.vis.ui.util.highlight.Highlight;
import com.kotcrab.vis.ui.util.highlight.Highlighter;

/**
 * Text area implementation supporting highlighting words and scrolling in both X and Y directions.
 * <p>
 * For best scroll pane settings you should create scroll pane using {@link #createCompatibleScrollPane()}.
 * <p>
 * Note about overlapping highlights: this text area can handle overlapping highlights, highlights that starts earlier
 * have higher priority. If two highlights have the exactly the same start point, then it is undefined which highlight
 * will be used and depends on how array containing highlights will be sorted.
 *
 * @see Highlighter
 * @since 1.1.2
 */
public class HighlightTextArea extends ScrollableTextArea {
    private final Array<Highlight> highlights = new Array<>();
    private final Array<Chunk> renderChunks = new Array<>();
    private boolean chunkUpdateScheduled = true;
    private final Color defaultColor = Color.WHITE;

    private BaseHighlighter highlighter;

    private float maxAreaWidth = 0;
    private float maxAreaHeight = 0;

    public HighlightTextArea(String text) {
        super(text);
        init();
    }

    public HighlightTextArea(String text, String styleName) {
        super(text, styleName);
        init();
    }

    public HighlightTextArea(String text, VisTextFieldStyle style) {
        super(text, style);
        init();
    }

    private void init() {
        softwrap = false;
    }

    @Override
    void updateDisplayText() {
        super.updateDisplayText();
        processHighlighter();
    }

    @Override
    protected void calculateOffsets() {
        super.calculateOffsets();
        if (!chunkUpdateScheduled) return;
        chunkUpdateScheduled = false;
        highlights.sort();
        renderChunks.clear();

        String text = getText();

        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        boolean carryHighlight = false;
        for (int lineIdx = 0, highlightIdx = 0; lineIdx < linesBreak.size; lineIdx += 2) {
            int lineStart = linesBreak.items[lineIdx];
            int lineEnd = linesBreak.items[lineIdx + 1];
            int lineProgress = lineStart;
            float chunkOffset = 0;

            while (highlightIdx < highlights.size) {
                Highlight highlight = highlights.get(highlightIdx);
                if (highlight.start() > lineEnd) {
                    break;
                }

                if (highlight.start() == lineProgress || carryHighlight) {
                    renderChunks.add(new Chunk(text.substring(lineProgress, Math.min(highlight.end(), lineEnd)), highlight.color(), chunkOffset, lineIdx));
                    lineProgress = Math.min(highlight.end(), lineEnd);

                    if (highlight.end() > lineEnd) {
                        carryHighlight = true;
                    } else {
                        carryHighlight = false;
                        highlightIdx++;
                    }
                } else {
                    //protect against overlapping highlights
                    boolean noMatch = false;
                    while (highlight.start() <= lineProgress) {
                        highlightIdx++;
                        if (highlightIdx >= highlights.size) {
                            noMatch = true;
                            break;
                        }
                        highlight = highlights.get(highlightIdx);
                        if (highlight.start() > lineEnd) {
                            noMatch = true;
                            break;
                        }
                    }
                    if (noMatch) break;
                    renderChunks.add(new Chunk(text.substring(lineProgress, highlight.start()), defaultColor, chunkOffset, lineIdx));
                    lineProgress = highlight.start();
                }

                Chunk chunk = renderChunks.peek();
                layout.setText(style.font, chunk.text);
                chunkOffset += layout.width;
                //current highlight needs to be applied to next line meaning that there is no other highlights that can be applied to currently parsed line
                if (carryHighlight) break;
            }

            if (lineProgress < lineEnd) {
                renderChunks.add(new Chunk(text.substring(lineProgress, lineEnd), defaultColor, chunkOffset, lineIdx));
            }
        }

        maxAreaWidth = 0;
        for (String line : text.split("\\n")) {
            layout.setText(style.font, line);
            maxAreaWidth = Math.max(maxAreaWidth, layout.width + 30);
        }

        layoutPool.free(layout);
        updateScrollLayout();
    }

    @Override
    protected void drawText(Batch batch, BitmapFont font, float x, float y) {
        maxAreaHeight = 0;
        float offsetY = 0;
        float parentAlpha = font.getColor().a;
        for (int i = firstLineShowing * 2; i < (firstLineShowing + linesShowing) * 2 && i < linesBreak.size; i += 2) {
            for (Chunk chunk : renderChunks) {
                if (chunk.lineIndex == i) {
                    font.setColor(chunk.color);
                    font.getColor().a *= parentAlpha;
                    font.draw(batch, chunk.text, x + chunk.offsetX, y + offsetY);
                }
            }

            offsetY -= font.getLineHeight();
            maxAreaHeight += font.getLineHeight();
        }

        maxAreaHeight += 30;
    }

    /**
     * Processes highlighter rules, collects created highlights and schedules text area displayed text update. This should be called
     * after highlighter rules has changed to update highlights.
     */
    public void processHighlighter() {
        if (highlights == null) return;
        highlights.clear();
        if (highlighter != null) highlighter.process(this, highlights);
        chunkUpdateScheduled = true;
    }

    public BaseHighlighter getHighlighter() {
        return highlighter;
    }

    /**
     * Changes highlighter of text area. Note that you don't have to call {@link #processHighlighter()} after changing
     * highlighter - you only have to call it when highlighter rules has changed.
     */
    public void setHighlighter(BaseHighlighter highlighter) {
        this.highlighter = highlighter;
        processHighlighter();
    }

    @Override
    public float getPrefWidth() {
        return maxAreaWidth + 5;
    }

    @Override
    public float getPrefHeight() {
        return maxAreaHeight + 5;
    }

    @Override
    public ScrollPane createCompatibleScrollPane() {
        ScrollPane scrollPane = super.createCompatibleScrollPane();
        scrollPane.setScrollingDisabled(false, false);
        return scrollPane;
    }

    private static class Chunk {
        String text;
        Color color;
        float offsetX;
        int lineIndex;

        public Chunk(String text, Color color, float offsetX, int lineIndex) {
            this.text = text;
            this.color = color;
            this.offsetX = offsetX;
            this.lineIndex = lineIndex;
        }
    }
}
