package games.rednblack.editor.graph;

import games.rednblack.editor.graph.data.FieldType;

public interface GraphBoxInputConnector<T extends FieldType> {
    Side getSide();

    float getOffset();

    String getFieldId();

    enum Side {
        Left, Top
    }
}
