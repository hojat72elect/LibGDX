package games.rednblack.h2d.common;

/**
 * Created by azakhary on 10/23/2015.
 */
public interface IItemCommand {

    void doAction(Object body);

    void undoAction(Object body);
}
