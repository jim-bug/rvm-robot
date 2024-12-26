/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 5:33:33 PM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;

class SpeedCommand extends Command {

    public SpeedCommand() {
        super();
    }

    public SpeedCommand(int speed, String acceleration) {
        super();
        _speed = speed;
        _acceleration = acceleration;
    }

    protected void setName() {
        _name = "SP";
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
        String token = tokenizer.nextToken();
        _speed = Integer.parseInt(token);
        if (tokenizer.hasMoreTokens()) {
            _acceleration = tokenizer.nextToken();
        }
        else {
            _acceleration = null;
        }
    }

    protected void appendParameters(StringBuffer sb) {
        sb.append(" ");
        sb.append(_speed);
        if (_acceleration != null) {
            sb.append(",");
            sb.append(_acceleration);
        }
    }

    public boolean areParametersValid() {
        return (inRange(_speed, RVM1.MIN_SPEED, RVM1.MAX_SPEED) &&
                (RVM1.HIGH_ACCELERATION.equals(_acceleration) ||
                RVM1.LOW_ACCELERATION.equals(_acceleration) ||
                _acceleration == null));
    }

    protected void updateRVM1State() {
        _state.setSpeed(_speed);
        _state.setAcceleration(_acceleration);
    }

    public Command getClone() {
        return new SpeedCommand(_speed, _acceleration);
    }

    private int _speed = -1;
    private String _acceleration;
}
