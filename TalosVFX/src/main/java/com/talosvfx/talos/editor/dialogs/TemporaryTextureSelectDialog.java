
package com.talosvfx.talos.editor.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.talosvfx.talos.editor.project2.SharedResources;

import lombok.Getter;
import lombok.Setter;

public class TemporaryTextureSelectDialog extends VisWindow {

    private TextureSelection selected;
    @Setter
    private OnTextureSelected listener;

    public TemporaryTextureSelectDialog() {
        super("Select Texture");

        setCenterOnAdd(true);
        setModal(true);
        setResizable(true);
        setMovable(true);
        addCloseButton();
        closeOnEscape();

        initContent();

        pack();
        invalidate();

        centerWindow();
    }

    private void initContent() {

        Array<TextureSelection> selection = new Array<>();

        Table subTable = new Table();

        String[] inbuilt = new String[]{
                "fire.png",
                "spot.png",
                "smoke.png"
        };

        int elementsPerRow = 4;
        int counter = 0;
        for (int i = 0; i < inbuilt.length; i++) {
            final TextureSelection textureSelection = new TextureSelection(inbuilt[i]);
            selection.add(textureSelection);
            textureSelection.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selected = textureSelection;

                    for (TextureSelection tex : selection) {
                        tex.setSelected(tex == textureSelection);
                    }
                }
            });

            subTable.add(textureSelection).size(100);

            counter++;
            if (counter > elementsPerRow) {
                counter = 0;
                subTable.row();
            }
        }

        add(subTable);
        row();

        TextButton saveButton = new TextButton("Select", getSkin());
        add(saveButton).right().padRight(5);
        row();

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (selected != null) {
                    listener.onSelected(selected);
                    selected.setSelected(false);
                    selected = null;
                }
                close();
            }
        });
    }

    public interface OnTextureSelected {
        void onSelected(TextureSelection textureSelection);
    }

    public static class TextureSelection extends Table {

        private final Image selected;
        @Getter
        private final Texture texture;
        @Getter
        private final String internalAssetPath;

        public TextureSelection(String internalAssetPath) {
            this.internalAssetPath = internalAssetPath;
            setBackground(SharedResources.skin.newDrawable("white", 0, 0, 0, 0.9f));

            texture = new Texture(Gdx.files.internal(internalAssetPath));
            final Image image = new Image(texture);
            selected = new Image(SharedResources.skin.newDrawable("white", 1f, 1f, 1f, 1f));
            this.selected.setColor(1f, 1f, 1f, 0);

            add(new Stack(image, selected)).grow();
        }

        public void setSelected(boolean selected) {
            this.selected.setColor(1f, 1f, 1f, selected ? 0.2f : 0);
        }
    }
}
