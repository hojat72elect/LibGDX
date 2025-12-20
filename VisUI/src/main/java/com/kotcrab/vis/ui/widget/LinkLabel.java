package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.CursorManager;

/**
 * Simple LinkLabel allows to create label with clickable link and underline on mouse over. Link can have custom text.
 * By default clicking link will open it in default browser, this can be changed by settings label listener.
 *
 * @since 0.7.2
 */
public class LinkLabel extends VisLabel {
    static private final Color tempColor = new Color();

    private LinkLabelStyle style;
    private ClickListener clickListener;

    private LinkLabelListener listener;
    private CharSequence url;

    public LinkLabel(CharSequence url) {
        super(url, VisUI.getSkin().get(LinkLabelStyle.class));
        init(url);
    }

    public LinkLabel(CharSequence text, CharSequence url) {
        super(text, VisUI.getSkin().get(LinkLabelStyle.class));
        init(url);
    }

    public LinkLabel(CharSequence text, int alignment) {
        super(text, VisUI.getSkin().get(LinkLabelStyle.class));
        setAlignment(alignment);
        init(text);
    }

    public LinkLabel(CharSequence text, Color textColor) {
        super(text, VisUI.getSkin().get(LinkLabelStyle.class));
        setColor(textColor);
        init(text);
    }

    public LinkLabel(CharSequence text, LinkLabelStyle style) {
        super(text, style);
        init(text);
    }

    public LinkLabel(CharSequence text, CharSequence url, String styleName) {
        super(text, VisUI.getSkin().get(styleName, LinkLabelStyle.class));
        init(url);
    }

    public LinkLabel(CharSequence text, CharSequence url, LinkLabelStyle style) {
        super(text, style);
        init(url);
    }

    public LinkLabel(CharSequence text, String fontName, Color color) {
        super(text, new LinkLabelStyle(VisUI.getSkin().getFont(fontName), color, VisUI.getSkin().getDrawable("white")));
        init(text);
    }

    @Override
    public LinkLabelStyle getStyle() {
        return (LinkLabelStyle) super.getStyle();
    }

    private void init(CharSequence linkUrl) {
        this.url = linkUrl;
        style = getStyle();

        addListener(clickListener = new ClickListener(Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (listener == null) {
                    Gdx.net.openURI(url.toString());
                } else {
                    listener.clicked(url.toString());
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                Gdx.graphics.setSystemCursor(SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                CursorManager.restoreDefaultCursor();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Drawable underline = style.underline;
        if (underline != null && clickListener.isOver()) {
            Color color = tempColor.set(getColor());
            color.a *= parentAlpha;
            if (style.fontColor != null) color.mul(style.fontColor);
            batch.setColor(color);
            underline.draw(batch, getX(), getY(), getWidth(), 1);
        }
    }

    public CharSequence getUrl() {
        return url;
    }

    public void setUrl(CharSequence url) {
        this.url = url;
    }

    public LinkLabelListener getListener() {
        return listener;
    }

    public void setListener(LinkLabelListener listener) {
        this.listener = listener;
    }

    public interface LinkLabelListener {
        void clicked(String url);
    }

    public static class LinkLabelStyle extends LabelStyle {
        /**
         * Optional
         */
        public Drawable underline;

        public LinkLabelStyle() {
        }

        public LinkLabelStyle(BitmapFont font, Color fontColor, Drawable underline) {
            super(font, fontColor);
            this.underline = underline;
        }

        public LinkLabelStyle(LinkLabelStyle style) {
            super(style);
            this.underline = style.underline;
        }
    }
}
