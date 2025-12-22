package games.rednblack.h2d.common.plugins;

import com.badlogic.gdx.utils.Array;

import java.util.Set;

public interface H2DPlugin {

    String getName();

    void initPlugin();

    void setAPI(PluginAPI pluginAPI);

    void onDropDownOpen(Set<Integer> selectedEntities, Array<String> actionsSet);
}
