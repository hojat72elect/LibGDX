package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.Json;

import java.util.Set;

import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class CutItemsCommand extends EntityModifyRevertibleCommand {

    private String backup;

    @Override
    public void doAction() {
        backup = EntityUtils.getJsonStringFromEntities(sandbox.getSelector().getSelectedItems());
        String data = EntityUtils.getJsonStringFromEntities(sandbox.getSelector().getSelectedItems());

        Sandbox.copyToClipboard(data);
        sandbox.getSelector().removeCurrentSelectedItems();

        facade.sendNotification(MsgAPI.DELETE_ITEMS_COMMAND_DONE);
    }

    @Override
    public void undoAction() {
        Json json = HyperJson.getJson();
        CompositeItemVO compositeVO = json.fromJson(CompositeItemVO.class, backup);
        Set<Integer> newEntitiesList = PasteItemsCommand.createEntitiesFromVO(compositeVO);

        sandbox.getEngine().process();
        for (int entity : newEntitiesList) {
            Facade.getInstance().sendNotification(MsgAPI.NEW_ITEM_ADDED, entity);
        }

        facade.sendNotification(MsgAPI.ACTION_SET_SELECTION, newEntitiesList);
    }
}
