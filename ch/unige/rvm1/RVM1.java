/*
 * User: jelmini8
 * Date: Jan 7, 2002
 * Time: 5:30:00 PM
 */
package ch.unige.rvm1;

public interface RVM1 {

    int MIN_SPEED = 0;
    int MAX_SPEED = 9;
    String HIGH_ACCELERATION = "H";
    String LOW_ACCELERATION = "L";
    float BASE_ROTATION_RANGE = 300.0F;
    float SHOULDER_ROTATION_RANGE = 130.0F;
    float ELBOW_ROTATION_RANGE = 110.0F;
    float PITCH_ROTATION_RANGE = 180.0F;
    float ROLL_ROTATION_RANGE = 360.0F;
    int BASE_LINK_LENGTH = 300;
    int SHOULDER_LINK_LENGTH = 250;
    int ELBOW_LINK_LENGTH = 160;
    int TOOL_LENGTH = 0;//72;//140;

    void sendCommand(String cmd) throws RVM1CommandException;

    void addRVM1Listener(RVM1Listener l);

    void removeRVM1Listener(RVM1Listener l);
}
