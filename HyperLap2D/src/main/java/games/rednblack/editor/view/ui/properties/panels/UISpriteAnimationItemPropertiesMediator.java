package games.rednblack.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Array;

import games.rednblack.editor.controller.commands.component.UpdateSpriteAnimationDataCommand;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationComponent;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.editor.view.ui.properties.UIItemPropertiesMediator;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpriteAnimationItemPropertiesMediator extends UIItemPropertiesMediator<UISpriteAnimationItemProperties> {
    private static final String TAG = UISpriteAnimationItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private SpriteAnimationComponent spriteAnimationComponent;

    public UISpriteAnimationItemPropertiesMediator() {
        super(NAME, new UISpriteAnimationItemProperties());
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        super.listNotificationInterests(interests);
        interests.add(UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED:

                break;
            default:
                break;
        }
    }

    @Override
    protected void translateObservableDataToView(int entity) {

        spriteAnimationComponent = SandboxComponentRetriever.get(entity, SpriteAnimationComponent.class);
        Array<String> animations = new Array<>();
        spriteAnimationComponent.frameRangeMap.keySet().forEach(animations::add);

        viewComponent.setFPS(spriteAnimationComponent.fps);
        viewComponent.setAnimations(animations);
        viewComponent.setSelectedAnimation(spriteAnimationComponent.currentAnimation);
        viewComponent.setPlayMode(spriteAnimationComponent.playMode);
    }

    @Override
    protected void translateViewToItemData() {
        Object payload = UpdateSpriteAnimationDataCommand.payload(observableReference,
                viewComponent.getFPS(),
                viewComponent.getSelectedAnimation(),
                viewComponent.getPlayMode());

        facade.sendNotification(MsgAPI.ACTION_UPDATE_SPRITE_ANIMATION_DATA, payload);
    }
}
