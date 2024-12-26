/*
 * User: jelmini8
 * Date: Dec 22, 2001
 * Time: 2:54:42 PM
 */
package ch.unige.rvm1;

import java.util.ArrayList;
import java.util.Iterator;

public class SimpleRVM1 implements RVM1 {

    public SimpleRVM1(Connection connection) {
        _connection = connection;
        _connection.addConnectionListener(new ConnectionListener() {
            public void stateChanged(boolean ready) {
                if (ready) {
                    notifyReadyEvent();
                }
                else {
                    notifyBusyEvent();
                }
            }
        });
    }

    public void sendCommand(String cmd) throws RVM1CommandException {
        _connection.send(cmd);
    }

    public synchronized void addRVM1Listener(RVM1Listener l) {
        _listeners.add(l);
    }

    public synchronized void removeRVM1Listener(RVM1Listener l) {
        _listeners.remove(l);
    }

    protected synchronized void notifyBusyEvent() {
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            RVM1Listener listener = (RVM1Listener) i.next();
            listener.busy();
        }
    }

    protected synchronized void notifyReadyEvent() {
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            RVM1Listener listener = (RVM1Listener) i.next();
            listener.ready();
        }
    }

    protected synchronized void notifyErrorEvent(int errType, String command, String message) {
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            RVM1Listener listener = (RVM1Listener) i.next();
            listener.error(errType, command, message);
        }
    }

    protected ArrayList _listeners = new ArrayList();
    protected Connection _connection;
}
