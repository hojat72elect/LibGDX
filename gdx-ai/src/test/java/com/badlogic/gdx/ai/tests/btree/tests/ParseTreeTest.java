package com.badlogic.gdx.ai.tests.btree.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.Reader;

/**
 * A simple test to demonstrate behavior tree parsing.
 */
public class ParseTreeTest extends BehaviorTreeTestBase {

    private BehaviorTreeViewer<?> treeViewer;

    public ParseTreeTest() {
        super("Parse Tree");
    }

    @Override
    public Actor createActor(Skin skin) {
        Reader reader = null;
        try {
            reader = Gdx.files.internal("data/dog.tree").reader();
            BehaviorTreeParser<Dog> parser = new BehaviorTreeParser<Dog>(BehaviorTreeParser.DEBUG_HIGH);
            BehaviorTree<Dog> tree = parser.parse(reader, new Dog("Buddy"));
            treeViewer = createTreeViewer(tree.getObject().name, tree, true, skin);

            return new ScrollPane(treeViewer, skin);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }
}
