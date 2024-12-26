/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 4:54:10 PM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;

class NestCommand extends Command {

    protected void setName() {
        _name = "NT";
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
    }

    protected void appendParameters(StringBuffer sb) {
    }

    public boolean areParametersValid() {
        return true;
    }

    public Command getClone() {
        return new NestCommand();
    }

    protected void updateRVM1State() {
        _state.setBasePos(RVM1.BASE_ROTATION_RANGE);
        _state.setShoulderPos(RVM1.SHOULDER_ROTATION_RANGE);
        _state.setElbowPos(RVM1.ELBOW_ROTATION_RANGE);
        _state.setPitchPos(0);
        _state.setRollPos(RVM1.ROLL_ROTATION_RANGE);
        _state.setGripClosed(false);
    }
}
