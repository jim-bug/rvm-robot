/*
 * User: jelmini8
 * Date: Jan 9, 2002
 * Time: 6:37:47 PM
 */
package ch.unige.rvm1;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Connection {

    public void send(String command) {
        try {
            notifyState(false);
            sendString(command);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    protected void sendString(String command) throws IOException {
    
        _outputStream.write(command.getBytes());
        // _outputStream.write('\n');
    }

    public void close() {
        try {
            _outputStream.close();
        }
        catch (IOException e) {
        }
    }

    public synchronized void addConnectionListener(ConnectionListener l) {
        _listeners.add(l);
    }

    public synchronized void removeConnectionListener(ConnectionListener l) {
        _listeners.remove(l);
    }

    protected synchronized void notifyState(boolean state) {
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            ConnectionListener listener = (ConnectionListener) i.next();
            listener.stateChanged(state);
        }
    }

    protected OutputStream _outputStream;
    protected ArrayList _listeners = new ArrayList();
    protected boolean _ready;
}
