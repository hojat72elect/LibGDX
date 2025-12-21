package games.rednblack.editor.renderer.commons;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.HashMap;

import games.rednblack.editor.renderer.factory.component.ComponentFactory;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic;

/**
 * Created by azakhary on 7/20/2015.
 */
public interface IExternalItemType {
    int getTypeId();

    DrawableLogic getDrawable();

    BaseSystem getSystem();

    ComponentFactory getComponentFactory();

    void injectMappers();

    boolean hasResources();

    void loadExternalTypesAsync(IResourceRetriever rm, ObjectSet<String> assetsToLoad, HashMap<String, Object> assets);

    void loadExternalTypesSync(IResourceRetriever rm, ObjectSet<String> assetsToLoad, HashMap<String, Object> assets);
}
