/*
 * User: jelmini8
 * Date: Jan 7, 2002
 * Time: 4:12:05 PM
 */
package ch.unige.rvm1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

class InternalCommandQueue implements CommandQueue {

    public InternalCommandQueue(final AdvancedRVM1 rvm1) {
        _rvm1 = rvm1;
        _commands = new Queue();
        _consumer = new CommandConsumer(this);
        _listeners = new ArrayList();

        _rvm1.addRVM1Listener(new RVM1ListenerAdapter() {
            public void ready() {
                _ready = true;
            }

            public void busy() {
                _ready = false;
            }
        });
        Command.addCommandListener(new CommandListener() {
            public void error(String command, String message) {
            }

            public void executed(String command) {
                notifyListeners(command);
            }
        });
        _consumer.start();
    }

    public void addCommand(Command cmd) {
        _commands.add(cmd);
    }

    public void addSequentialCommand(Command cmd) throws RVM1Exception {
        if (_commands.isEmpty()) {
            addCommand(cmd);
        }
        else
            throw new RVM1Exception("Sequential mode: robot busy");
    }

    public void execute() {
        Command command = _commands.remove();
        command.execute();
    }

    public void removeLastCommands(String cmdName) {
        _commands.removeLastCommands(cmdName);
    }

    public void purge() {
        synchronized (_commands) {
            _commands.clear();
        }
    }

    public int size() {
        return _commands.size();
    }

    public boolean isEmpty() {
        return _commands.isEmpty();
    }

    public synchronized void addCommandQueueListener(CommandQueueListener l) {
        _listeners.add(l);
    }

    public synchronized void removeCommandQueueListener(CommandQueueListener l) {
        _listeners.remove(l);
    }

    public void stop() {
        _consumer.halt();
        purge();
    }

    private synchronized void notifyListeners(String cmd) {
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            CommandQueueListener listener = (CommandQueueListener) i.next();
            listener.executed(cmd);
        }
    }

    /**
     * Classe interne pour faciliter la synchronisation
     */
    private class Queue {
        public synchronized void add(Command c) {
            _comms.addFirst(c);
            if (_ready) {
                notifyAll();
            }
        }

        public synchronized Command remove() {
            while (isEmpty()) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                }
            }
            return (Command) _comms.removeLast();
        }

        public synchronized void clear() {
            _comms.clear();
        }

        public synchronized int size() {
            return _comms.size();
        }

        public synchronized boolean isEmpty() {
            return _comms.isEmpty();
        }

        public synchronized void removeLastCommands(String cmdName) {
            ListIterator i = _comms.listIterator();
            while (i.hasNext()) {
                Command command = (Command) i.next();
                if (command.isCommand(cmdName)) {
                    i.remove();
                }
                else {
                    return;
                }
            }
        }

        private LinkedList _comms = new LinkedList();
    }

    private Queue _commands;
    private AdvancedRVM1 _rvm1;
    private CommandConsumer _consumer;
    private ArrayList _listeners;
    private boolean _ready = true;
}
