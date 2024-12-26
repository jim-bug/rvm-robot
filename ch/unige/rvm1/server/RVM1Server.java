package ch.unige.rvm1.server;

import ch.unige.rvm1.*;

import java.io.*;
import java.net.*;

public class RVM1Server {
    ServerSocket ss;
    Socket s;
    SerialConnection sc;
    BufferedReader in;
    OutputStream out;
    private Object lock = new Object();


    public RVM1Server(int port) {
        try {
            ss = new ServerSocket(port);
            System.out.println(port);
            sc = new SerialConnection("COM2", "JRobot RVM1Server");
            sc.addConnectionListener(new ConnectionListener() {
                public void stateChanged(boolean ready) {
                    synchronized (lock) {
                        if (ready) lock.notifyAll();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            close();
            System.exit(0);
        }
    }

    public void start() {
        while (true) {
            try {
                System.out.println("\nServer is waiting for a client...");
                s = ss.accept();
                System.out.println("\nServer busy.");
                out = s.getOutputStream();
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                String line = in.readLine();
                System.out.println("\nStart: " + line + "\n");
                if (!line.toUpperCase().equals("RVM1")) {
                    System.out.println("Bad identification");
                    close();
                    continue;
                }
                out.write("OK\n".getBytes());

                while (!line.toUpperCase().equals("END")) {
                    line = in.readLine();
                    System.out.println("command: " + line);
                    if (!line.toUpperCase().equals("END")) {
                        sc.send(line);
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                        out.write("OK\n".getBytes());
                    }
                }
                close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public void close() {
        try {
            s.close();
        } catch (IOException e2) {
        }
    }

    public static void main(String args[]) {
        new RVM1Server(Integer.parseInt(args[0])).start();
    }
}
