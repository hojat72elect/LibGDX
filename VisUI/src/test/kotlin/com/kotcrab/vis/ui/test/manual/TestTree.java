package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestTree extends VisWindow {

    public TestTree() {
        super("tree");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        if (TestApplication.USE_VIS_WIDGETS)
            addVisWidgets();
        else
            addNormalWidgets();

        setSize(150, 380);
        setPosition(774, 303);
    }

    private void addNormalWidgets() {
        Skin skin = VisUI.getSkin();

        Tree tree = new Tree(skin);
        TestNode item1 = new TestNode(new Label("item 1", skin));
        TestNode item2 = new TestNode(new Label("item 2", skin));
        TestNode item3 = new TestNode(new Label("item 3", skin));

        item1.add(new TestNode(new Label("item 1.1", skin)));
        item1.add(new TestNode(new Label("item 1.2", skin)));
        item1.add(new TestNode(new Label("item 1.3", skin)));

        item2.add(new TestNode(new Label("item 2.1", skin)));
        item2.add(new TestNode(new Label("item 2.2", skin)));
        item2.add(new TestNode(new Label("item 2.3", skin)));

        item3.add(new TestNode(new Label("item 3.1", skin)));
        item3.add(new TestNode(new Label("item 3.2", skin)));
        item3.add(new TestNode(new Label("item 3.3", skin)));

        item1.setExpanded(true);

        tree.add(item1);
        tree.add(item2);
        tree.add(item3);

        add(tree).expand().fill();
    }

    private void addVisWidgets() {
        VisTree tree = new VisTree();
        TestNode item1 = new TestNode(new VisLabel("item 1"));
        TestNode item2 = new TestNode(new VisLabel("item 2"));
        TestNode item3 = new TestNode(new VisLabel("item 3"));

        item1.add(new TestNode(new VisLabel("item 1.1")));
        item1.add(new TestNode(new VisLabel("item 1.2")));
        item1.add(new TestNode(new VisLabel("item 1.3")));

        item2.add(new TestNode(new VisLabel("item 2.1")));
        item2.add(new TestNode(new VisLabel("item 2.2")));
        item2.add(new TestNode(new VisLabel("item 2.3")));

        item3.add(new TestNode(new VisLabel("item 3.1")));
        item3.add(new TestNode(new VisLabel("item 3.2")));
        item3.add(new TestNode(new VisLabel("item 3.3")));

        item1.setExpanded(true);

        tree.add(item1);
        tree.add(item2);
        tree.add(item3);

        add(tree).expand().fill();
    }

    static class TestNode extends Tree.Node {
        public TestNode(Actor actor) {
            super(actor);
        }
    }
}
