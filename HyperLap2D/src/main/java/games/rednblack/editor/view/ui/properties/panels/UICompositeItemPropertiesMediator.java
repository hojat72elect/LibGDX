package games.rednblack.editor.view.ui.properties.panels;

import java.util.HashSet;
import java.util.Set;

import games.rednblack.editor.controller.commands.component.UpdateCompositeDataCommand;
import games.rednblack.editor.renderer.components.CompositeTransformComponent;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.systems.CompositeSystem;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.ui.properties.UIItemPropertiesMediator;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UICompositeItemPropertiesMediator extends UIItemPropertiesMediator<UICompositeItemProperties> {

    private static final String TAG = UICompositeItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UICompositeItemPropertiesMediator() {
        super(NAME, new UICompositeItemProperties());
    }

    @Override
    protected void translateObservableDataToView(int item) {
        CompositeTransformComponent transformComponent = SandboxComponentRetriever.get(item, CompositeTransformComponent.class);
        viewComponent.setAutomaticResize(transformComponent.automaticResize);
        viewComponent.setScissorsEnabled(transformComponent.scissorsEnabled);
        viewComponent.setRenderToFBOEnabled(transformComponent.renderToFBO);
    }

    @Override
    protected void translateViewToItemData() {
        CompositeItemVO payloadVo = new CompositeItemVO();
        payloadVo.loadFromEntity(observableReference, sandbox.getEngine(), sandbox.sceneControl.sceneLoader.getEntityFactory());

        payloadVo.automaticResize = viewComponent.isAutomaticResizeIsEnabled();
        payloadVo.scissorsEnabled = viewComponent.isScissorsEnabled();
        payloadVo.renderToFBO = viewComponent.isRenderToFBOEnabled();

        Object payload = UpdateCompositeDataCommand.payload(observableReference, payloadVo);
        facade.sendNotification(MsgAPI.ACTION_UPDATE_COMPOSITE_DATA, payload);

        CompositeSystem compositeSystem = Sandbox.getInstance().getEngine().getSystem(CompositeSystem.class);
        if (compositeSystem != null) {
            compositeSystem.process(observableReference);
        }

        Set<Integer> entityHashSet = new HashSet<>();
        entityHashSet.add(observableReference);
        Facade.getInstance().sendNotification(MsgAPI.ITEM_SELECTION_CHANGED, entityHashSet);
    }
}