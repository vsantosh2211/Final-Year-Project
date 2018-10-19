


import java.io.*;
import java.net.*;
import java.util.*;
 
public class HttpTunnel 
{
   static Socket ss;
   static InputStream in;
   static ServerSocket ser;
    /** Creates a new instance of HttpTunnel */
    private HttpTunnel() {
    }
    public  static String undoHttpTunneling(InputStream in) throws Exception
    {
        int c=0;
        String str=" ";
        while((c=in.read())!='\r'){
            str+=(char)c;
        }
        System.out.println("String ... "+str.trim());
		//if(str.trim().startsWith("GET")){
		StringTokenizer st=new StringTokenizer(str.trim()," ");
        st.nextToken();     // Removing GET
       return st.nextToken(); //returning the argument
	
    }

	public static void doHttpTunneling(PrintWriter out, String data) throws Exception
    {
        out.print("HTTP/1.0 200 OK\r\n");
        out.print("Content-Type: application/octet-stream\r\n");
        out.print("Content-Length:"+data.length()+"\r\n");
        out.print("\r\n");
        out.print(data);
        out.close();
        System.out.println("Data: "+data+"\nData Trasmitted");
   }
    
    }
