
package com.talosvfx.talos.editor.widgets.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.talosvfx.talos.editor.addons.scene.SceneEditorWorkspace;

public class SearchFilteredTree<T> extends Table {
    private static final float AUTO_SCROLL_RANGE = 40;
    private static final float AUTO_SCROLL_SPEED = 500;

    public TextField textField;
    public ScrollPane scrollPane;
    private final FilteredTree<T> filteredTree;
    private boolean autoSelect = true;

    private final Table searchTable;

    private final Vector2 tmp = new Vector2();

    public SearchFilteredTree(Skin skin, final FilteredTree<T> tree, final TextField.TextFieldFilter filter) {

        searchTable = new Table();
        Image image = new Image(skin.newDrawable("search"));

        textField = new TextField("", skin);
        if (filter != null) {
            textField.setTextFieldFilter(filter);
        }

        searchTable.add(image);
        searchTable.add(textField).growX().padLeft(5);

        filteredTree = tree;
        filteredTree.setSearchFilteredTree(this);

        add(searchTable).growX();
        row();
        scrollPane = new ScrollPane(filteredTree, skin, "list");
        add(scrollPane).grow();

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tree.smartFilter(textField.getText(), autoSelect);
                tree.invalidateHierarchy();
                tree.invalidate();
                tree.layout();
            }
        });

        textField.addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (SceneEditorWorkspace.isEnterPressed(keycode)) {
                    filteredTree.reportUserEnter();
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    public void reset() {
        textField.setText("");
        filteredTree.filter(textField.getText());
        filteredTree.invalidateHierarchy();
        filteredTree.invalidate();
        filteredTree.layout();
    }

    public void setPad(float padTop, float padLeft, float padBottom, float padRight) {
        Cell<TextField> textFieldCell = searchTable.getCell(textField);
        textFieldCell.pad(padTop, padLeft, padBottom, padRight);
    }

    public void setAutoSelect(boolean autoSelect) {
        this.autoSelect = autoSelect;
    }

    public void onItemHold() {
        tmp.set(Gdx.input.getX(), Gdx.input.getY());
        scrollPane.screenToLocalCoordinates(tmp);

        if (isInTopZone(tmp.x, tmp.y)) {
            scrollPane.setScrollY(scrollPane.getScrollY() - AUTO_SCROLL_SPEED * Gdx.graphics.getDeltaTime());
        } else if (isInBottomZone(tmp.x, tmp.y)) {
            scrollPane.setScrollY(scrollPane.getScrollY() + AUTO_SCROLL_SPEED * Gdx.graphics.getDeltaTime());
        }
    }

    private boolean isInTopZone(float localX, float localY) {
        return localY < scrollPane.getHeight() && localY > scrollPane.getHeight() - AUTO_SCROLL_RANGE;
    }

    private boolean isInBottomZone(float localX, float localY) {
        return localY > 0 && localY < AUTO_SCROLL_RANGE;
    }
}
