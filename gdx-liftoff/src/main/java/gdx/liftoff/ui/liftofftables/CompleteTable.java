package gdx.liftoff.ui.liftofftables;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.targeting;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;
import static gdx.liftoff.Main.CLEAR_WHITE;
import static gdx.liftoff.Main.SPACE_HUGE;
import static gdx.liftoff.Main.SPACE_LARGE;
import static gdx.liftoff.Main.SPACE_MEDIUM;
import static gdx.liftoff.Main.generatingProject;
import static gdx.liftoff.Main.skin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import gdx.liftoff.ui.panels.CompleteButtonsPanel;
import gdx.liftoff.ui.panels.CompletePanel;
import gdx.liftoff.ui.panels.GeneratingPanel;

/**
 * The final table in the workflow. An animation begins by showing the generating panel first. Then it displays the
 * complete and complete buttons panels.
 */
public class CompleteTable extends LiftoffTable {
    private GeneratingPanel generatingPanel;
    private Table completeTable;
    private CompletePanel completePanel;

    public CompleteTable() {
        populate();
    }

    @Override
    public void populate() {
        clearChildren();
        setBackground(skin.getDrawable("black"));
        pad(SPACE_LARGE).padLeft(SPACE_HUGE).padRight(SPACE_HUGE);

        //generating panel
        generatingPanel = new GeneratingPanel(false);

        completeTable = new Table();
        stack(generatingPanel, completeTable);

        //complete panel
        completeTable.defaults().space(SPACE_MEDIUM);
        completePanel = new CompletePanel(false);
        completeTable.add(completePanel);

        //complete buttons panel
        completeTable.row();
        CompleteButtonsPanel completeButtonsPanel = new CompleteButtonsPanel(false);
        completeTable.add(completeButtonsPanel);

        //animation initial setup
        completeTable.setColor(CLEAR_WHITE);
        completeTable.setTouchable(Touchable.disabled);

        //animation
        addAction(sequence(
                delay(1f),
                new Action() {
                    @Override
                    public boolean act(float v) {
                        if (generatingProject) return false;
                        else {
                            completePanel.populate(false);
                            return true;
                        }
                    }
                },
                targeting(generatingPanel, fadeOut(.3f)),
                targeting(completeTable, fadeIn(.3f)),
                targeting(completeTable, touchable(Touchable.enabled))
        ));
    }

    @Override
    public void captureKeyboardFocus() {

    }

    @Override
    public void finishAnimation() {

    }

    public void showCompletePanel() {
        clearActions();
        completePanel.populate(false);
        generatingPanel.setColor(CLEAR_WHITE);
        completeTable.setColor(Color.WHITE);
        completeTable.setTouchable(Touchable.enabled);
    }
}
