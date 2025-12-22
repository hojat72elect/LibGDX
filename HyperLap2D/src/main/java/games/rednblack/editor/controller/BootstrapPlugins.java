package games.rednblack.editor.controller;

import net.mountainblade.modular.Module;
import net.mountainblade.modular.ModuleManager;
import net.mountainblade.modular.impl.DefaultModuleManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import games.rednblack.editor.proxy.PluginManager;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.splash.SplashScreenAdapter;
import games.rednblack.h2d.common.plugins.H2DPlugin;
import games.rednblack.puremvc.commands.SimpleCommand;
import games.rednblack.puremvc.interfaces.INotification;

public class BootstrapPlugins extends SimpleCommand {

    public void execute(INotification notification) {
        super.execute(notification);

        SettingsManager settingsManager = facade.retrieveProxy(SettingsManager.NAME);
        if (!settingsManager.editorConfigVO.enablePlugins)
            return;

        facade.sendNotification(SplashScreenAdapter.UPDATE_SPLASH, "Loading Plugins...");

        PluginManager pluginManager = new PluginManager();
        facade.registerProxy(pluginManager);

        ModuleManager manager = new DefaultModuleManager();
        for (File pluginDir : settingsManager.pluginDirs) {
            Collection<Module> loadedPlugins = manager.loadModules(pluginDir);

            pluginManager.setPluginDir(pluginDir.getAbsolutePath());
            pluginManager.setCacheDir(settingsManager.cacheDir.getAbsolutePath());
            System.out.println("Plugins directory: " + pluginDir.getAbsolutePath());
            System.out.println("Plugins loaded: " + loadedPlugins.size());

            for (Module module : loadedPlugins) {
                try {
                    pluginManager.initPlugin((H2DPlugin) module.getClass().getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
