import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;


public class MainServer implements Runnable{
    
	  static ServerSocket serverSocket = null;
	  static Socket socket = null;
	  
	private Connection con=null;
    private Statement stmt=null;
    private ResultSet rs=null;
   
	boolean fake=false;
boolean expiry=false;
boolean theft=false;
//SimpleWrite sb;
	private String dbRecords = "",purchaseList="",ccNumber,pinNumber;
	private StringTokenizer stringToken = null;
	int availableBalance = 0,purchaseAmt = 0, totalBalance = 0;
	boolean cardAuthenticaionflag = false,balanceflag= false;
	String charset = "UTF-8"; // or "ISO-8859-1"
	  
   String QRData="";

	  PrintWriter out = null;
	  public MainServer(){
		  try{
			  //sb=new SimpleWrite();
			  serverSocket = new ServerSocket(222);
			  
		  }catch(Exception ex){
			  ex.printStackTrace();
			  System.out.println(ex);
		  }
	  }
	  public void run(){
		  try{
		  out =  new PrintWriter(socket.getOutputStream());
		  String str = HttpTunnel.undoHttpTunneling(socket.getInputStream());
		  str = str.substring(1);
		  System.out.println("Value....."+str);
		  
	
		  
		  if(str.startsWith("GETBOOKING")){
			 String dbdata="", record="";
			 boolean status=true;
			   try{
					con = getConnection();
								if(con==null){
									System.out.println("Connection not found");
									System.exit(0);
								}else{
									stmt = con.createStatement();
								rs=	stmt.executeQuery("select category, date_d, customer_no from booking_details");
								while(rs.next()){
									dbdata+=rs.getString(1)+"$"+rs.getString(2)+"$"+rs.getString(3)+"#";
									status=true;
								}
								if(true){
									record=dbdata;
									 HttpTunnel.doHttpTunneling(out,record);
								}
									 HttpTunnel.doHttpTunneling(out,"");
								}
		
			  }
        catch(Exception e){
            e.printStackTrace();};
		  }
	
		  else if(str.startsWith("INVALID")){
			  //new SimpleWrite().writeData("B");
			  System.out.println("VALID"+"A");
			  HttpTunnel.doHttpTunneling(out,"CLOSE");
		  }
		
		  
		 ////////////////////// 
		  System.out.println("Value....."+str);
		  
		  }catch(Exception ex){
			  ex.printStackTrace();
			  System.out.println(ex);
		  }
	  }
	  


	
	public static void main(String[] args) {
		try{
			MainServer ms = new MainServer();
			
		while(true){
			socket = serverSocket.accept();
			Thread thread = new Thread(ms);
			thread.start();
		}
	}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex);
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
