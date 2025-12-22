package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.BiFunction;

import games.rednblack.editor.controller.commands.resource.DeleteSpineAnimation;
import games.rednblack.editor.controller.commands.resource.DeleteSpriteAnimation;
import games.rednblack.editor.factory.ItemFactory;
import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.renderer.factory.EntityFactory;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.box.BoxItemResource;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.box.SpineResource;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.box.SpriteResource;
import games.rednblack.h2d.extension.spine.SpineItemType;
import games.rednblack.puremvc.interfaces.INotification;
import games.rednblack.puremvc.util.Interests;

public class UIAnimationsTabMediator extends UIResourcesTabMediator<UIAnimationsTab> {

    private static final String TAG = UIAnimationsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;
    private final Array<DraggableResource> animationBoxes;

    public UIAnimationsTabMediator() {
        super(NAME, new UIAnimationsTab());
        animationBoxes = new Array<>();
    }

    @Override
    public void listNotificationInterests(Interests interests) {
        super.listNotificationInterests(interests);
        interests.add(DeleteSpineAnimation.DONE, DeleteSpriteAnimation.DONE);
    }

    @Override
    public void handleNotification(INotification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case DeleteSpineAnimation.DONE:
            case DeleteSpriteAnimation.DONE:
                initList(viewComponent.searchString);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initList(String searchText) {
        animationBoxes.clear();
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        createAnimationResources(resourceManager.getProjectSpineAnimationsList().keySet(), SpineResource.class, ItemFactory.get()::createSpineAnimation, searchText);
        createAnimationResources(resourceManager.getProjectSpriteAnimationsList().keySet(), SpriteResource.class, ItemFactory.get()::createSpriteAnimation, searchText);
        animationBoxes.sort();
        viewComponent.setThumbnailBoxes(animationBoxes);
    }

    private void createAnimationResources(Set<String> strings, Class<? extends BoxItemResource> resourceClass, BiFunction<String, Vector2, Boolean> factoryFunction, String searchText) {
        for (String animationName : strings) {
            if (!animationName.toLowerCase().contains(searchText)
                    || filterResource(animationName, resourceClass == SpineResource.class ? SpineItemType.SPINE_TYPE : EntityFactory.SPRITE_TYPE))
                continue;

            try {
                Constructor<? extends BoxItemResource> constructor = resourceClass.getConstructor(String.class, boolean.class);
                DraggableResource draggableResource = new DraggableResource(constructor.newInstance(animationName, true));
                draggableResource.initDragDrop();
                draggableResource.setFactoryFunction(factoryFunction);
                animationBoxes.add(draggableResource);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
