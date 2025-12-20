package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestCollapsible extends VisWindow {

    public TestCollapsible() {
        super("collapsiblewidget");

        columnDefaults(0).left();

        addVisComponents();

        setPosition(955, 418);
        pack();
    }

    private void addVisComponents() {
        VisCheckBox collapseCheckBox = new VisCheckBox("show advanced settings");
        collapseCheckBox.setChecked(true);

        VisTable table = new VisTable();
        final CollapsibleWidget collapsibleWidget = new CollapsibleWidget(table);

        VisTable numberTable = new VisTable(true);
        numberTable.add(new VisLabel("2 + 2 * 2 = "));
        numberTable.add(new VisTextField());

        table.defaults().left();
        table.defaults().padLeft(10);
        table.add(new VisCheckBox("advanced option #1")).row();
        table.add(new VisCheckBox("advanced option #2")).row();
        table.add(numberTable).padTop(3).row();

        collapseCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
            }
        });

        VisTable notAdvancedTable = new VisTable(true);

        VisTextButton dummyButton = new VisTextButton("button");

        notAdvancedTable.defaults().left();
        notAdvancedTable.add(new VisLabel("less advanced settings")).expandX().fillX().row();
        notAdvancedTable.add(new VisCheckBox("option #1")).row();
        notAdvancedTable.add(new VisCheckBox("option #2")).row();
        notAdvancedTable.add(dummyButton);

        add(collapseCheckBox).row();
        add(collapsibleWidget).expandX().fillX().row();
        add(new Separator()).padTop(10).fillX().expandX().row();
        add(notAdvancedTable).expandX().fillX().padTop(5).row();
        add().expand().fill().padBottom(3);

        dummyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialogs.showOKDialog(getStage(), "message", "this button is for showcase only, please don't press");
            }
        });
    }
}
