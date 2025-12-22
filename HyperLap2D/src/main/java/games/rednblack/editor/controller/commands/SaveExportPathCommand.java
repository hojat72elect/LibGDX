package games.rednblack.editor.controller.commands;

import games.rednblack.editor.controller.SandboxCommand;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.puremvc.interfaces.INotification;

public class SaveExportPathCommand extends SandboxCommand {

    @Override
    public void execute(INotification notification) {
        String path = notification.getBody();

        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
        projectManager.setExportPaths(path);
        projectManager.saveCurrentProject();
    }
}
