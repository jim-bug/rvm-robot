/*
 * User: jelmini8
 * Date: Jan 10, 2002
 * Time: 11:09:04 PM
 */
package ch.unige.rvm1;

import java.util.ArrayList;
import java.util.Iterator;

abstract class CompositeCommand extends Command {

    public CompositeCommand() {
        _compositeCommands = new ArrayList();
    }

    public void execute() {
        super.execute();
        sendCompositeCommands();
    }

    protected void sendCompositeCommands() {
        Iterator i = _compositeCommands.iterator();
        while (i.hasNext()) {
            Command command = (Command) i.next();
            command.execute();
        }
    }

    protected void addCompositeCommand(Command command) {
        _compositeCommands.add(command);
    }

    private ArrayList _compositeCommands;
}
