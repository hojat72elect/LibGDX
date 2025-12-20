package com.kotcrab.vis.ui.widget.file.internal;

import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_ADD_TO_FAVORITES;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_DELETE;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_MOVE_TO_TRASH;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_NEW_DIRECTORY;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_REFRESH;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_REMOVE_FROM_FAVORITES;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_SHOW_IN_EXPLORER;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.CONTEXT_MENU_SORT_BY;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserStyle;
import com.kotcrab.vis.ui.widget.file.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilePopupMenu extends PopupMenu {
    private final FileChooserStyle style;

    private final SortingPopupMenu sortingPopupMenu;

    private FileHandle file;

    private final MenuItem delete;
    private final MenuItem newDirectory;
    private final MenuItem showInExplorer;
    private final MenuItem refresh;
    private final MenuItem addToFavorites;
    private final MenuItem removeFromFavorites;
    private final MenuItem sortBy;

    public FilePopupMenu(final FileChooser chooser, final FilePopupMenuCallback callback) {
        super(chooser.getChooserStyle().popupMenuStyle);
        this.style = chooser.getChooserStyle();

        sortingPopupMenu = new SortingPopupMenu(chooser);

        delete = new MenuItem(CONTEXT_MENU_DELETE.get(), style.iconTrash);
        newDirectory = new MenuItem(CONTEXT_MENU_NEW_DIRECTORY.get(), style.iconFolderNew);
        showInExplorer = new MenuItem(CONTEXT_MENU_SHOW_IN_EXPLORER.get());
        refresh = new MenuItem(CONTEXT_MENU_REFRESH.get(), style.iconRefresh);
        addToFavorites = new MenuItem(CONTEXT_MENU_ADD_TO_FAVORITES.get(), style.iconFolderStar);
        removeFromFavorites = new MenuItem(CONTEXT_MENU_REMOVE_FROM_FAVORITES.get(), style.iconFolderStar);
        sortBy = new MenuItem(CONTEXT_MENU_SORT_BY.get());
        sortBy.setSubMenu(sortingPopupMenu);

        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.showFileDelDialog(file);
            }
        });

        newDirectory.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.showNewDirDialog();
            }
        });

        showInExplorer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    FileUtils.showDirInExplorer(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        refresh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.refresh();
            }
        });

        addToFavorites.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chooser.addFavorite(file);
            }
        });

        removeFromFavorites.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chooser.removeFavorite(file);
            }
        });
    }

    public void build() {
        sortingPopupMenu.build();
        clearChildren();
        addItem(newDirectory);
        addItem(sortBy);
        addItem(refresh);
    }

    public void build(Array<FileHandle> favorites, FileHandle file) {
        sortingPopupMenu.build();
        this.file = file;

        clearChildren();

        addItem(newDirectory);
        addItem(sortBy);
        addItem(refresh);
        addSeparator();

        if (file.type() == FileType.Absolute || file.type() == FileType.External) addItem(delete);

        if (file.type() == FileType.Absolute) {
            addItem(showInExplorer);

            if (file.isDirectory()) {
                if (favorites.contains(file, false))
                    addItem(removeFromFavorites);
                else
                    addItem(addToFavorites);
            }
        }
    }

    public void buildForFavorite(Array<FileHandle> favorites, File file) {
        this.file = Gdx.files.absolute(file.getAbsolutePath());

        clearChildren();

        addItem(showInExplorer);

        if (favorites.contains(this.file, false)) addItem(removeFromFavorites);
    }

    public boolean isAddedToStage() {
        return getStage() != null;
    }

    public void fileDeleterChanged(boolean trashAvailable) {
        delete.setText(trashAvailable ? CONTEXT_MENU_MOVE_TO_TRASH.get() : CONTEXT_MENU_DELETE.get());
    }

    public interface FilePopupMenuCallback {
        void showNewDirDialog();

        void showFileDelDialog(FileHandle file);
    }
}
