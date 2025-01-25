/*
 * User: jelmini8
 * Date: Dec 1, 2001
 * Time: 3:08:47 PM
 */
package ch.unige.rvm1;
/*
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

*/
import java.io.IOException;
import gnu.io.*;        // temporary fix for RXTX


public class SerialConnection extends Connection {

    public SerialConnection(String portName, String appName) throws RVM1Exception {
        try {
            _portId = CommPortIdentifier.getPortIdentifier(portName);
            _serialPort = (SerialPort) _portId.open(appName, 2000);
            _outputStream = _serialPort.getOutputStream();
            _serialPort.setSerialPortParams(9600, SerialPort.DATABITS_7,
                    SerialPort.STOPBITS_2, SerialPort.PARITY_EVEN);

            _serialPort.addEventListener(new SerialPortEventListener() {
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPortEvent.CD &&
                            _serialPort.isCD() && !_ready) {
                        _ready = true;
                        notifyState(true);
                    }
                }
            });
            _serialPort.notifyOnCarrierDetect(true);
        }
        catch (Exception e) {
            throw new RVM1Exception(e.getMessage());
        }
    }

    protected void sendString(String command) throws IOException {
        super.sendString(command);
        _ready = false;
    }

    public void close() {
        _serialPort.close();
        super.close();
    }

    private CommPortIdentifier _portId;
    private SerialPort _serialPort;
}