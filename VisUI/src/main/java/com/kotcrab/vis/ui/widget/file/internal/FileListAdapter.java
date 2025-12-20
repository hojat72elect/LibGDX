package com.kotcrab.vis.ui.widget.file.internal;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class FileListAdapter extends ArrayAdapter<FileHandle, FileChooser.FileItem> {
    private final FileChooser chooser;
    private final Array<FileChooser.FileItem> orderedViews = new Array<FileChooser.FileItem>();
    private final GridGroup gridGroup;

    public FileListAdapter(FileChooser chooser, Array<FileHandle> files) {
        super(files);
        this.chooser = chooser;
        gridGroup = new GridGroup(128f, 2f);
    }

    @Override
    protected FileChooser.FileItem createView(FileHandle item) {
        return chooser.new FileItem(item, chooser.getViewMode());
    }

    @Override
    public void fillTable(VisTable itemsTable) {
        getViews().clear(); //clear cache
        orderedViews.clear();
        gridGroup.clear();

        if (getItemsSorter() != null) sort(getItemsSorter());

        FileChooser.ViewMode viewMode = chooser.getViewMode();

        if (viewMode.isGridMode()) {
            viewMode.setupGridGroup(chooser.getSizes(), gridGroup);
            for (final FileHandle item : iterable()) {
                final FileChooser.FileItem view = getView(item);
                orderedViews.add(view);
                prepareViewBeforeAddingToTable(item, view);
                gridGroup.addActor(view);
            }

            itemsTable.add(gridGroup).growX().minWidth(0);
        } else {
            for (final FileHandle item : iterable()) {
                final FileChooser.FileItem view = getView(item);
                orderedViews.add(view);
                prepareViewBeforeAddingToTable(item, view);
                itemsTable.add(view).growX();
                itemsTable.row();
            }
        }
    }

    @Override
    public ObjectMap<FileHandle, FileChooser.FileItem> getViews() {
        return super.getViews();
    }

    public Array<FileChooser.FileItem> getOrderedViews() {
        return orderedViews;
    }
}
