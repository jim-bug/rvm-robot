/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 7:20:25 PM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;
import java.text.DecimalFormat;

class MoveJointCommand extends Command {

    public MoveJointCommand() {
    }

    public MoveJointCommand(float baseAngle, float shoulderAngle, float elbowAngle, float pitchAngle, float rollAngle) {
        super();
        _baseAngle = baseAngle;
        _shoulderAngle = shoulderAngle;
        _elbowAngle = elbowAngle;
        _pitchAngle = pitchAngle;
        _rollAngle = rollAngle;
    }

    protected void setName() {
        _name = "MJ";
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
        String token = tokenizer.nextToken();
        _baseAngle = (float) Math.round(Float.parseFloat(token)*10)/10;
        token = tokenizer.nextToken();
        _shoulderAngle = (float) Math.round(Float.parseFloat(token)*10)/10;
        token = tokenizer.nextToken();
        _elbowAngle = (float) Math.round(Float.parseFloat(token)*10)/10;
        token = tokenizer.nextToken();
        _pitchAngle = (float) Math.round(Float.parseFloat(token)*10)/10;
        token = tokenizer.nextToken();
        _rollAngle = (float) Math.round(Float.parseFloat(token)*10)/10;
    }

    public boolean areParametersValid() {
        return (inRange(_state.getBasePos() + _baseAngle, 0, RVM1.BASE_ROTATION_RANGE) &&
                inRange(_state.getShoulderPos() + _shoulderAngle, 0, RVM1.SHOULDER_ROTATION_RANGE) &&
                inRange(_state.getElbowPos() + _elbowAngle, 0, RVM1.ELBOW_ROTATION_RANGE) &&
                inRange(_state.getPitchPos() + _pitchAngle, 0, RVM1.PITCH_ROTATION_RANGE) &&
                inRange(_state.getRollPos() + _rollAngle, 0, RVM1.ROLL_ROTATION_RANGE));
    }

    protected void appendParameters(StringBuffer sb) {
        DecimalFormat df = new DecimalFormat();
        sb.append(" ");
        sb.append(df.format(_baseAngle));
        sb.append("  ,");
        sb.append(df.format(_shoulderAngle));
        sb.append("  ,");
        sb.append(df.format(_elbowAngle));
        sb.append("  ,");
        sb.append(df.format(_pitchAngle));
        sb.append("  ,");
        sb.append(df.format(_rollAngle));
    }

    protected void updateRVM1State() {
        _state.setBasePos(_state.getBasePos() + _baseAngle);
        _state.setShoulderPos(_state.getShoulderPos() + _shoulderAngle);
        _state.setElbowPos(_state.getElbowPos() + _elbowAngle);
        _state.setPitchPos(_state.getPitchPos() + _pitchAngle);
        _state.setRollPos(_state.getRollPos() + _rollAngle);
    }

    public Command getClone() {
        return new MoveJointCommand(_baseAngle, _shoulderAngle, _elbowAngle, _pitchAngle, _rollAngle);
    }

    protected float _baseAngle;
    protected float _shoulderAngle;
    protected float _elbowAngle;
    protected float _pitchAngle;
    protected float _rollAngle;
}
