package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import games.rednblack.editor.controller.commands.resource.DeleteLibraryItem;
import games.rednblack.editor.controller.commands.resource.ExportLibraryItemCommand;
import games.rednblack.editor.factory.ItemFactory;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.list.LibraryItemResource;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UILibraryItemsTabMediator extends UIResourcesTabMediator<UILibraryItemsTab> {

    private static final String TAG = UILibraryItemsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private final Array<DraggableResource> itemArray = new Array<>();

    public UILibraryItemsTabMediator() {
        super(NAME, new UILibraryItemsTab());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        super.listNotificationInterests(interests);
        interests.add(MsgAPI.LIBRARY_LIST_UPDATED, DeleteLibraryItem.DONE, ExportLibraryItemCommand.DONE);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.LIBRARY_LIST_UPDATED:
            case DeleteLibraryItem.DONE:
                initList(viewComponent.searchString);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initList(String searchText) {
        searchText = searchText.toLowerCase();
        ProjectManager projectManager = Facade.getInstance().retrieveProxy(ProjectManager.NAME);
        HashMap<String, CompositeItemVO> items = projectManager.currentProjectInfoVO.libraryItems;

        itemArray.clear();
        for (String key : items.keySet()) {
            if (!key.toLowerCase().contains(searchText)
                    || filterResource(key, EntityFactory.COMPOSITE_TYPE)) continue;
            DraggableResource draggableResource = new DraggableResource(new LibraryItemResource(key));
            draggableResource.setFactoryFunction(ItemFactory.get()::createItemFromLibrary);
            draggableResource.initDragDrop();
            itemArray.add(draggableResource);
        }
        itemArray.sort();
        viewComponent.setItems(itemArray);
    }
}
