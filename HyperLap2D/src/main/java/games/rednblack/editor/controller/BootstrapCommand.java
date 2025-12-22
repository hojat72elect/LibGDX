package games.rednblack.editor.controller;

import games.rednblack.editor.controller.commands.AddComponentToItemCommand;
import games.rednblack.editor.controller.commands.AddSelectionCommand;
import games.rednblack.editor.controller.commands.AddToLibraryActionCommand;
import games.rednblack.editor.controller.commands.AddToLibraryCommand;
import games.rednblack.editor.controller.commands.CenterOriginPointCommand;
import games.rednblack.editor.controller.commands.ChangeOriginPointPosition;
import games.rednblack.editor.controller.commands.ChangePolygonVertexPositionCommand;
import games.rednblack.editor.controller.commands.ChangeRulerPositionCommand;
import games.rednblack.editor.controller.commands.CompositeCameraChangeCommand;
import games.rednblack.editor.controller.commands.ConvertToButtonCommand;
import games.rednblack.editor.controller.commands.ConvertToCompositeCommand;
import games.rednblack.editor.controller.commands.CopyItemsCommand;
import games.rednblack.editor.controller.commands.CreateItemCommand;
import games.rednblack.editor.controller.commands.CreatePrimitiveCommand;
import games.rednblack.editor.controller.commands.CreateStickyNoteCommand;
import games.rednblack.editor.controller.commands.CustomVariableModifyCommand;
import games.rednblack.editor.controller.commands.CutItemsCommand;
import games.rednblack.editor.controller.commands.DeleteItemsCommand;
import games.rednblack.editor.controller.commands.DeleteLayerCommand;
import games.rednblack.editor.controller.commands.DeletePolygonVertexCommand;
import games.rednblack.editor.controller.commands.ExportProjectCommand;
import games.rednblack.editor.controller.commands.ItemChildrenTransformCommand;
import games.rednblack.editor.controller.commands.ItemTransformCommand;
import games.rednblack.editor.controller.commands.ItemsMoveCommand;
import games.rednblack.editor.controller.commands.LayerJumpCommand;
import games.rednblack.editor.controller.commands.LayerSwapCommand;
import games.rednblack.editor.controller.commands.ModifyStickyNoteCommand;
import games.rednblack.editor.controller.commands.NewLayerCommand;
import games.rednblack.editor.controller.commands.PasteItemsCommand;
import games.rednblack.editor.controller.commands.PluginItemCommand;
import games.rednblack.editor.controller.commands.ReleaseSelectionCommand;
import games.rednblack.editor.controller.commands.RemoveComponentFromItemCommand;
import games.rednblack.editor.controller.commands.RemoveStickyNoteCommand;
import games.rednblack.editor.controller.commands.RenameLayerCommand;
import games.rednblack.editor.controller.commands.RenameLibraryActionCommand;
import games.rednblack.editor.controller.commands.SaveExportPathCommand;
import games.rednblack.editor.controller.commands.SetSelectionCommand;
import games.rednblack.editor.controller.commands.ShowNotificationCommand;
import games.rednblack.editor.controller.commands.UpdateEntityComponentsCommand;
import games.rednblack.editor.controller.commands.UpdatePolygonDataCommand;
import games.rednblack.editor.controller.commands.UpdateSceneDataCommand;
import games.rednblack.editor.controller.commands.component.ReplaceRegionCommand;
import games.rednblack.editor.controller.commands.component.ReplaceSpineCommand;
import games.rednblack.editor.controller.commands.component.ReplaceSpriteAnimationCommand;
import games.rednblack.editor.controller.commands.component.UpdateCircleShapeCommand;
import games.rednblack.editor.controller.commands.component.UpdateCompositeDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateImageItemDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateLabelDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateLightBodyDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateLightDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateParticleDataCommand;
import games.rednblack.editor.controller.commands.component.UpdatePhysicsDataCommand;
import games.rednblack.editor.controller.commands.component.UpdatePolygonVerticesCommand;
import games.rednblack.editor.controller.commands.component.UpdateSensorDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateShaderDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateSpineDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateSpriteAnimationDataCommand;
import games.rednblack.editor.controller.commands.component.UpdateTalosDataCommand;
import games.rednblack.editor.controller.commands.resource.ChangeLibraryActionCommand;
import games.rednblack.editor.controller.commands.resource.DeleteImageResource;
import games.rednblack.editor.controller.commands.resource.DeleteLibraryAction;
import games.rednblack.editor.controller.commands.resource.DeleteLibraryItem;
import games.rednblack.editor.controller.commands.resource.DeleteMultipleResources;
import games.rednblack.editor.controller.commands.resource.DeleteParticleEffect;
import games.rednblack.editor.controller.commands.resource.DeleteShaderCommand;
import games.rednblack.editor.controller.commands.resource.DeleteSpineAnimation;
import games.rednblack.editor.controller.commands.resource.DeleteSpriteAnimation;
import games.rednblack.editor.controller.commands.resource.DeleteTalosVFX;
import games.rednblack.editor.controller.commands.resource.DeleteTinyVGResource;
import games.rednblack.editor.controller.commands.resource.DuplicateLibraryAction;
import games.rednblack.editor.controller.commands.resource.ExportActionCommand;
import games.rednblack.editor.controller.commands.resource.ExportLibraryItemCommand;
import games.rednblack.editor.splash.SplashScreenAdapter;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.commands.SimpleCommand;
import games.rednblack.puremvc.interfaces.INotification;

