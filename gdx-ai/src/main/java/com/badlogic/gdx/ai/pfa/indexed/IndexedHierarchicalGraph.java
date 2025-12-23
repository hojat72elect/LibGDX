package com.badlogic.gdx.ai.pfa.indexed;

import com.badlogic.gdx.ai.pfa.HierarchicalGraph;

/**
 * A hierarchical graph for the {@link IndexedAStarPathFinder}.
 *
 * @param <N> Type of node
 */
public abstract class IndexedHierarchicalGraph<N> implements IndexedGraph<N>, HierarchicalGraph<N> {

    protected int levelCount;
    protected int level;

    /**
     * Creates an {@code IndexedHierarchicalGraph} with the given number of levels.
     */
    public IndexedHierarchicalGraph(int levelCount) {
        this.levelCount = levelCount;
        this.level = 0;
    }

    @Override
    public int getLevelCount() {
        return levelCount;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public abstract N convertNodeBetweenLevels(int inputLevel, N node, int outputLevel);
}
