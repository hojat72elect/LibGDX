package com.kotcrab.vis.ui.widget.file.internal;

import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.SORT_BY_ASCENDING;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.SORT_BY_DATE;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.SORT_BY_DESCENDING;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.SORT_BY_NAME;
import static com.kotcrab.vis.ui.widget.file.internal.FileChooserText.SORT_BY_SIZE;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class SortingPopupMenu extends PopupMenu {
    private final FileChooser chooser;
    private final Drawable selectedMenuItem;

    private final MenuItem sortByName;
    private final MenuItem sortByDate;
    private final MenuItem sortBySize;
    private final MenuItem sortByAscending;
    private final MenuItem sortByDescending;

    private final Image sortByNameImage;
    private final Image sortByDateImage;
    private final Image sortBySizeImage;
    private final Image sortByAscendingImage;
    private final Image sortByDescendingImage;

    public SortingPopupMenu(final FileChooser chooser) {
        selectedMenuItem = chooser.getChooserStyle().contextMenuSelectedItem;
        this.chooser = chooser;

        addItem(sortByName = new MenuItem(SORT_BY_NAME.get(), selectedMenuItem, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setSorting(FileChooser.FileSorting.NAME, true);
            }
        }));
        addItem(sortByDate = new MenuItem(SORT_BY_DATE.get(), selectedMenuItem, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setSorting(FileChooser.FileSorting.MODIFIED_DATE, false);
            }
        }));
        addItem(sortBySize = new MenuItem(SORT_BY_SIZE.get(), selectedMenuItem, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setSorting(FileChooser.FileSorting.SIZE, true);
            }
        }));

        addSeparator();

        addItem(sortByAscending = new MenuItem(SORT_BY_ASCENDING.get(), selectedMenuItem, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setSortingOrderAscending(true);
            }
        }));
        addItem(sortByDescending = new MenuItem(SORT_BY_DESCENDING.get(), selectedMenuItem, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setSortingOrderAscending(false);
            }
        }));

        sortByNameImage = sortByName.getImage();
        sortByDateImage = sortByDate.getImage();
        sortBySizeImage = sortBySize.getImage();
        sortByAscendingImage = sortByAscending.getImage();
        sortByDescendingImage = sortByDescending.getImage();

        sortByNameImage.setScaling(Scaling.none);
        sortByDateImage.setScaling(Scaling.none);
        sortBySizeImage.setScaling(Scaling.none);
        sortByAscendingImage.setScaling(Scaling.none);
        sortByDescendingImage.setScaling(Scaling.none);
    }

    public void build() {
        sortByNameImage.setDrawable(null);
        sortByDateImage.setDrawable(null);
        sortBySizeImage.setDrawable(null);
        sortByAscendingImage.setDrawable(null);
        sortByDescendingImage.setDrawable(null);
        switch (chooser.getSorting()) {
            case NAME:
                sortByNameImage.setDrawable(selectedMenuItem);
                break;
            case MODIFIED_DATE:
                sortByDateImage.setDrawable(selectedMenuItem);
                break;
            case SIZE:
                sortBySizeImage.setDrawable(selectedMenuItem);
                break;
        }

        if (chooser.isSortingOrderAscending()) {
            sortByAscendingImage.setDrawable(selectedMenuItem);
        } else {
            sortByDescendingImage.setDrawable(selectedMenuItem);
        }
    }
}