public class BootstrapCommand extends SimpleCommand {

    @Override
    public void execute(INotification notification) {
        super.execute(notification);

        facade.sendNotification(SplashScreenAdapter.UPDATE_SPLASH, "Loading Commands...");

        facade.registerCommand(MsgAPI.ACTION_CUT, CutItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_COPY, CopyItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_PASTE, PasteItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE, DeleteItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CREATE_ITEM, CreateItemCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CAMERA_CHANGE_COMPOSITE, CompositeCameraChangeCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CREATE_PRIMITIVE, CreatePrimitiveCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CREATE_STICKY_NOTE, CreateStickyNoteCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REMOVE_STICKY_NOTE, RemoveStickyNoteCommand.class);
        facade.registerCommand(MsgAPI.ACTION_MODIFY_STICKY_NOTE, ModifyStickyNoteCommand.class);

        facade.registerCommand(MsgAPI.ACTION_DELETE_LAYER, DeleteLayerCommand.class);
        facade.registerCommand(MsgAPI.ACTION_NEW_LAYER, NewLayerCommand.class);
        facade.registerCommand(MsgAPI.ACTION_SWAP_LAYERS, LayerSwapCommand.class);
        facade.registerCommand(MsgAPI.ACTION_JUMP_LAYERS, LayerJumpCommand.class);
        facade.registerCommand(MsgAPI.ACTION_RENAME_LAYER, RenameLayerCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ADD_COMPONENT, AddComponentToItemCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REMOVE_COMPONENT, RemoveComponentFromItemCommand.class);
        facade.registerCommand(MsgAPI.CUSTOM_VARIABLE_MODIFY, CustomVariableModifyCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEMS_MOVE_TO, ItemsMoveCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEM_AND_CHILDREN_TO, ItemChildrenTransformCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEM_TRANSFORM_TO, ItemTransformCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REPLACE_REGION_DATA, ReplaceRegionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REPLACE_SPRITE_ANIMATION_DATA, ReplaceSpriteAnimationCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REPLACE_SPINE_ANIMATION_DATA, ReplaceSpineCommand.class);
        facade.registerCommand(MsgAPI.ACTION_ADD_TO_LIBRARY, AddToLibraryCommand.class);
        facade.registerCommand(MsgAPI.ACTION_ADD_TO_LIBRARY_ACTION, AddToLibraryActionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CHANGE_LIBRARY_ACTION, ChangeLibraryActionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CONVERT_TO_BUTTON, ConvertToButtonCommand.class);
        facade.registerCommand(MsgAPI.ACTION_GROUP_ITEMS, ConvertToCompositeCommand.class);

        facade.registerCommand(MsgAPI.ACTION_SET_SELECTION, SetSelectionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_ADD_SELECTION, AddSelectionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_RELEASE_SELECTION, ReleaseSelectionCommand.class);

        facade.registerCommand(MsgAPI.ACTION_UPDATE_RULER_POSITION, ChangeRulerPositionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CHANGE_POLYGON_VERTEX_POSITION, ChangePolygonVertexPositionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_POLYGON_VERTEX, DeletePolygonVertexCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CHANGE_ORIGIN_POSITION, ChangeOriginPointPosition.class);
        facade.registerCommand(MsgAPI.ACTION_CENTER_ORIGIN_POSITION, CenterOriginPointCommand.class);

        // DATA MODIFY by components
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SCENE_DATA, UpdateSceneDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_ITEM_DATA, UpdateEntityComponentsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_LABEL_DATA, UpdateLabelDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_LIGHT_DATA, UpdateLightDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_COMPOSITE_DATA, UpdateCompositeDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_PARTICLE_DATA, UpdateParticleDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_TALOS_DATA, UpdateTalosDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_BODY_LIGHT_DATA, UpdateLightBodyDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_CIRCLE_SHAPE, UpdateCircleShapeCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_PHYSICS_BODY_DATA, UpdatePhysicsDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SENSOR_DATA, UpdateSensorDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SHADER_DATA, UpdateShaderDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_IMAGE_ITEM_DATA, UpdateImageItemDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SPRITE_ANIMATION_DATA, UpdateSpriteAnimationDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SPINE_ANIMATION_DATA, UpdateSpineDataCommand.class);

