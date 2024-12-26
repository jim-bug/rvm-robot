/*
 * Created by IntelliJ IDEA.
 * User: jelmini8
 * Date: Jan 15, 2002
 * Time: 12:41:02 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package ch.unige.rvm1.joystick;

import ch.unige.rvm1.*;
import com.centralnexus.input.Joystick;

import java.io.IOException;

public class JoystickHandler {

    public JoystickHandler(AdvancedRVM1 rvm1) throws IOException {
        _rvm1 = rvm1;
        _rvm1.addRVM1Listener(new RVM1ListenerAdapter() {
            public void ready() {
                synchronized (_lock) {
                    System.out.println("notify");
                    _ready = true;
                    _lock.notifyAll();
                }
            }

            public void error(int errType, String command, String message) {
                synchronized (_lock) {
                    System.out.println("notify error");
                    _ready = true;
                    _lock.notifyAll();
                }
            }
        });
        _joystick = Joystick.createInstance();
    }

    public boolean available() {
        return Joystick.isPluggedIn(_joystick.getID());
    }

    public void start() {
        _stop = false;
        _pollingThread = new Thread(new Runnable() {
            public void run() {
                while (!_stop || Thread.interrupted()) {
                    try {
                        synchronized (_lock) {
                            if (!_ready) {
                                System.out.println("wait");
                                _lock.wait();
                            }

                            _joystick.poll();
                            int x = (int) (_joystick.getX() * 100);
                            //System.out.println(_joystick.getX());
                            if (_joystick.isButtonDown(Joystick.BUTTON3)) {
                                moveBase(x);
                            }
                            else {
                                moveY(x);
                            }
                            int z = (int) (_joystick.getY() * 100);
                            moveZ(z);
                        }
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        });
        _pollingThread.start();
    }

    private void moveBase(int x) {
        int dx = mapping(x);
        if (dx != 0) {
            System.out.println("move base:" + dx + "; queue:" + _rvm1.getCommandQueue().size());
            _rvm1.moveJoint(dx, 0, 0, 0, 0);
            _ready = false;
        }
    }

    private void moveY(int y) {
        int dy = mapping(y);
        if (dy != 0) {
            RVM1State state = _rvm1.getState();
            Point3D p = state.getPositionYZ();
            System.out.println("move Y:" + dy+" new y:"+ Math.round(p.y+dy)+", z:"+Math.round(p.z));
            _rvm1.drawYZ(Math.round(p.y+dy),Math.round(p.z));
            _ready = false;
        }
    }

    private void moveZ(int z) {
        int dz = mapping(z);
        if (dz != 0) {
            RVM1State state = _rvm1.getState();
            Point3D p = state.getPositionYZ();
            System.out.println("move Z:" + dz+" y:"+ Math.round(p.y)+", new Z:"+Math.round(p.z+dz));
            _rvm1.drawYZ(Math.round(p.y),Math.round(p.z+dz));
            _ready = false;
        }
    }


    public void stop() {
        _stop = true;
        _pollingThread.interrupt();
    }

    private int mapping(int x) {
        int sign = 0;
        if (x!=0) {
            sign = x / Math.abs(x);
        }
        x = Math.abs(x);
//        System.out.println(x+", "+sign);
        if(x < 30) {
            return 0;
        }
        if(x < 60) {
            return sign*1;
        }
        if(x < 75) {
            return sign*5;
        }
        if(x < 90) {
            return sign*15;
        }
        return sign*30;
    }

    private Joystick _joystick;
    private AdvancedRVM1 _rvm1;
    private Thread _pollingThread;
    private boolean _stop;
    private boolean _ready;
    private Object _lock = new Object();
}
