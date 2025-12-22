package games.rednblack.editor.controller.commands.resource;

import games.rednblack.editor.utils.AssetIOManager;
import games.rednblack.editor.utils.AssetsUtils;

public class DeleteLibraryItem extends DeleteResourceCommand {

    private static final String CLASS_NAME = "games.rednblack.editor.controller.commands.resource.DeleteLibraryItem";
    public static final String DONE = CLASS_NAME + "DONE";

    @Override
    protected String confirmDialogTitle() {
        return "Delete Library Item";
    }

    @Override
    public void doAction() {
        String libraryItemName = notification.getBody();

        AssetIOManager.getInstance().deleteAsset(AssetsUtils.TYPE_HYPERLAP2D_LIBRARY, sandbox.getRootEntity(), libraryItemName);

        facade.sendNotification(DONE, libraryItemName);
    }
}
