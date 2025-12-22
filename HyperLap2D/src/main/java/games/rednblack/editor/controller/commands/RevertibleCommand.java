package games.rednblack.editor.controller.commands;

import games.rednblack.editor.controller.SandboxCommand;
import games.rednblack.editor.proxy.CommandManager;
import games.rednblack.puremvc.Notification;
import games.rednblack.puremvc.interfaces.INotification;

public abstract class RevertibleCommand extends SandboxCommand {

    protected CommandManager commandManager;
    protected INotification notification;

    protected boolean isCancelled = false;
    protected boolean stateDone = false;

    @Override
    public void execute(INotification notification) {
        commandManager = facade.retrieveProxy(CommandManager.NAME);
        this.notification = ((Notification) notification).copy();
        callDoAction();
        stateDone = true;
        if (!isCancelled) commandManager.addCommand(this);
    }

    public abstract void doAction();

    public abstract void undoAction();

    public void callDoAction() {
        doAction();
    }

    public void callUndoAction() {
        undoAction();
    }

    public INotification getNotification() {
        return notification;
    }

    public boolean isStateDone() {
        return stateDone;
    }

    public void setStateDone(boolean state) {
        stateDone = state;
    }

    public void cancel() {
        isCancelled = true;
    }
}
