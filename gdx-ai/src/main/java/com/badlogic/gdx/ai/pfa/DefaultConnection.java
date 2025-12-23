package com.badlogic.gdx.ai.pfa;

/**
 * A {@code DefaultConnection} is a {@link Connection} whose cost is 1.
 *
 * @param <N> Type of node
 */
public class DefaultConnection<N> implements Connection<N> {

    protected N fromNode;
    protected N toNode;

    public DefaultConnection(N fromNode, N toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public float getCost() {
        return 1f;
    }

    @Override
    public N getFromNode() {
        return fromNode;
    }

    @Override
    public N getToNode() {
        return toNode;
    }
}
