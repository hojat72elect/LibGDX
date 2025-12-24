
package com.talosvfx.talos.editor.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.talosvfx.talos.TalosMain;
import com.talosvfx.talos.editor.filesystem.FileChooserListener;
import com.talosvfx.talos.editor.filesystem.FileSystemInteraction;

public class SettingsDialog extends VisWindow {

    public static final String ASSET_PATH = "assetPath";

    private final ObjectMap<String, TextField> textFieldMap;

    public SettingsDialog() {
        super("Talos Preferences");

        textFieldMap = new ObjectMap<>();

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

        for (String key : textFieldMap.keys()) {
            textFieldMap.get(key).setText(TalosMain.Instance().Prefs().getString(key));
        }
    }

    public void addPathSetting(String name, final String id) {
        Table inputTable = new Table();

        inputTable.add(new Label(name, getSkin())).width(220);
        final TextField inputPathField = new TextField("", getSkin());
        inputPathField.setDisabled(true);
        inputTable.add(inputPathField).padLeft(13).width(270);
        TextButton browseInputBtn = new TextButton("Browse", getSkin());
        inputTable.add(browseInputBtn).padLeft(3);

        add(inputTable).pad(5).left().expandX().padTop(10);
        row();

        textFieldMap.put(id, inputPathField);

        browseInputBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showFolderSelect(id);
            }
        });
    }

    private void initContent() {
        addPathSetting("Particle Assets Default Path", ASSET_PATH);

        TextButton saveButton = new TextButton("Save", getSkin());
        add(saveButton).right().padRight(5);
        row();

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                save();
            }
        });
    }

    private void showFolderSelect(final String id) {
        FileSystemInteraction.instance().showFolderChooser(new FileChooserListener() {
            @Override
            public void selected(Array<FileHandle> files) {
                textFieldMap.get(id).setText(files.get(0).path());
            }
        });
    }

    private void save() {
        for (String key : textFieldMap.keys()) {
            TalosMain.Instance().Prefs().putString(key, textFieldMap.get(key).getText());
        }
        TalosMain.Instance().Prefs().flush();
        close();
    }
}