        facade.registerCommand(MsgAPI.ACTION_UPDATE_MESH_DATA, UpdatePolygonVerticesCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_POLYGON_DATA, UpdatePolygonDataCommand.class);

        facade.registerCommand(MsgAPI.ACTION_EXPORT_PROJECT, ExportProjectCommand.class);
        facade.registerCommand(MsgAPI.SAVE_EXPORT_PATH, SaveExportPathCommand.class);

        facade.registerCommand(MsgAPI.ACTION_PLUGIN_PROXY_COMMAND, PluginItemCommand.class);

        // Resources
        facade.registerCommand(MsgAPI.ACTION_DELETE_IMAGE_RESOURCE, DeleteImageResource.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_TINY_VG_RESOURCE, DeleteTinyVGResource.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SHADER, DeleteShaderCommand.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_LIBRARY_ITEM, DeleteLibraryItem.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_LIBRARY_ACTION, DeleteLibraryAction.class);
        facade.registerCommand(MsgAPI.ACTION_DUPLICATE_LIBRARY_ACTION, DuplicateLibraryAction.class);
        facade.registerCommand(MsgAPI.ACTION_EXPORT_LIBRARY_ITEM, ExportLibraryItemCommand.class);
        facade.registerCommand(MsgAPI.ACTION_EXPORT_ACTION_ITEM, ExportActionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_RENAME_ACTION_ITEM, RenameLibraryActionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_PARTICLE_EFFECT, DeleteParticleEffect.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_TALOS_VFX, DeleteTalosVFX.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SPINE_ANIMATION_RESOURCE, DeleteSpineAnimation.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SPRITE_ANIMATION_RESOURCE, DeleteSpriteAnimation.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_MULTIPLE_RESOURCE, DeleteMultipleResources.class);

        facade.registerCommand(MsgAPI.SHOW_NOTIFICATION, ShowNotificationCommand.class);
    }
}
