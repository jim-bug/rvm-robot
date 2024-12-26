/*
 * User: jelmini8
 * Date: Dec 22, 2001
 * Time: 3:33:11 PM
 */
package ch.unige.rvm1;

public interface RVM1Listener {
    void ready();

    void busy();

    void error(int errType, String command, String message);

    int HARDWARE_ERROR = 1;
    int SOFTWARE_ERROR = 2;
    int SEQUENTIAL_MODE_ERROR = 3;
}
