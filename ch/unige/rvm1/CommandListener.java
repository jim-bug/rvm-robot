/*
 * User: jelmini8
 * Date: Jan 8, 2002
 * Time: 7:03:23 PM
 */
package ch.unige.rvm1;

interface CommandListener {
    void error(String command, String message);

    void executed(String command);
}
