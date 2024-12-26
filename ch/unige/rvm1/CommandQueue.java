/*
 * User: jelmini8
 * Date: Jan 7, 2002
 * Time: 4:16:14 PM
 */
package ch.unige.rvm1;

public interface CommandQueue {
    void removeLastCommands(String cmdName);

    void purge();

    int size();

    boolean isEmpty();

    void addCommandQueueListener(CommandQueueListener l);

    void removeCommandQueueListener(CommandQueueListener l);
}
