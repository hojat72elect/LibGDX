package games.rednblack.editor.controller.commands;

import games.rednblack.editor.controller.SandboxCommand;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.h2d.common.H2DDialogs;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.interfaces.INotification;

public class ExportProjectCommand extends SandboxCommand {

    @Override
    public void execute(INotification notification) {
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        if (projectManager.currentProjectVO.texturePackerVO.fast) {
            H2DDialogs.showConfirmDialog(sandbox.getUIStage(),
                    "Warning, not optimized Atlas!", "While a fast packing can speed up the development time,\nfor production you should disable it:\n\nFile -> Settings -> Project Export -> Fast Packing",
                    new String[]{"Cancel", "Yes, export as is"}, new Integer[]{0, 1}, r -> {
                        if (r == 1) {
                            projectManager.exportProject();
                            facade.sendNotification(MsgAPI.SHOW_NOTIFICATION, "Project successfully exported");
                        }
                    }).padBottom(20).pack();
        } else {
            projectManager.exportProject();
            facade.sendNotification(MsgAPI.SHOW_NOTIFICATION, "Project successfully exported");
        }
    }
}
