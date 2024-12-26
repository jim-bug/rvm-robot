/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 7:09:12 PM
 */
package ch.unige.rvm1;

import java.util.StringTokenizer;

class ResetCommand extends CompositeCommand {

    public ResetCommand() {
        addCompositeCommand(new NestCommand());
    }

    protected void setName() {
        _name = "RS";
    }

    protected void parseParameters(StringTokenizer tokenizer) throws Exception {
    }

    public boolean areParametersValid() {
        return true;
    }

    protected void appendParameters(StringBuffer sb) {
    }

    protected void updateRVM1State() {
    }

    public Command getClone() {
        return new ResetCommand();
    }
}
