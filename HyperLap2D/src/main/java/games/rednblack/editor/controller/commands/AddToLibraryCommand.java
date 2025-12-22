package games.rednblack.editor.controller.commands;

import java.util.HashMap;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class AddToLibraryCommand extends HistoricRevertibleCommand {

    private String createdLibraryItemName;
    private CompositeItemVO overwritten;
    private String prevName;
    private String entityId;

    public static Object payloadUnLink(int entity) {
        Object[] payload = new Object[2];
        payload[0] = entity;
        payload[1] = "";

        return payload;
    }

    public static Object payloadLink(int entity, String link) {
        Object[] payload = new Object[2];
        payload[0] = entity;
        payload[1] = link;

        return payload;
    }

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();

        int item = ((int) payload[0]);
        if (entityId == null)
            entityId = EntityUtils.getEntityId(item);
        createdLibraryItemName = (String) payload[1];

        MainItemComponent mainItemComponent = SandboxComponentRetriever.get(item, MainItemComponent.class);

        if (createdLibraryItemName.length() > 0) {
            ProjectManager projectManager = Facade.getInstance().retrieveProxy(ProjectManager.NAME);
            HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;

            if (libraryItems.containsKey(createdLibraryItemName)) {
                overwritten = libraryItems.get(createdLibraryItemName);
            }

            CompositeItemVO newVO = new CompositeItemVO();
            newVO.loadFromEntity(item, sandbox.getEngine(), sandbox.sceneControl.sceneLoader.getEntityFactory());
            newVO.cleanIds();
            libraryItems.put(createdLibraryItemName, newVO);

            //mark this entity as belonging to library
            mainItemComponent.libraryLink = createdLibraryItemName;
            facade.sendNotification(MsgAPI.LIBRARY_LIST_UPDATED);
        } else {
            prevName = mainItemComponent.libraryLink;
            // unlink it
            mainItemComponent.libraryLink = "";
        }
        facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED);
    }

    @Override
    public void undoAction() {
        ProjectManager projectManager = Facade.getInstance().retrieveProxy(ProjectManager.NAME);
        HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;

        if (createdLibraryItemName.length() > 0) {
            libraryItems.remove(createdLibraryItemName);

            if (overwritten != null) {
                libraryItems.put(createdLibraryItemName, overwritten);
            }
            facade.sendNotification(MsgAPI.LIBRARY_LIST_UPDATED);
        } else {
            int entity = EntityUtils.getByUniqueId(entityId);
            MainItemComponent mainItemComponent = SandboxComponentRetriever.get(entity, MainItemComponent.class);
            mainItemComponent.libraryLink = prevName;
            facade.sendNotification(MsgAPI.ITEM_DATA_UPDATED);
        }
    }
}
