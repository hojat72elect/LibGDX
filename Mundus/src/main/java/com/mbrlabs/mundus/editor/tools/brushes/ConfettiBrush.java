package com.mbrlabs.mundus.editor.tools.brushes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mbrlabs.mundus.editor.core.project.ProjectManager;
import com.mbrlabs.mundus.editor.history.CommandHistory;
import com.mbrlabs.mundus.editor.tools.picker.GameObjectPicker;
import com.mbrlabs.mundus.editor.utils.Fa;

import org.jetbrains.annotations.NotNull;

public class ConfettiBrush extends TerrainBrush {

    public ConfettiBrush(ProjectManager projectManager, CommandHistory history, GameObjectPicker goPicker) {
        super(projectManager, history, Gdx.files.internal("brushes/confetti.png"), goPicker);
    }

    @NotNull
    @Override
    public String getName() {
        return "Confetti brush";
    }

    @NotNull
    @Override
    public Drawable getIcon() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String getIconFont() {
        return Fa.Companion.getHASHTAG();
    }
}
