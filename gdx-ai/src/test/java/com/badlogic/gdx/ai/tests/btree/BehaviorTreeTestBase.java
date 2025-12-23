package com.badlogic.gdx.ai.tests.btree;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Base class for individual behavior tree tests.
 */
public abstract class BehaviorTreeTestBase {

    private String name;
    private String description;

    private final Array<BehaviorTreeViewer<?>> treeViewers;

    public BehaviorTreeTestBase(String name) {
        this(name, null);
    }

    public BehaviorTreeTestBase(String name, String description) {
        this.name = name;
        this.description = description;

        this.treeViewers = new Array<BehaviorTreeViewer<?>>();
    }

    public abstract Actor createActor(Skin skin);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected BehaviorTreeViewer<?> createTreeViewer(String name, BehaviorTree<?> tree, boolean loadAndSave, Skin skin) {
        @SuppressWarnings({"rawtypes", "unchecked"})
        BehaviorTreeViewer<?> btv = new BehaviorTreeViewer(tree, loadAndSave, skin);
        btv.setName(name);

        treeViewers.add(btv);

        return btv;
    }

    public void dispose() {
        for (BehaviorTreeViewer<?> treeViewer : treeViewers)
            treeViewer.getBehaviorTree().resetTask();
        treeViewers.clear();
    }
}
