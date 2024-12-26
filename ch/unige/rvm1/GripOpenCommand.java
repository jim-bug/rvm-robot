/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 7:19:19 PM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;

class GripOpenCommand extends Command {

    protected void setName() {
        _name = "GO";
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
    }

    public boolean areParametersValid() {
        return true;
    }

    protected void appendParameters(StringBuffer sb) {
    }

    protected void updateRVM1State() {
        _state.setGripClosed(false);
    }

    public Command getClone() {
        return new GripOpenCommand();
    }
}
