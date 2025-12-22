package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.NodeComponent;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.puremvc.Facade;

public abstract class EntityModifyRevertibleCommand extends HistoricRevertibleCommand {

    @Override
    public void callDoAction() {
        super.callDoAction();
        postChange();
    }

    @Override
    public void callUndoAction() {
        super.callUndoAction();
        postChange();
    }

    protected void postChange() {
        String parentId = EntityUtils.getEntityId(sandbox.getCurrentViewingEntity());
        int entity = EntityUtils.getByUniqueId(parentId);

        // Update item library data if it was in library
        MainItemComponent mainItemComponent = SandboxComponentRetriever.get(entity, MainItemComponent.class);
        String link = mainItemComponent.libraryLink;

        if (link != null && link.length() > 0) {
            ProjectManager projectManager = Facade.getInstance().retrieveProxy(ProjectManager.NAME);
            HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;
            if (libraryItems.containsKey(mainItemComponent.libraryLink)) {
                CompositeItemVO itemVO = new CompositeItemVO();
                itemVO.loadFromEntity(entity, sandbox.getEngine(), sandbox.sceneControl.sceneLoader.getEntityFactory());
                itemVO.cleanIds();
                libraryItems.put(mainItemComponent.libraryLink, itemVO);
            }

            Array<Integer> linkedEntities = EntityUtils.getByLibraryLink(link);
            for (int dependable : linkedEntities) {
                if (dependable == entity) continue;
                NodeComponent nodeComponent = SandboxComponentRetriever.get(dependable, NodeComponent.class);
                for (int child : nodeComponent.children) {
                    sandbox.getEngine().delete(child);
                }
                nodeComponent.children.clear();
                sandbox.getEngine().process();

                EntityFactory factory = sandbox.getSceneControl().sceneLoader.getEntityFactory();
                factory.initAllChildren(dependable, libraryItems.get(link));
            }
        }
    }
}
