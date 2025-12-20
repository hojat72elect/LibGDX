package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.ScrollableTextArea;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestTextAreaAndScroll extends VisWindow {

    public TestTextAreaAndScroll() {
        super("textarea / scrollpane");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        if (TestApplication.USE_VIS_WIDGETS)
            addVisWidgets();
        else
            addNormalWidgets();

        setResizable(true);
        setSize(180, 380);
        setPosition(28, 300);
    }

    private void addNormalWidgets() {
        Skin skin = VisUI.getSkin();

        TextArea textArea = new TextArea("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec iaculis odio.", skin);
        textArea.setPrefRows(5);

        // ---

        VisTable table = new VisTable();

        for (int i = 0; i < 20; i++)
            table.add(new Label("Label #" + (i + 1), skin)).expand().fill().row();

        ScrollPane scrollPane = new ScrollPane(table, skin, "list");
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);

        // ---

        add(textArea).row();
        add(scrollPane).spaceTop(8).fillX().expandX().row();
    }

    private void addVisWidgets() {
        ScrollableTextArea textArea = new ScrollableTextArea("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec iaculis odio.\nFun thing: This text area supports scrolling.");

        // ---

        VisTable table = new VisTable();

        for (int i = 0; i < 20; i++)
            table.add(new VisLabel("Label #" + (i + 1))).expand().fill().row();

        VisScrollPane scrollPane = new VisScrollPane(table);
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);

        // ---

        add(textArea.createCompatibleScrollPane()).growX().height(100).row();
        add(scrollPane).spaceTop(8).growX().row();
    }
}
