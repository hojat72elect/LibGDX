package games.rednblack.editor.view.ui.followers;

public interface FollowerTransformationListener {
    void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y);

    void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y);

    void anchorUp(NormalSelectionFollower follower, int anchor, int button, float x, float y);

    void anchorMouseEnter(NormalSelectionFollower follower, int anchor, float x, float y);

    void anchorMouseExit(NormalSelectionFollower follower, int anchor, float x, float y);
}
