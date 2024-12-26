/*
 * User: jelmini8
 * Date: Jan 9, 2002
 * Time: 12:02:58 PM
 */
package ch.unige.rvm1;

import java.awt.*;

public interface RVM1State {
    float getBasePos();

    float getShoulderPos();

    float getElbowPos();

    float getPitchPos();

    float getRollPos();

    boolean isGripClosed();

    int getSpeed();

    String getAcceleration();

    Point3D getPositionYZ();

    Point3D getPositionXYZ();

    RVM1MutableState getCopy();
}
