package com.mbrlabs.mundus.editor.tools.brushes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mbrlabs.mundus.editor.core.project.ProjectManager;
import com.mbrlabs.mundus.editor.history.CommandHistory;
import com.mbrlabs.mundus.editor.tools.picker.GameObjectPicker;
import com.mbrlabs.mundus.editor.utils.Fa;

import org.jetbrains.annotations.NotNull;

public class StarBrush extends TerrainBrush {

    public StarBrush(ProjectManager projectManager, CommandHistory history, GameObjectPicker goPicker) {
        super(projectManager, history, Gdx.files.internal("brushes/star.png"), goPicker);
    }

    @NotNull
    @Override
    public String getName() {
        return "Star brush";
    }

    @NotNull
    @Override
    public Drawable getIcon() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String getIconFont() {
        return Fa.Companion.getSTAR();
    }
}
