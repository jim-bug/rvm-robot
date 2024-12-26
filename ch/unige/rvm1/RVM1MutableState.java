/*
 * Created by IntelliJ IDEA.
 * User: jelmini8
 * Date: Jan 11, 2002
 * Time: 5:29:06 PM
 * To change template for new interface use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package ch.unige.rvm1;

public interface RVM1MutableState extends RVM1State {
    void setBasePos(float basePos);

    void setShoulderPos(float shoulderPos);

    void setElbowPos(float elbowPos);

    void setPitchPos(float pitchPos);

    void setRollPos(float rollPos);

    void setGripClosed(boolean gripClosed);

    void setSpeed(int speed);

    void setAcceleration(String acceleration);
}
