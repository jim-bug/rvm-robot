/*
 * User: jelmini8
 * Date: Jan 9, 2002
 * Time: 9:57:14 AM
 */
package ch.unige.rvm1;

import java.util.ArrayList;
import java.util.Iterator;

class CommandInterpreter {

    public CommandInterpreter() {
        _commands = new ArrayList();
        _commands.add(new NestCommand());
        _commands.add(new ResetCommand());
        _commands.add(new MoveJointCommand());
        _commands.add(new GripCloseCommand());
        _commands.add(new GripOpenCommand());
        _commands.add(new SpeedCommand());
        _commands.add(new AbsoluteMoveCommand());
        _commands.add(new DrawYZCommand());
    }

    public Command getCommand(String cmd) throws RVM1CommandException {
        if (cmd == null || cmd.length() < 2) {
            throw new RVM1CommandException("Command not found.");
        }
        cmd = cmd.toUpperCase();
        String cmdName = cmd.substring(0, 2);
        String params = cmd.substring(2);
        Command command = getByName(cmdName);
        command = command.getClone();
        command.setParameters(params);
        return command;
    }

    private Command getByName(String cmdName) throws RVM1CommandException {
        Iterator i = _commands.iterator();
        while (i.hasNext()) {
            Command command = (Command) i.next();
            if (command.isCommand(cmdName)) {
                return command;
            }
        }
        throw new RVM1CommandException("Command not found.");
    }

    private ArrayList _commands;
}
