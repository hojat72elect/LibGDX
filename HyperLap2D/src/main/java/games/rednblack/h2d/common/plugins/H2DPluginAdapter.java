package games.rednblack.h2d.common.plugins;

import com.badlogic.gdx.utils.Array;

import net.mountainblade.modular.Module;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import games.rednblack.puremvc.Facade;

public abstract class H2DPluginAdapter implements H2DPlugin, Module {

    public Facade facade;
    protected PluginAPI pluginAPI;
    protected String name;

    public H2DPluginAdapter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Triggered whenever a context menu is displayed
     *
     * @param selectedEntities list of entities that were selected when context menu was created about, if right clicked on empty space empty array is used
     * @param actionsSet       list of current actions (notification id's) planned for this particular context menu, it can be modified by adding or removing elements.
     */
    @Override
    public void onDropDownOpen(Set<Integer> selectedEntities, Array<String> actionsSet) {

    }

    public PluginAPI getAPI() {
        return pluginAPI;
    }

    @Override
    public void setAPI(PluginAPI pluginAPI) {
        this.pluginAPI = pluginAPI;
        facade = pluginAPI.getFacade();
    }

    public Map<String, Object> getStorage() {
        return pluginAPI.getEditorConfig().pluginStorage.computeIfAbsent(name, k -> new HashMap<>());
    }
}
