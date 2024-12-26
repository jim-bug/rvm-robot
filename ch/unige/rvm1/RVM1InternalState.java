/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 4:20:14 PM
 */
package ch.unige.rvm1;

class RVM1InternalState implements RVM1MutableState {

    public RVM1InternalState(RVM1 rvm1) {
        _rvm1 = rvm1;
        _basePos = RVM1.BASE_ROTATION_RANGE;
        _shoulderPos = RVM1.SHOULDER_ROTATION_RANGE;
        _elbowPos = RVM1.ELBOW_ROTATION_RANGE;
        _pitchPos = 0;
        _rollPos = RVM1.ROLL_ROTATION_RANGE;
        _gripClosed = false;
        _speed = 4;
        _acceleration = RVM1.LOW_ACCELERATION;
    }

    public float getBasePos() {
        return _basePos;
    }

    public void setBasePos(float basePos) {
        _basePos = basePos;
    }

    public float getShoulderPos() {
        return _shoulderPos;
    }

    public void setShoulderPos(float shoulderPos) {
        _shoulderPos = shoulderPos;
    }

    public float getElbowPos() {
        return _elbowPos;
    }

    public void setElbowPos(float elbowPos) {
        _elbowPos = elbowPos;
    }

    public float getPitchPos() {
        return _pitchPos;
    }

    public void setPitchPos(float pitchPos) {
        _pitchPos = pitchPos;
    }

    public float getRollPos() {
        return _rollPos;
    }

    public void setRollPos(float rollPos) {
        _rollPos = rollPos;
    }

    public boolean isGripClosed() {
        return _gripClosed;
    }

    public void setGripClosed(boolean gripClosed) {
        _gripClosed = gripClosed;
    }

    public int getSpeed() {
        return _speed;
    }

    public void setSpeed(int speed) {
        _speed = speed;
    }

    public String getAcceleration() {
        return _acceleration;
    }

    public void setAcceleration(String acceleration) {
        _acceleration = acceleration;
    }

    public Point3D getPositionYZ() {
        double theta1 = Math.toRadians(_shoulderPos - 30);
        double theta2 = Math.toRadians(_elbowPos - 110);
        double theta3 = Math.toRadians(_pitchPos - 90);

        float y = (float) (RVM1.TOOL_LENGTH * Math.cos(theta1 + theta2 + theta3) +
                RVM1.ELBOW_LINK_LENGTH * Math.cos(theta1 + theta2) +
                RVM1.SHOULDER_LINK_LENGTH * Math.cos(theta1));
        float z = (float) (RVM1.TOOL_LENGTH * Math.sin(theta1 + theta2 + theta3) +
                RVM1.ELBOW_LINK_LENGTH * Math.sin(theta1 + theta2) +
                RVM1.SHOULDER_LINK_LENGTH * Math.sin(theta1));
        return new Point3D(0, y, z + RVM1.BASE_LINK_LENGTH);
    }

    public Point3D getPositionXYZ() {
        Point3D point3D = getPositionYZ();
        double alpha = Math.toRadians(330 - _basePos);
        float x = (float) (point3D.y * Math.cos(alpha));
        float y = (float) (point3D.y * Math.sin(alpha));
        point3D.setLocation(x, y, point3D.z);
        return point3D;
    }

    public Connection getConnection() {
        return _connection;
    }

    public void setConnection(Connection connection) {
        _connection = connection;
    }

    public RVM1 getRVM1() {
        return _rvm1;
    }

    public RVM1MutableState getCopy() {
        RVM1InternalState s = new RVM1InternalState(_rvm1);
        s.setSpeed(_speed);
        s.setAcceleration(_acceleration);
        s.setBasePos(_basePos);
        s.setShoulderPos(_shoulderPos);
        s.setElbowPos(_elbowPos);
        s.setPitchPos(_pitchPos);
        s.setRollPos(_rollPos);
        s.setConnection(_connection);
        s.setGripClosed(_gripClosed);
        return s;
    }

//    private double toRadians(float degrees) {
//        return degrees * Math.PI / 180;
//    }

    private float _basePos;
    private float _shoulderPos;
    private float _elbowPos;
    private float _pitchPos;
    private float _rollPos;
    private boolean _gripClosed;
    private int _speed;
    private String _acceleration;

    private RVM1 _rvm1;
    private Connection _connection;
}
