package games.rednblack.editor.controller;

import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.puremvc.commands.SimpleCommand;

public class SandboxCommand extends SimpleCommand {

    protected Sandbox sandbox;

    public SandboxCommand() {
        sandbox = Sandbox.getInstance();
    }
}
