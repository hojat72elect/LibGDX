package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.ButtonBar;
import com.kotcrab.vis.ui.widget.ButtonBar.ButtonType;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestButtonBar extends VisWindow {

    public TestButtonBar() {
        super("buttonbar");

        addCloseButton();
        closeOnEscape();

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        add(new VisLabel("Windows: "));
        add(createTable(ButtonBar.WINDOWS_ORDER)).expand().fill();
        row();

        add(new VisLabel("Linux: "));
        add(createTable(ButtonBar.LINUX_ORDER)).expand().fill();
        row();

        add(new VisLabel("Mac: "));
        add(createTable(ButtonBar.OSX_ORDER)).expand().fill();
        row();

        pack();
        setPosition(300, 245);
    }

    private VisTable createTable(String order) {
        ButtonBar buttonBar = new ButtonBar(order);

        ChangeListener dummyListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        };

        buttonBar.setButton(ButtonType.LEFT, dummyListener);
        buttonBar.setButton(ButtonType.RIGHT, dummyListener);
        buttonBar.setButton(ButtonType.HELP, dummyListener);
        buttonBar.setButton(ButtonType.NO, dummyListener);
        buttonBar.setButton(ButtonType.YES, dummyListener);
        buttonBar.setButton(ButtonType.CANCEL, dummyListener);
        buttonBar.setButton(ButtonType.BACK, dummyListener);
        buttonBar.setButton(ButtonType.NEXT, dummyListener);
        buttonBar.setButton(ButtonType.APPLY, dummyListener);
        buttonBar.setButton(ButtonType.FINISH, dummyListener);
        buttonBar.setButton(ButtonType.OK, dummyListener);
        return buttonBar.createTable();
    }
}
