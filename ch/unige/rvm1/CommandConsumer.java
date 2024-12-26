/*
 * User: jelmini8
 * Date: Jan 7, 2002
 * Time: 6:04:18 PM
 */
package ch.unige.rvm1;

class CommandConsumer extends Thread {

    public CommandConsumer(InternalCommandQueue queue) {
        _queue = queue;
    }

    public void run() {
        while (!_end) {
            _queue.execute();
        }
    }

    public void halt() {
        _end = true;
    }

    private InternalCommandQueue _queue;
    private boolean _end;
}
