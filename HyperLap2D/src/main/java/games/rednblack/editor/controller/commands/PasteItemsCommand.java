package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.HashSet;
import java.util.Set;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ZIndexComponent;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.data.MainItemVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.renderer.systems.LayerSystem;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.ui.FollowersUIMediator;
import games.rednblack.editor.view.ui.box.UILayerBoxMediator;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class PasteItemsCommand extends EntityModifyRevertibleCommand {

    private final Array<String> pastedEntityIds = new Array<>();

    public static Set<Integer> createEntitiesFromVO(CompositeItemVO compositeVO) {
        Set<Integer> entities = new HashSet<>();

        EntityFactory factory = Sandbox.getInstance().sceneControl.sceneLoader.getEntityFactory();
        int parentEntity = Sandbox.getInstance().getCurrentViewingEntity();


        for (String key : compositeVO.content.keys()) {
            if (key.equals(CompositeItemVO.class.getName())) continue;

            Array<MainItemVO> vos = compositeVO.content.get(key);
            for (MainItemVO mainItemVO : vos) {
                int entity = factory.createEntity(parentEntity, mainItemVO);
                entities.add(entity);
            }
        }

        Array<MainItemVO> compositeVOs = compositeVO.content.get(CompositeItemVO.class.getName());
        if (compositeVOs != null) {
            for (MainItemVO mainItemVO : compositeVOs) {
                CompositeItemVO compositeItemVO = (CompositeItemVO) mainItemVO;
                int composite = factory.createEntity(parentEntity, compositeItemVO);
                entities.add(composite);
                factory.initAllChildren(composite, compositeItemVO);
            }
        }

        return entities;
    }

    @Override
    public void doAction() {
        Object[] payload = (Object[]) Sandbox.retrieveFromClipboard();

        UILayerBoxMediator layerBoxMediator = facade.retrieveMediator(UILayerBoxMediator.NAME);
        if (layerBoxMediator.getCurrentSelectedLayerName() == null || payload == null) {
            cancel();
            return;
        }

        Vector2 cameraPrevPosition = (Vector2) payload[0];
        Vector2 cameraCurrPosition = new Vector2(Sandbox.getInstance().getCamera().position.x, Sandbox.getInstance().getCamera().position.y);

        Vector2 diff = cameraCurrPosition.sub(cameraPrevPosition);

        Json json = HyperJson.getJson();
        CompositeItemVO compositeVO = json.fromJson(CompositeItemVO.class, (String) payload[1]);
        compositeVO.cleanIds();

        Set<Integer> newEntitiesList = createEntitiesFromVO(compositeVO);
        sandbox.getEngine().process();
        for (int entity : newEntitiesList) {
            TransformComponent transformComponent = SandboxComponentRetriever.get(entity, TransformComponent.class);
            transformComponent.x += diff.x;
            transformComponent.y += diff.y;
            ZIndexComponent zIndexComponent = SandboxComponentRetriever.get(entity, ZIndexComponent.class);
            zIndexComponent.setLayerName(layerBoxMediator.getCurrentSelectedLayerName());
            Sandbox.getInstance().getEngine().getSystem(LayerSystem.class).process();
            Facade.getInstance().sendNotification(MsgAPI.NEW_ITEM_ADDED, entity);
            pastedEntityIds.add(EntityUtils.getEntityId(entity));
        }

        facade.sendNotification(MsgAPI.ACTION_SET_SELECTION, newEntitiesList);
    }

    @Override
    public void undoAction() {
        FollowersUIMediator followersUIMediator = Facade.getInstance().retrieveMediator(FollowersUIMediator.NAME);
        for (String entityId : pastedEntityIds) {
            int entity = EntityUtils.getByUniqueId(entityId);
            followersUIMediator.removeFollower(entity);
            sandbox.getEngine().delete(entity);
            sandbox.getEngine().process();
            facade.sendNotification(MsgAPI.DELETE_ITEMS_COMMAND_DONE);
        }
        pastedEntityIds.clear();
    }
}
