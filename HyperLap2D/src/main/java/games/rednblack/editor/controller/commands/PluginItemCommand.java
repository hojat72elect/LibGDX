package games.rednblack.editor.controller.commands;

import games.rednblack.h2d.common.IItemCommand;

public class PluginItemCommand extends EntityModifyRevertibleCommand {

    private IItemCommand command;
    private Object body;

    public static Object build(IItemCommand command, Object body) {
        Object[] payload = new Object[2];
        payload[0] = command;
        payload[1] = body;
        return payload;
    }

    @Override
    public void doAction() {
        if (command == null) {
            Object[] payload = notification.getBody();
            command = (IItemCommand) payload[0];
            body = payload[1];
        }

        command.doAction(body);
    }

    @Override
    public void undoAction() {
        command.undoAction(body);
    }
}
