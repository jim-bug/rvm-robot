/*
 * User: jelmini8
 * Date: Jan 9, 2002
 * Time: 6:54:23 PM
 */
package ch.unige.rvm1;

import java.io.*;
import java.net.*;

public class SocketConnection extends Connection {

    public SocketConnection(InetAddress address, int port) throws IOException {
        _socket = new Socket(address, port);
        _outputStream = _socket.getOutputStream();
        _inputStream = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

        _socket.setSoTimeout(5000);
        try {
            _ready = true;
            sendString("RVM1");
            _socket.setSoTimeout(0);
        } catch (InterruptedIOException e) {
            System.out.println("time out");
            throw e;
        }
    }

    protected void sendString(String command) throws IOException {
        if(_ready) {
            _ready = false;
            super.sendString(command);
            //_outputStream.flush();
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String rep = _inputStream.readLine();
                        if(rep.equals("OK")) {
                            notifyState(true);
                        }
                    }
                    catch(IOException e) {
                    }
                    finally {
                        _ready = true;
                    }
                }
            });
            thread.start();
        }
    }

    public void close() {
        _end = true;
        try {
            _outputStream.write("END\n".getBytes());
            _inputStream.close();
            super.close();
            _socket.close();
        }
        catch(IOException e) {
        }
    }

    private Socket _socket;
    private BufferedReader _inputStream;
    private boolean _end;
}
