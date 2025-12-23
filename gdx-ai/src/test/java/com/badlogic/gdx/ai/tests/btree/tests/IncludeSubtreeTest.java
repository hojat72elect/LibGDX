package com.badlogic.gdx.ai.tests.btree.tests;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A simple test to demonstrate subtree inclusion both eager (at clone-time) and lazy (at run-time).
 */
public class IncludeSubtreeTest extends BehaviorTreeTestBase {

    private final boolean lazy;

    public IncludeSubtreeTest(boolean lazy) {
        super("Include Subtree" + (lazy ? " Lazily" : ""));
        this.lazy = lazy;
    }

    @Override
    public Actor createActor(Skin skin) {
        BehaviorTreeLibraryManager libraryManager = BehaviorTreeLibraryManager.getInstance();
        libraryManager.setLibrary(new BehaviorTreeLibrary(BehaviorTreeParser.DEBUG_HIGH));

        String name = lazy ? "data/dogIncludeLazy.tree" : "data/dogInclude.tree";
        BehaviorTree<Dog> tree = libraryManager.createBehaviorTree(name, new Dog("Buddy"));
        BehaviorTreeViewer<?> treeViewer = createTreeViewer(tree.getObject().name, tree, true, skin);

        return new ScrollPane(treeViewer, skin);
    }
}
