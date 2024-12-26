/*
 * Created by IntelliJ IDEA.
 * User: jelmini8
 * Date: Jan 10, 2002
 * Time: 1:31:41 PM
 * To change template for new interface use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package ch.unige.rvm1;

public interface CommandQueueListener {
    void executed(String command);
}
