/*
 * User: jelmini8
 * Date: Jan 7, 2002
 * Time: 4:59:04 PM
 */
package ch.unige.rvm1;

import java.util.*;

abstract class Command {

    public Command() {
        setName();
    }

    public static void setRVM1InternalState(RVM1InternalState state) {
        _state = state;
    }

    public static void addCommandListener(CommandListener l) {
        _listeners.add(l);
    }

    public static void setSimulation(boolean simulation) {
        if(simulation != _simulation) {
            _simulation = simulation;
            if(_simulation) {
                _backupState = _state;
                _state = (RVM1InternalState) _state.getCopy();
            }
            else {
                _state = _backupState;
                _backupState = null;
            }
        }
    }

    public static boolean isSimulation() {
        return _simulation;
    }

    public void setParameters(String parameters) throws RVM1CommandException {
        StringTokenizer tokenizer = new StringTokenizer(parameters.toUpperCase(), " ,");
        try {
            parseParameters(tokenizer);
        }
        catch(Exception e) {
            throw new RVM1CommandException("Illegal parameters.");
        }

        if(tokenizer.hasMoreTokens()) {
            throw new RVM1CommandException("Too much parameters.");
        }
    }

    public void execute() {
        if(areParametersValid()) {
            notifyExecuted();
            if(_simulation) {
                simulation();
            }
            else {
                execution();
            }
        }
        else {
            notifyError("Out of range parameter values.");
        }
    }

    protected void execution() {
        RVM1ListenerAdapter listener = createListener();
        sendCommand();
        updateRVM1State();
        synchronized(_lock) {
            try {
                _lock.wait();
            }
            catch(InterruptedException e) {
            }
        }
        _state.getRVM1().removeRVM1Listener(listener);
    }

    protected void simulation() {
        updateRVM1State();
    }

    public boolean isCommand(String cmd) {
        return (cmd != null && cmd.equals(_name));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(10);
        sb.append(_name);
        appendParameters(sb);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return getClone();
    }

    protected void setName() {
        _name = "ABSTRACT COMMAND";
    }

    protected void notifyError(String errMessage) {
        for(Iterator iterator = _listeners.iterator(); iterator.hasNext();) {
            CommandListener listener = (CommandListener) iterator.next();
            listener.error(toString(), errMessage);
        }
//        throw new RVM1CommandException(errMessage);
    }

    protected void notifyExecuted() {
        for(Iterator iterator = _listeners.iterator(); iterator.hasNext();) {
            CommandListener listener = (CommandListener) iterator.next();
            listener.executed(toString());
        }
    }

    protected void sendCommand() {
        // _state.getConnection().send(toString());        // probabile modifica
        String command = toString() + "\r\n";
        for(int i = 0; i < command.length(); i++){
            System.out.println(command.charAt(i) + " : " + (int)command.charAt(i));
        }
        _state.getConnection().send(command);        // probabile modifica
    }

    protected boolean inRange(float val, float min, float max) {
        return (val >= min && val <= max);
    }

    protected boolean inRange(int val, int min, int max) {
        return (val >= min && val <= max);
    }

    private RVM1ListenerAdapter createListener() {
        RVM1ListenerAdapter listener = new RVM1ListenerAdapter() {
            public void ready() {
                synchronized(_lock) {
                    _lock.notify();
                }
            }
        };
        _state.getRVM1().addRVM1Listener(listener);
        return listener;
    }

    public abstract Command getClone();

    public abstract boolean areParametersValid();

    protected abstract void parseParameters(StringTokenizer tokenizer) throws Exception;

    protected abstract void appendParameters(StringBuffer sb);

    protected abstract void updateRVM1State();

    protected static RVM1InternalState _state = null;
    protected static RVM1InternalState _backupState = null;
    protected static boolean _simulation = false;
    protected static ArrayList<CommandListener> _listeners = new ArrayList<CommandListener>(2);
    protected String _name;
    protected static Object _lock = new Object();
}
