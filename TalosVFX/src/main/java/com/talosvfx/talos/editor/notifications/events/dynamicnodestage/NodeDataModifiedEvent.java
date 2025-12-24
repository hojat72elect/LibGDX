package com.talosvfx.talos.editor.notifications.events.dynamicnodestage;

import com.talosvfx.talos.editor.nodes.DynamicNodeStage;
import com.talosvfx.talos.editor.nodes.NodeWidget;
import com.talosvfx.talos.editor.notifications.events.AbstractContextRequiredEvent;

public class NodeDataModifiedEvent extends AbstractContextRequiredEvent<DynamicNodeStage<?>> {
    public boolean isFastChange = false;
    public boolean renamedField = false;
    private NodeWidget node;

    @Override
    public void reset() {
        node = null;
        isFastChange = false;
        renamedField = false;
    }

    public NodeDataModifiedEvent set(DynamicNodeStage<?> context, NodeWidget node) {
        setContext(context);
        this.node = node;
        return this;
    }

    public NodeWidget getNode() {
        return node;
    }
}
