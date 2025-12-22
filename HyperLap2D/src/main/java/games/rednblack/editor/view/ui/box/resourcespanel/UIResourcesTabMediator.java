package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.utils.ObjectMap;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.filters.IAbstractResourceFilter;
import games.rednblack.puremvc.Mediator;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public abstract class UIResourcesTabMediator<T extends UIResourcesTab> extends Mediator<T> {
    private static final String NAME = "games.rednblack.editor.view.ui.box.resourcespanel.UIResourcesTabMediator";
    public static final String CHANGE_ACTIVE_FILTER = NAME + ".CHANGE_ACTIVE_FILTER";

    protected ObjectMap<String, IAbstractResourceFilter> filters = new ObjectMap<>();

    private SettingsManager settingsManager;

    public UIResourcesTabMediator(String mediatorName, T viewComponent) {
        super(mediatorName, viewComponent);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        settingsManager = facade.retrieveProxy(SettingsManager.NAME);
    }


    @Override
    public void listNotificationInterests(Interests interests) {
        interests.add(ProjectManager.PROJECT_OPENED,
                ProjectManager.PROJECT_DATA_UPDATED,
                MsgAPI.ADD_RESOURCES_BOX_FILTER,
                MsgAPI.UPDATE_RESOURCES_LIST);
        interests.add(CHANGE_ACTIVE_FILTER);
    }

    @Override
    public void handleNotification(INotification notification) {
        IAbstractResourceFilter filter;

        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
            case ProjectManager.PROJECT_DATA_UPDATED:
            case MsgAPI.UPDATE_RESOURCES_LIST:
                initList(viewComponent.searchString);
                break;
            case MsgAPI.ADD_RESOURCES_BOX_FILTER:
                filter = notification.getBody();
                filter.setActive(settingsManager.editorConfigVO.enabledFilters.getOrDefault(filter.id, false));
                if (!filters.containsKey(filter.id))
                    filters.put(filter.id, filter);
                break;
            case CHANGE_ACTIVE_FILTER:
                if (filters.containsKey(notification.getType())) {
                    filter = filters.get(notification.getType());
                    filter.setActive(notification.getBody());
                    settingsManager.editorConfigVO.enabledFilters.put(filter.id, filter.isActive());
                    settingsManager.saveEditorConfig();
                }
                initList(viewComponent.searchString);
                break;
            default:
                break;
        }
    }

    protected abstract void initList(String searchText);

    protected boolean filterResource(String resName, int resType) {
        for (IAbstractResourceFilter filter : filters.values()) {
            if (filter.isActive() && filter.filterResource(resName, resType))
                return true;
        }
        return false;
    }
}
