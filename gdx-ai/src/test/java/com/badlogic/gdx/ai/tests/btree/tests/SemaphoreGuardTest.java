package com.badlogic.gdx.ai.tests.btree.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.decorator.SemaphoreGuard;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.ai.utils.NonBlockingSemaphoreRepository;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.Reader;

/**
 * A simple test to demonstrate the {@link SemaphoreGuard} task.
 */
public class SemaphoreGuardTest extends BehaviorTreeTestBase {

    public SemaphoreGuardTest() {
        super("Semaphore Guard", "When Buddy walks Snoopy barks and vice versa");
    }

    @Override
    public Actor createActor(Skin skin) {
        // Create the semaphore
        NonBlockingSemaphoreRepository.clear();
        NonBlockingSemaphoreRepository.addSemaphore("dogSemaphore", 1);

        Reader reader = null;
        try {
            // Parse Buddy's tree
            reader = Gdx.files.internal("data/dogSemaphore.tree").reader();
            BehaviorTreeParser<Dog> parser = new BehaviorTreeParser<Dog>(BehaviorTreeParser.DEBUG_HIGH);
            BehaviorTree<Dog> buddyTree = parser.parse(reader, new Dog("Buddy"));

            // Clone Buddy's tree for Snoopy
            BehaviorTree<Dog> snoopyTree = (BehaviorTree<Dog>) buddyTree.cloneTask();
            snoopyTree.setObject(new Dog("Snoopy"));

            // Create split pane
            BehaviorTreeViewer<?> buddyTreeViewer = createTreeViewer(buddyTree.getObject().name, buddyTree, false, skin);
            BehaviorTreeViewer<?> snoopyTreeViewer = createTreeViewer(snoopyTree.getObject().name, snoopyTree, false, skin);
            return new SplitPane(new ScrollPane(buddyTreeViewer, skin), new ScrollPane(snoopyTreeViewer, skin), true, skin,
                    "default-horizontal");
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        NonBlockingSemaphoreRepository.clear();
    }
}
