package games.rednblack.editor.controller;

import games.rednblack.editor.proxy.CommandManager;
import games.rednblack.editor.proxy.FontManager;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.proxy.ResolutionManager;
import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.proxy.SceneDataManager;
import games.rednblack.editor.proxy.WindowTitleManager;
import games.rednblack.editor.splash.SplashScreenAdapter;
import games.rednblack.h2d.common.proxy.CursorManager;
import games.rednblack.puremvc.commands.SimpleCommand;
import games.rednblack.puremvc.interfaces.INotification;

public class BootstrapProxyCommand extends SimpleCommand {
    @Override
    public void execute(INotification notification) {
        super.execute(notification);

        facade.sendNotification(SplashScreenAdapter.UPDATE_SPLASH, "Loading Proxies...");

        facade.registerProxy(new WindowTitleManager());
        facade.registerProxy(new FontManager());
        facade.registerProxy(new CommandManager());
        facade.registerProxy(new CursorManager());
        facade.registerProxy(new ResolutionManager());
        facade.registerProxy(new ResourceManager());
        facade.registerProxy(new ProjectManager());
        facade.registerProxy(new SceneDataManager());
    }
}
