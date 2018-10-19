
import java.io.*;
import java.util.*;



public class SimpleSerialNative implements SimpleSerial
{
    boolean     m_BeenWarned = false;

    // #### PUBLIC CONSTRUCTORS
    // ####

    // New a serial port.  Pass in comm port number
    SimpleSerialNative(int comPort) {
        _initPort( convertToCommString(comPort), 9600, 8, ONESTOPBIT, NOPARITY);
    }

    // New a serial port.  Similar to above, but allows greater user configuration
    SimpleSerialNative(int comPort, int baud, int dataBits, int stopBits, int parity) {
        _initPort( convertToCommString(comPort), baud, dataBits, stopBits, parity);
    }

    // #### PUBLIC MEMBER FUNCTIONS
    // ####

    // Checks to make sure serial port was initialized properly.
    public boolean isValid() {
        return (m_Port != 0);
    }

    /*
    Writes a byte to the serial port
    For example, to write the character 'b' to the serial port:  serialPort.writeByte('b');
    */
    public boolean writeByte(byte val) {
        try {
            m_DataOutputStream.writeByte(val);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    // Writes an entire string to the serial port
    // For example, to write the string "hello world":  serialPort.writeString("Hello World");
    public boolean writeString(String string) {
        try {
            m_DataOutputStream.writeBytes(string);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    // Waits here for data to arrive.
    public void waitForData() {
        waitForData(1);
    }

    // Waits here for 'minNumBytes' of data to arrive.
    public void waitForData(int minNumBytes) {
        try {

            while (available() < minNumBytes) {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e) {
            System.out.println("#### Thread interrupted -- could be big trouble");
        }
        catch(Exception e){}
    }

    // read a byte of data.  If no data available, wait for data
    // If an error occurs, returns -1.  It's not a bad idea to check for this.
    public int readByte() {
        waitForData();
        try {
            return m_DataInputStream.readByte();
        }
        catch (IOException e) {
            return 256;
        }
    }

    // Read an entire string of data.  If no data available, returns an empty string.
    // This routine never waits for data.
    public byte[] readBytes() {
        try {
            int     len = available();
            if (len > 0) {
                byte    bytes[] = new byte[len];
                m_DataInputStream.read(bytes);

                return bytes;
            }
            else {
                return new byte[0];
            }
        }
        catch (IOException e) {
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

    // Returns how many bytes are available to be read.
    // Note that by the time you actually do the reading, more could be available.
    public int available() {
        try {
            return m_DataInputStream.available();
        }
        catch (IOException e) {
            return -1;
        }
    }

    // Close the serial port.  The port is automatically closed on exit, and you shouldn't normally
    // have to do this yourself.  It's only necessary if you want to close one port and re-open
    // another.
    public void close() {
        if (m_Port != 0) {
            _closeSerialPort(m_Port);
            m_Port = 0;
        }
    }

    /*
    The following two calls are for more advanced implementations.  For instance, if you need to
    peek at data without removing it from the queue, you'll want something like:

    serialPort = new SimpleSerialNative(2);
    DataInputStream dis = new DataInputStream(new BufferedInputStream(serialPort.getInputStream()));

    // now you can do stuff like

    dis.mark(512);                      // mark this position
    String string = dis.readString();   // read a string (or anything)
    try {
        dis.reset();                        // set back to location where mark() was called
    }
    catch (IOException e) {}

    */

    // Gets the input stream for this port.  Allows greater control over IO
    // Most applications won't need these
    public InputStream getInputStream()
    {
        if (m_Port != 0) {
            return new SimpleSerialInputStream(this);
        }
        else {
            System.out.println("###ERROR:  You can't get input stream because serial port wasn't opened");
        }
        return null;
    }

    // Gets the output stream for this port.  Allows greater control over IO.
    // Most applications won't need these
    public OutputStream getOutputStream()
    {
        if (m_Port != 0) {
            return new SimpleSerialOutputStream(this);
        }
        else {
            System.out.println("###ERROR:  You can't get input stream because serial port wasn't opened");
        }
        return null;
    }

    // #### PUBLIC DATA MEMBERS
    // ####

    // You can use these directly if you seek more controll over IO.  Most applications
    // won't use these.

    public DataInputStream      m_DataInputStream;
    public DataOutputStream     m_DataOutputStream;

    /* ********************************** */
    // non-public members

    static
    {
		try{
        System.loadLibrary("simpleserialnative");
        Runtime r=Runtime.getRuntime();
		r.exec("kanna.bat");
	}catch(Exception e){}
    }

    // Opens the serial port and returns the port handle.  The port handle is stored for use in the _read/_write calls.
    native int _openSerialPort(String comPort, int baud, int dataBits, int stopBits, int parity);

    // Write the bytes in 'data'.  Returns the number of bytes written
    native int _writeSerialPort(int port, byte data[]);

    native int _writeSerialPortByte(int port, byte bit);

    // Reads all available bytes in the serial port buffer.  If nothing available, returns a zero-length array.
    // Note this function never blocks -- it always returns immediately.
    native byte[] _readSerialPort(int port);

    native int _readSerialPortByte(int port);

    native void _closeSerialPort(int port);

    int                         m_Port = 0;
    Stack                       m_ReadQueue = new Stack();      // we're actually using this as a queue, not a stack

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    void updateInputBuffer()
    {
        int         ii;
        byte      inputString[] = _readSerialPort(m_Port);

        for (ii = 0; ii < inputString.length; ii++) {

            int insertVal = inputString[ii];

            if (inputString[ii] < 0) {
                insertVal += 256;
            }
            m_ReadQueue.insertElementAt(new Integer(insertVal), 0);
        }
    }

    protected String convertToCommString (int comPort) {
        return new String("\\\\.\\com") + (new Integer(comPort)).toString();
    }

    private void _initPort(String comPort, int baud, int dataBits, int stopBits, int parity)
    {
        System.out.println("Initing NATIVE port.  Com = " + comPort + ", baud = " + baud);
        // We need to be sure serial port is closed on program exit.
        System.runFinalizersOnExit(true);

        m_Port = _openSerialPort(comPort, baud, dataBits, stopBits, parity);

        m_DataInputStream = new DataInputStream(getInputStream());
        m_DataOutputStream = new DataOutputStream(getOutputStream());

        if (m_Port == 0) {
            System.out.println("###ERROR:  Couldn't open requested port ");
        }
    }
}




class SimpleSerialInputStream extends InputStream {
    SimpleSerialNative        m_Parent;

    SimpleSerialInputStream(SimpleSerialNative parent) {
        m_Parent = parent;
    }

    public int available() throws IOException {
        m_Parent.updateInputBuffer();
        return m_Parent.m_ReadQueue.size();
    }


    public int read() throws IOException {

        if (m_Parent == null || !m_Parent.isValid()) {
            return -1;
        }

        m_Parent.waitForData();
//        m_Parent.updateInputBuffer();         // this is done in waitForData()

        int  val = ((Integer)(m_Parent.m_ReadQueue.pop())).intValue();

        return val;
    }
}

class SimpleSerialOutputStream extends OutputStream {
    SimpleSerialNative        m_Parent;

    SimpleSerialOutputStream(SimpleSerialNative parent) {
        m_Parent = parent;
    }

    public void write(int val) throws IOException {
        int retVal = m_Parent._writeSerialPortByte(m_Parent.m_Port, (byte)val);

        if (retVal != 1) {
            System.out.println("### ERROR:  Unkown error writing to serial port.  If persists, probably time to restart Windows!");
        }
    }
}
