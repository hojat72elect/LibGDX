package games.rednblack.editor.view.ui.followers;

public interface PolygonTransformationListener {
    void vertexUp(PolygonFollower follower, int vertexIndex, float x, float y);

    void vertexDown(PolygonFollower follower, int vertexIndex, float x, float y);

    void anchorDown(PolygonFollower follower, int anchor, float x, float y);

    void anchorDragged(PolygonFollower follower, int anchor, float x, float y);

    void anchorUp(PolygonFollower follower, int anchor, int button, float x, float y);
}
