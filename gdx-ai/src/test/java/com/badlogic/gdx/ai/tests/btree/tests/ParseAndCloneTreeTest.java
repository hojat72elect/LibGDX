package com.badlogic.gdx.ai.tests.btree.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.TaskCloner;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.btree.utils.DistributionAdapters;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.KryoUtils;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.Reader;

/**
 * A simple test to demonstrate behavior tree cloning capabilities.
 */
public class ParseAndCloneTreeTest extends BehaviorTreeTestBase {

    boolean useKryo;

    public ParseAndCloneTreeTest(boolean useKryo) {
        super("Parse and Clone Tree (" + (useKryo ? "Kryo" : "no") + " cloner)");
        this.useKryo = useKryo;
    }

    @Override
    public Actor createActor(Skin skin) {
        Reader reader = null;
        try {
            // Parse
            reader = Gdx.files.internal("data/dog.tree").reader();
            BehaviorTreeParser<Dog> parser = new BehaviorTreeParser<Dog>(new DistributionAdapters(), BehaviorTreeParser.DEBUG_HIGH,
                    new BehaviorTreeParser.DefaultBehaviorTreeReader<Dog>(true) {
                        @Override
                        public void comment(String text) {
                            System.out.println("<<<<<" + text + ">>>>>");
                        }
                    });
            BehaviorTree<Dog> treeArchetype = parser.parse(reader, null);

            // Clone
            Task.TASK_CLONER = !useKryo ? null : new TaskCloner() {
                @Override
                public <T> Task<T> cloneTask(Task<T> task) {
                    return KryoUtils.copy(task);
                }

                @Override
                public <T> void freeTask(Task<T> task) {
                }
            };
            BehaviorTree<Dog> tree = (BehaviorTree<Dog>) treeArchetype.cloneTask();
            tree.setObject(new Dog("Cloned Buddy"));
            BehaviorTreeViewer<?> treeViewer = createTreeViewer(tree.getObject().name, tree, true, skin);

            return new ScrollPane(treeViewer, skin);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        Task.TASK_CLONER = null;
    }
}
