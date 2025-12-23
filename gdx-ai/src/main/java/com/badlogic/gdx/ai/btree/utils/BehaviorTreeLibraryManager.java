package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.TaskCloneException;
import com.badlogic.gdx.utils.SerializationException;

/**
 * The {@code BehaviorTreeLibraryManager} is a singleton in charge of the creation of behavior trees using the underlying library.
 * If no library is explicitly set (see the method {@link #setLibrary(BehaviorTreeLibrary)}), a default library instantiated by
 * the constructor {@link BehaviorTreeLibrary#BehaviorTreeLibrary() BehaviorTreeLibrary()} is used instead.
 */
public final class BehaviorTreeLibraryManager {

    private static final BehaviorTreeLibraryManager instance = new BehaviorTreeLibraryManager();

    private BehaviorTreeLibrary library;

    private BehaviorTreeLibraryManager() {
        setLibrary(new BehaviorTreeLibrary());
    }

    /**
     * Returns the singleton instance of the {@code BehaviorTreeLibraryManager}.
     */
    public static BehaviorTreeLibraryManager getInstance() {
        return instance;
    }

    /**
     * Gets the the behavior tree library
     *
     * @return the behavior tree library
     */
    public BehaviorTreeLibrary getLibrary() {
        return library;
    }

    /**
     * Sets the the behavior tree library
     *
     * @param library the behavior tree library to set
     */
    public void setLibrary(BehaviorTreeLibrary library) {
        this.library = library;
    }

    /**
     * Creates the root task of {@link BehaviorTree} for the specified reference.
     *
     * @param treeReference the tree identifier, typically a path
     * @return the root task of the tree cloned from the archetype.
     * @throws SerializationException if the reference cannot be successfully parsed.
     * @throws TaskCloneException     if the archetype cannot be successfully parsed.
     */
    public <T> Task<T> createRootTask(String treeReference) {
        return library.createRootTask(treeReference);
    }

    /**
     * Creates the {@link BehaviorTree} for the specified reference.
     *
     * @param treeReference the tree identifier, typically a path
     * @return the tree cloned from the archetype.
     * @throws SerializationException if the reference cannot be successfully parsed.
     * @throws TaskCloneException     if the archetype cannot be successfully parsed.
     */
    public <T> BehaviorTree<T> createBehaviorTree(String treeReference) {
        return library.createBehaviorTree(treeReference);
    }

    /**
     * Creates the {@link BehaviorTree} for the specified reference and blackboard object.
     *
     * @param treeReference the tree identifier, typically a path
     * @param blackboard    the blackboard object (it can be {@code null}).
     * @return the tree cloned from the archetype.
     * @throws SerializationException if the reference cannot be successfully parsed.
     * @throws TaskCloneException     if the archetype cannot be successfully parsed.
     */
    public <T> BehaviorTree<T> createBehaviorTree(String treeReference, T blackboard) {
        return library.createBehaviorTree(treeReference, blackboard);
    }

    /**
     * Dispose behavior tree obtain by this library manager.
     *
     * @param treeReference the tree identifier.
     * @param behaviorTree  the tree to dispose.
     */
    public void disposeBehaviorTree(String treeReference, BehaviorTree<?> behaviorTree) {
        library.disposeBehaviorTree(treeReference, behaviorTree);
    }
}
