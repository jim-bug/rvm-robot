/*
 * User: Carlo Jelmini
 * Date: Jan 16, 2002
 * Time: 1:25:28 AM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;

public class DrawYZCommand extends AbsoluteMoveCommand {

    public DrawYZCommand() {
    }

    public DrawYZCommand(int y, int z) {
        super();
        _y = y;
        _z = z;
        inverseKinematics();
    }

    protected void setName() {
        _name = "DY";
    }

    protected void sendCommand() {
        String relativeCommand = toAbsoluteMove();
        _state.getConnection().send(relativeCommand);
    }

    public Command getClone() {
        return new DrawYZCommand(_y, _z);
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
        String token = tokenizer.nextToken();
        _y = Integer.parseInt(token);
        token = tokenizer.nextToken();
        _z = Integer.parseInt(token);
        inverseKinematics();
    }

    public boolean areParametersValid() {
        return (inRange(_baseAngle, 0, RVM1.BASE_ROTATION_RANGE) &&
                inRange(_shoulderAngle, 0, RVM1.SHOULDER_ROTATION_RANGE) &&
                inRange(_elbowAngle, 0, RVM1.ELBOW_ROTATION_RANGE) &&
                inRange(_pitchAngle, 0, RVM1.PITCH_ROTATION_RANGE) &&
                inRange(_rollAngle, 0, RVM1.ROLL_ROTATION_RANGE));
    }

//    protected void updateRVM1State() {
//        _state.setBasePos(_baseAngle);
//        _state.setShoulderPos(_shoulderAngle);
//        _state.setElbowPos(_elbowAngle);
//        _state.setPitchPos(_pitchAngle);
//        _state.setRollPos(_rollAngle);
//    }

    protected void appendParameters(StringBuffer sb) {
        sb.append(" ");
        sb.append(_y);
        sb.append(",");
        sb.append(_z);
        sb.append(" (");
        super.appendParameters(sb);
        sb.append(")");
    }

    private void inverseKinematics() {
        _baseAngle = _state.getBasePos();
        _rollAngle = _state.getRollPos();

        int z = _z - RVM1.BASE_LINK_LENGTH;
        double phi = Math.atan2((double) z, (double) _y);

        double a = (double) (_y * _y + z * z -
                RVM1.SHOULDER_LINK_LENGTH * RVM1.SHOULDER_LINK_LENGTH -
                RVM1.ELBOW_LINK_LENGTH * RVM1.ELBOW_LINK_LENGTH/*-
                RVM1.TOOL_LENGTH * RVM1.TOOL_LENGTH*/) /
                (double) (2 * RVM1.SHOULDER_LINK_LENGTH * RVM1.ELBOW_LINK_LENGTH/**RVM1.TOOL_LENGTH*/);
//        System.out.println("a:" + a);

        double a2 = a * a;

        double theta2;
        double b = 0.0;
        if(a2 > 1.0) {
            a2 = 1.0;
        }

        b = Math.sqrt(1.0 - a2);
//        System.out.println("b:" + b);
//        int y = (int)(_y - Math.cos(phi) * RVM1.TOOL_LENGTH);
//        z -= Math.sin(phi) * RVM1.TOOL_LENGTH;

        int c = 0;
        do {
            b = -b;
            theta2 = Math.atan2(-b, a);
//            System.out.println("theta2:" + theta2);

            double theta1 = Math.atan2(z, _y) -
                    Math.atan2(RVM1.ELBOW_LINK_LENGTH *
                    Math.sin(theta2), RVM1.SHOULDER_LINK_LENGTH + RVM1.ELBOW_LINK_LENGTH * Math.cos(theta2));

            double theta3 = phi - theta1 - theta2;

            _shoulderAngle = (float) Math.toDegrees(theta1) + 30;
            _elbowAngle = (float) Math.toDegrees(theta2) + 110;
            _pitchAngle = (float) Math.toDegrees(theta3) + 90;
//            System.out.println(_shoulderAngle + "," + _elbowAngle + "," + _pitchAngle);
            c++;
        } while(!areParametersValid() && c < 2);


    }

    private String toAbsoluteMove() {
        StringBuffer sb = new StringBuffer();
        sb.append("MJ ");
        sb.append((int)Math.round(_baseAngle*10)/10 - _state.getBasePos());
        sb.append(',');
        sb.append((int)Math.round(_shoulderAngle*10)/10 - _state.getShoulderPos());
        sb.append(',');
        sb.append((int)Math.round(_elbowAngle*10)/10 - _state.getElbowPos());
        sb.append(',');
        sb.append((int)Math.round(_pitchAngle*10)/10 - _state.getPitchPos());
        sb.append(',');
        sb.append((int)Math.round(_rollAngle*10)/10 - _state.getRollPos());
        System.out.println(sb);
        return sb.toString();
    }

    private int _y, _z;
}
