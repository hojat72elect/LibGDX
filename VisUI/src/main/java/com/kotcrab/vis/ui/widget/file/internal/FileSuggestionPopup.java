package com.kotcrab.vis.ui.widget.file.internal;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

public class FileSuggestionPopup extends AbstractSuggestionPopup {
    public FileSuggestionPopup(FileChooser chooser) {
        super(chooser);
    }

    public void pathFieldKeyTyped(Stage stage, Array<FileHandle> files, VisTextField pathField) {
        if (pathField.getText().length() == 0) {
            remove();
            return;
        }

        int suggestions = createSuggestions(files, pathField);
        if (suggestions == 0) {
            remove();
            return;
        }

        showMenu(stage, pathField);
    }

    private int createSuggestions(Array<FileHandle> files, final VisTextField fileNameField) {
        clearChildren();
        int suggestions = 0;
        for (final FileHandle file : files) {
            if (file.name().startsWith(fileNameField.getText()) && !file.name().equals(fileNameField.getText())) {
                MenuItem item = createMenuItem(getTrimmedName(file.name()));
                item.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        chooser.highlightFiles(file);
                    }
                });
                addItem(item);
                suggestions++;
            }

            if (suggestions == MAX_SUGGESTIONS) {
                break;
            }
        }

        if (chooser.getMode() == FileChooser.Mode.SAVE && suggestions == 0 //don't show matches when there are files that actually exist and matched previous search
                && chooser.getActiveFileTypeFilterRule() != null && fileNameField.getText().matches(".*\\.")) {
            FileTypeFilter.Rule rule = chooser.getActiveFileTypeFilterRule();

            for (String extension : rule.getExtensions()) {
                MenuItem item = createMenuItem(fileNameField.getText() + extension);
                final String arbitraryPath = fileNameField.getText() + extension;
                item.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fileNameField.setText(arbitraryPath);
                        fileNameField.setCursorAtTextEnd();
                    }
                });
                addItem(item);
                suggestions++;
            }
        }

        return suggestions;
    }

    private String getTrimmedName(String name) {
        if (name.length() > 40) {
            return name.substring(0, 40) + "...";
        } else {
            return name;
        }
    }
}
