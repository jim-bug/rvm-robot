/*
 * User: jelmini8
 * Date: Dec 1, 2001
 * Time: 3:08:47 PM
 */
package ch.unige.rvm1;


public class AdvancedRVM1 extends SimpleRVM1 {

    public AdvancedRVM1(Connection connection) {
        super(connection);

        _batchMode = true;
        _state = new RVM1InternalState(this);
        _state.setConnection(_connection);
        Command.setRVM1InternalState(_state);
        Command.addCommandListener(new CommandListener() {
            public void error(String command, String message) {
                _queue.purge();
                notifyErrorEvent(RVM1Listener.SOFTWARE_ERROR, command, message);
            }

            public void executed(String command) {
            }
        });
        _interpreter = new CommandInterpreter();
        _queue = new InternalCommandQueue(this);
        nest();
        speed(4, "L");
    }

    public void dispose() {
        _queue.stop();
        _connection.close();
    }

    public void setSequentialMode() {
        _batchMode = false;
    }

    public void setBatchMode() {
        _batchMode = true;
    }

    public void setSimulation(boolean simulation) {
        Command.setSimulation(simulation);
    }

    public boolean isSimulation() {
        return Command.isSimulation();
    }

    public CommandQueue getCommandQueue() {
        return _queue;
    }

    public RVM1State getState() {
        return _state;
    }

    public void sendCommand(String cmd) throws RVM1CommandException {
        addCommandInQueue(_interpreter.getCommand(cmd));
    }

    public void reset() {
        addCommandInQueue(new ResetCommand());
    }

    public void nest() {
        addCommandInQueue(new NestCommand());
    }

    public void speed(int speed, String acceleration) {
        Command cmd = new SpeedCommand(speed, acceleration);
        addCommandInQueue(cmd);
    }

    public void speed(int speed) {
        speed(speed, _state.getAcceleration());
    }

    public void gripOpen() {
        addCommandInQueue(new GripOpenCommand());
    }

    public void gripClose() {
        addCommandInQueue(new GripCloseCommand());
    }

    public void moveJoint(int baseAngle, int shoulderAngle,
            int elbowAngle, int pitchAngle, int rollAngle) {
        Command cmd = new MoveJointCommand(baseAngle, shoulderAngle, elbowAngle, pitchAngle, rollAngle);
        addCommandInQueue(cmd);
    }

    public void drawYZ(int y, int z) {
        Command cmd = new DrawYZCommand(y,z);
        addCommandInQueue(cmd);
    }

    protected void addCommandInQueue(Command rs) {
        if(_batchMode) {
            _queue.addCommand(rs);
        }
        else {
            try {
                _queue.addSequentialCommand(rs);
            }
            catch(RVM1Exception e) {
                notifyErrorEvent(RVM1Listener.SEQUENTIAL_MODE_ERROR, rs.toString(), e.getMessage());
            }
        }
    }

    protected InternalCommandQueue _queue;
    protected boolean _batchMode;
    protected CommandInterpreter _interpreter;
    protected RVM1InternalState _state;
}
