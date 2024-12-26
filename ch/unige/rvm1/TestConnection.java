/*
 * User: jelmini8
 * Date: Jan 14, 2002
 * Time: 6:28:50 PM
 */
package ch.unige.rvm1;

import javax.swing.*;
import java.io.IOException;
import java.awt.event.*;

public class TestConnection extends Connection {

    public TestConnection(int delay) {
        _timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                notifyState(true);
                //_timer.stop();
            }
        });
        _timer.setRepeats(false);
    }

    protected void sendString(String command) throws IOException {
        _timer.start();
    }

    public void close() {
        //do nothing
    }

    private Timer _timer;
}
