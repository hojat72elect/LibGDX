package games.rednblack.editor.view.ui.followers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AnchorListener extends ClickListener {

    private final Vector2 localTOglobal = new Vector2();
    protected int anchorId;
    protected FollowerTransformationListener listener = new EmptyTransformationListener();
    protected NormalSelectionFollower follower;

    public AnchorListener(NormalSelectionFollower follower, int anchorId) {
        this.anchorId = anchorId;
        this.follower = follower;
    }

    public AnchorListener(NormalSelectionFollower follower, FollowerTransformationListener listener, int anchorId) {
        this.listener = listener;
        this.anchorId = anchorId;
        this.follower = follower;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        localTOglobal.set(event.getStageX(), event.getStageY());
        if (listener != null) listener.anchorDown(follower, anchorId, localTOglobal.x, localTOglobal.y);
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        localTOglobal.set(event.getStageX(), event.getStageY());
        if (listener != null) listener.anchorDragged(follower, anchorId, localTOglobal.x, localTOglobal.y);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        localTOglobal.set(event.getStageX(), event.getStageY());
        if (listener != null) listener.anchorUp(follower, anchorId, button, localTOglobal.x, localTOglobal.y);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        localTOglobal.set(event.getStageX(), event.getStageY());
        if (listener != null) listener.anchorMouseEnter(follower, anchorId, localTOglobal.x, localTOglobal.y);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        localTOglobal.set(event.getStageX(), event.getStageY());
        if (listener != null) listener.anchorMouseExit(follower, anchorId, localTOglobal.x, localTOglobal.y);
    }
}
