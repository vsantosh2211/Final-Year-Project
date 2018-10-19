import javax.comm.*;
import java.io.*;



public class SimpleSerialJava implements SimpleSerial {
    DataInputStream     m_DIS = null;
    DataOutputStream    m_DOS = null;
    SerialPort          m_SerialPort = null;
    boolean             m_BeenWarned = false;

    // New a serial port.  Pass in comm port number
    SimpleSerialJava(int comPort) {
        _initPort(comPort, 9600, 8, ONESTOPBIT, NOPARITY);
    }

    // New a serial port.  Similar to above, but allows greater user configuration
    SimpleSerialJava(int comPort, int baud, int dataBits, int stopBits, int parity) {
        _initPort(comPort, baud, dataBits, stopBits, parity);
    }

    public void _initPort(int comPort, int baud, int dataBits, int stopBits, int parity) {

        CommPortIdentifier  cpi = null;
        CommPort            cp = null;
        

        System.out.println("Initing JAVA port.  Com = " + comPort + ", baud = " + baud);

        try {
            cpi = CommPortIdentifier.getPortIdentifier("COM" + comPort);
        }
        catch (NoSuchPortException e) {
            System.out.println("#### ERROR:  no such port: (" + comPort + ")");
            return;
        }

        try {
            cp = cpi.open("SimpleSerial", 1000);
        }
        catch (PortInUseException e) {
            System.out.println("Port in use");
            return;
        }

        if (cp instanceof SerialPort) {
            m_SerialPort = (SerialPort)cp;

            try {
                byte stopBitsLookup[] = {1, 3, 2};

                m_SerialPort.setSerialPortParams(baud, dataBits, stopBitsLookup[stopBits], parity);
                m_SerialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            }
            catch (UnsupportedCommOperationException e) {
                System.out.println("#### ERROR:  Unsupported comm operation exception");
                return;
            }

            try {
                m_DIS = new DataInputStream(m_SerialPort.getInputStream());
                m_DOS = new DataOutputStream(m_SerialPort.getOutputStream());
            }
            catch (IOException e) {
                System.out.println("### ERROR:  Could't open data stream");
                m_DIS = null;
                m_DOS = null;
            }
        }
    }

    public void close() {
        if (m_SerialPort != null) {
            m_SerialPort.close();
        }
        m_SerialPort = null;
    }

    public boolean writeByte(byte byteVal) {
        try {
            m_DOS.writeByte(byteVal);
            return true;
        }
        catch (IOException e) {
            System.out.println("### IO ERROR WRITING BYTE");
            System.out.println("### error is:  " + e);
            return false;
        }
    }

    public int readByte() {
        try {
            return m_DIS.readByte();
        }
        catch (IOException e) {
            System.out.println("### IO ERROR READING BYTE");
            System.out.println("### error is:  " + e);
            return 0;
        }
    }

    public OutputStream getOutputStream() {
        return m_DOS;
    }

    public InputStream getInputStream() {
        return m_DIS;
    }

    public boolean isValid() {
        return (m_DOS != null && m_DIS != null);
    }

    public int available() {
        try {
            return m_DIS.available();
        }
        catch (IOException e) {
            System.out.println("### ERROR:  Got IOException in avaialable");
            m_DIS = null;
            return -1;
        }
    }

    public byte[] readBytes() {
        try {
            int available = m_DIS.available();
            if (available > 0) {
                byte        data[] = new byte[available];

                m_DIS.read(data);
                return data;

            }
            return new byte[0];
        }
        catch (IOException e) {
            System.out.println("### IO ERROR reading multiple bytes");
            System.out.println("### Error is:  " + e);

            return new byte[0];
        }
    }

    public String readString() {
        int     ii;

        byte data[] = readBytes();

        if (!m_BeenWarned) {
            for (ii = 0; ii < data.length; ii++) {
                if (!m_BeenWarned && data[ii] < 0) {
                    m_BeenWarned = true;
                    System.out.println("--> #### WARNING:  You are reading string data with values less than zero.");
                    System.out.println("--> #### This can be dangerous as Char->Byte remapping can change negative values!");
                    System.out.println("--> #### It's MUCH safer to use readBytes[] instead");
                    System.out.println("--> #### You will only receive this warning ONCE");
                    System.out.println("--> ####");
                }
            }
        }

        return new String(data);

    }




}