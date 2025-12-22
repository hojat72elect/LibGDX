package games.rednblack.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skin;

import games.rednblack.editor.controller.commands.component.UpdateSpineDataCommand;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.ui.properties.UIItemPropertiesMediator;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.extension.spine.SpineComponent;
import games.rednblack.h2d.extension.spine.SpineVO;

public class UISpineAnimationItemPropertiesMediator extends UIItemPropertiesMediator<UISpineAnimationItemProperties> {
    private static final String TAG = UISpineAnimationItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    Array<String> animations = new Array<>();
    Array<String> skins = new Array<>();
    private SpineComponent spineComponent;

    public UISpineAnimationItemPropertiesMediator() {
        super(NAME, new UISpineAnimationItemProperties());
    }

    @Override
    protected void translateObservableDataToView(int entity) {
        spineComponent = SandboxComponentRetriever.get(entity, SpineComponent.class);

        animations.clear();
        for (Animation animation : spineComponent.getAnimations()) {
            animations.add(animation.getName());
        }

        viewComponent.setAnimations(animations);
        viewComponent.setSelectedAnimation(spineComponent.currentAnimationName);

        skins.clear();
        for (Skin skin : spineComponent.getSkins()) {
            skins.add(skin.getName());
        }
        viewComponent.setSkins(skins);
        viewComponent.setSelectedSkin(spineComponent.currentSkinName);
    }

    @Override
    protected void translateViewToItemData() {
        SpineVO payloadVO = new SpineVO();
        payloadVO.currentAnimationName = viewComponent.getSelectedAnimation();
        payloadVO.currentSkinName = viewComponent.getSelectedSkin();

        Object payload = UpdateSpineDataCommand.payload(observableReference, payloadVO);
        facade.sendNotification(MsgAPI.ACTION_UPDATE_SPINE_ANIMATION_DATA, payload);
    }
}
