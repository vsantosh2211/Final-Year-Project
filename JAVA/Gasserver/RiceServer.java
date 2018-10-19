import java.io.*;
import java.util.*;
import javax.swing.*;
import java.sql.*;
import com.nura.sms.SendSMS;





public class RiceServer{

Connection con=null;
Statement stmt=null;
ResultSet rs=null;
String c,bname,bauthor;
    SimpleSerial            m_SerialPort = null;                                // Serial port
    int                     m_PortIndex;                                        // Which comm port to use (1-based value -- there is no Comm0)
    boolean                 m_IsNative = true;
    String inputString ;
   static String klmString=null;
StringTokenizer stringToken = null;
String replyMessage = "";
String mobileNo = "";
String name = null;
int fine;
OutputStream outputStream;
boolean status=true;
String vehicleNo,gpslocation;
   // Use local code, or use JavaComm from Sun
    static final String     m_PrefsFileName = new String("JavaTerm.pref");      // name of preferences file
public static void main(String[] argh) {


        new RiceServer();

				

    }



    private void initSerialPort() throws IOException {


        if (m_SerialPort != null) {
            m_SerialPort.close();
            m_SerialPort = null;
        }


        if (m_IsNative) {
            m_SerialPort = new SimpleSerialNative(3);
        }


        if (!m_SerialPort.isValid()) {
            throw (new IOException("Serial port not opened"));
        }
    }


    RiceServer() {
        int ii;



        try {

            DataInputStream prefs = new DataInputStream(new FileInputStream(m_PrefsFileName));

            m_PortIndex = prefs.readInt();
            m_IsNative = prefs.readBoolean();

            if (m_PortIndex < 0 ) {
                throw new IOException(m_PrefsFileName + " is corrupt");
            }

            initSerialPort();
        }

        catch(IOException e) {
            System.out.println("preferences file 'JavaTerm.pref' not found / didn't open or there was a problem opening serial port.  Searching for serial port");
			int m=4;

            find_open_serial_port:
            for (ii = 0; ii < m; ii++) {
                try {
                    m_PortIndex = ii + 1;             // This is the serial port we want to open
                    initSerialPort();                       // Try opening this serial port.  Throws exception if there's a problem
                    System.out.println("Opening serial port Comm" + m_PortIndex);
                    break find_open_serial_port;            // If we haven't thrown an exception, we're done
                }
                catch (IOException ee) {                     // wind up here if initSerialPort() above has a problem
                    if (ii == m - 1) {
                        System.out.println("Couldn't open any serial ports");
                        System.exit(0);                     // can't open any serial ports.
                    }
                }
            }
        }


			 
        // Infinite loop.  WindowListener above will break us out of loop with call to System.exit(0);
        for (;true;) {
            // Get any pending characters from serial port.
            // Returns empty string if there's nothing to read.
            // This is in contrast to readByte() which patiently waits for data.

            if (m_SerialPort != null) {
                 inputString = m_SerialPort.readString();


try{
	    if(inputString.intern()=="")
			{
			   System.out.println("No DataFound");
			   System.out.println("NO DATAFOUND");
								outputStream = m_SerialPort.getOutputStream();								    
								outputStream.write("B".getBytes()); 

            }else{

					    try {
							System.out.println(inputString);

							File file = new File("help.txt");
							boolean bool = file.exists();
							if(bool){
								file.delete();
							}

							FileOutputStream fout1 = new FileOutputStream("help.txt",true);
							fout1.write(inputString.getBytes());
							fout1.close();
							//Thread.sleep(2000);
							FileInputStream fin = new FileInputStream("help.txt");	
							byte b[] = new byte[fin.available()];
							fin.read(b);
							fin.close();
							String data = new String(b);
							System.out.println("Data....."+data);

							//Thread.sleep(10000);
							

							if(data.startsWith("R")){
								if(status){
								con = getConnection();
								if(con==null){
									System.out.println("Connection not found");
									System.exit(0);
								}else{

									stmt = con.createStatement();
									
									int i = stmt.executeUpdate("insert into booking_details(category, date_d, customer_no) values('RICE',now(),'1234567890')");
									if(i>0){
										System.out.println("Values are inserted....");
									
									
												SendSMS sendSMS=new SendSMS();
									sendSMS.sendSMS("7010739604", "Rice to be delivered to this customer id=123456");
									sendSMS.sendSMS("9789029498", "Rice Booked..");
										
										//	doit(gpslocation);
                                       status=false;
									   }else{
										   System.out.println("Values are not inserted....");
									}
								}
								}
							}
							
							
								
							

							}catch (Exception e){
					 			e.printStackTrace();
					 			System.out.println(e);
	 	                    }System.out.println(inputString);
		
			}
}catch(Exception ex){System.out.println(ex);}





            }

            try {

			
				Thread.sleep(1000);

            }
            catch (InterruptedException e) {}

        }
    }

	
	

	
	
	

	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/gasbooking", "root", "admin");
			return connection;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
		return null;
	}

}
