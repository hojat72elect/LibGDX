package com.mbrlabs.mundus.editor.core.project;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mbrlabs.mundus.editor.assets.EditorAssetManager;
import com.mbrlabs.mundus.editor.core.EditorScene;
import com.mbrlabs.mundus.editor.preferences.MundusPreferencesManager;
import com.mbrlabs.mundus.editor.utils.Log;

import org.apache.commons.lang3.StringUtils;

/**
 * A project context represents an loaded and opened project.
 * <p>
 * A project context can have many scenes, nut only one scene at a time can be
 * active.
 */
public class ProjectContext implements Disposable {

    private static final String TAG = ProjectContext.class.getSimpleName();

    public ProjectSettings settings;
    public String path;
    public String name;

    // Has the project completed loading?
    public boolean loaded = false;

    public Array<String> scenes;
    public EditorScene currScene;

    public EditorAssetManager assetManager;
    public MundusPreferencesManager projectPref;
    public boolean renderWireframe = false;
    /**
     * set by kryo when project is loaded. do not use this
     */
    public String activeSceneName;
    private int idProvider;

    public ProjectContext(int idProvider) {
        scenes = new Array<>();
        settings = new ProjectSettings();
        currScene = new EditorScene();
        this.idProvider = idProvider;
    }

    public synchronized int obtainID() {
        idProvider += 1;
        return idProvider;
    }

    public synchronized int inspectCurrentID() {
        return idProvider;
    }

    @Override
    public void dispose() {
        Log.debug(TAG, "Disposing current project: {}", path);
        if (assetManager != null) {
            assetManager.dispose();
        }
    }

    /**
     * Initializes this projects Preferences manager
     * The project name must be set before calling this method.
     */
    public void initPreferences() {
        if (StringUtils.isEmpty(name)) {
            throw new GdxRuntimeException("Cannot initialize preferences if project name is null or blank.");
        }
        projectPref = new MundusPreferencesManager(name);
    }
}
