package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class GameObjectModifiedEvent {

    private GameObject gameObject;

    public GameObjectModifiedEvent(final GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public interface GameObjectModifiedListener extends EventListener {

        @Subscribe
        void onGameObjectModified(GameObjectModifiedEvent event);
    }
}
