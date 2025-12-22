package games.rednblack.editor.graph;

import games.rednblack.editor.graph.data.FieldType;

public interface GraphBoxOutputConnector<T extends FieldType> {
    Side getSide();

    float getOffset();

    String getFieldId();

    enum Side {
        Right, Bottom
    }
}
