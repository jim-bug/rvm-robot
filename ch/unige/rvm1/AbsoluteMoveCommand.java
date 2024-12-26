/*
 * Created by IntelliJ IDEA.
 * User: jelmini8
 * Date: Jan 11, 2002
 * Time: 7:05:56 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package ch.unige.rvm1;


class AbsoluteMoveCommand extends MoveJointCommand {

    public AbsoluteMoveCommand() {
    }

    public AbsoluteMoveCommand(float baseAngle, float shoulderAngle, float elbowAngle, float pitchAngle, float rollAngle) {
        super(baseAngle, shoulderAngle, elbowAngle, pitchAngle, rollAngle);
    }

    protected void setName() {
        _name = "AM";
    }

    protected void sendCommand() {
        String relativeCommand = toRelative();
        _state.getConnection().send(relativeCommand);
    }

    public Command getClone() {
        return new AbsoluteMoveCommand(_baseAngle, _shoulderAngle, _elbowAngle, _pitchAngle, _rollAngle);
    }

    public boolean areParametersValid() {
        return (inRange(_baseAngle, 0, RVM1.BASE_ROTATION_RANGE) &&
                inRange(_shoulderAngle, 0, RVM1.SHOULDER_ROTATION_RANGE) &&
                inRange(_elbowAngle, 0, RVM1.ELBOW_ROTATION_RANGE) &&
                inRange(_pitchAngle, 0, RVM1.PITCH_ROTATION_RANGE) &&
                inRange(_rollAngle, 0, RVM1.ROLL_ROTATION_RANGE));
    }

    protected void updateRVM1State() {
        _state.setBasePos(_baseAngle);
        _state.setShoulderPos(_shoulderAngle);
        _state.setElbowPos(_elbowAngle);
        _state.setPitchPos(_pitchAngle);
        _state.setRollPos(_rollAngle);
    }

    private String toRelative() {
        StringBuffer sb = new StringBuffer();
        sb.append("MJ ");
        sb.append(_baseAngle - _state.getBasePos());
        sb.append(',');
        sb.append(_shoulderAngle - _state.getShoulderPos());
        sb.append(',');
        sb.append(_elbowAngle - _state.getElbowPos());
        sb.append(',');
        sb.append(_pitchAngle - _state.getPitchPos());
        sb.append(',');
        sb.append(_rollAngle - _state.getRollPos());
        return sb.toString();
    }
}
