package games.rednblack.editor.controller;

import games.rednblack.puremvc.commands.MacroCommand;

public class StartupCommand extends MacroCommand {
    @Override
    protected void initializeMacroCommand() {
        super.initializeMacroCommand();

        addSubCommand(new BootstrapProxyCommand());
        addSubCommand(new BootstrapViewCommand());
        addSubCommand(new BootstrapInfoCommand());
        addSubCommand(new BootstrapCommand());
        addSubCommand(new BootstrapPlugins());
    }
}
