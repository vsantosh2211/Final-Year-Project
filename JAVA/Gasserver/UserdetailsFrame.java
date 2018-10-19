
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.sql.*;

public class UserdetailsFrame extends JFrame {

	private JLabel txt_ownerName,txt_clientName,txt_mailid,txt_tollgateArea,txt_load;
	private JLabel jl,j2,j3,j4,j5;
	private JButton btn_submit;
    private	JPanel jp=new JPanel();
	JPanel jp1=new JPanel();
	JPanel jp2=new JPanel();
	String ownerName,clientName,mailId,tollgateArea,extra;
	String Name,VehicleNumber,lifiid,mobileNO,Emailid;
	String fileName = "";
	
	Toolkit tk;
	Dimension d;
	
	
	//JRadioButton jrbNumbers = new JRadioButton("Numbers");
	JTabbedPane jtp=new JTabbedPane();
	String request = "";
	static String serverIp = "";
	
	public UserdetailsFrame(String userId,String load,int balance)throws Exception{
		
		JLabel bgr; 
		bgr = new JLabel();                   
		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		
		jp.setLayout(null);
		jp1.setLayout(null);
		txt_ownerName = new JLabel();
		txt_clientName = new JLabel();
		txt_mailid = new JLabel();
		txt_tollgateArea = new JLabel();
		txt_load = new JLabel();
		
		try{
			
			Connection con = getConnection();
			if(con==null){
				System.exit(0);
			}else{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select name,vehicleno,contactno, lifiid,email from userdetails where lifiid='A'");
				if(rs.next()){
					Name = rs.getString(1);
					VehicleNumber = rs.getString(2);
					mobileNO = rs.getString(3);
					lifiid = rs.getString(4);
					Emailid = rs.getString(5);
					
					
					
					
				}else{
					JOptionPane.showMessageDialog(UserdetailsFrame.this,"No Data Found");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex);
		}
			
		jl=new JLabel("OwnerName:");
		j2=new JLabel("Vehicle Number:  ");
		
		j3=new JLabel("Mobile Number:");
		j4=new JLabel("Lifi Id:  ");
		j5=new JLabel("Total Deducted :");
		
		//j3 =new JLabel("Enter Query:  ");
		btn_submit = new JButton("Close");
		//jb2=new JButton("Clear");
	    jp.add(txt_ownerName);
	    jp.add(txt_clientName);
	    jp.add(txt_mailid);
	    jp.add(txt_tollgateArea);
	    jp.add(txt_load);
		jp.add(btn_submit);
		jp.add(jl);
		jp.add(j2);
		jp.add(j3);
		jp.add(j4);
		jp.add(j5);
		jp.add(btn_submit);
				
		jtp.addTab("User Details",jp);
	    add(jtp);
	    jl.setBounds(50,50,250,25);
	    txt_ownerName.setBounds(200,50,250,25);
		j2.setBounds(50,80,150,25);
		txt_clientName.setBounds(200,80,250,25);
       
		j3.setBounds(50,110,250,25);
		txt_mailid.setBounds(200,110,300,25);
		j4.setBounds(50,140,150,25);
		txt_tollgateArea.setBounds(200,140,250,25);
		j5.setBounds(50,170,250,25);
		txt_load.setBounds(200,170,250,25);
		

		btn_submit.setBounds(50,250,100,25);
            
              
		btn_submit.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent ae){
        		System.exit(0);
        	}
        });
 				System.out.println(Name);
					System.out.println(VehicleNumber);
					System.out.println(mobileNO);
					System.out.println(lifiid);
					System.out.println(Emailid);
					System.out.println(load);
		txt_ownerName.setText(Name);
		txt_clientName.setText(VehicleNumber);
		txt_mailid.setText(mobileNO);
		txt_tollgateArea.setText(lifiid);
		txt_load.setText(String.valueOf(balance));
		
		setTitle("MainWindow");
		
		
		int x = (d.height/2) - 200;
		int y = (d.width/2) - 450;
 		this.setLocation(x,y); 
 		this.setSize(new Dimension(400,400)); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		try{
		//SendMailTLS.sendMail("C:\\Users\\ALLEN\\Desktop\\WeightEMB\\image\\image0.jpg",mailId,tollgateArea,load);
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex);
		}
	}
   
  public static void main(String a[]){
	  try{
	  new UserdetailsFrame("A","00000",1);
	  }catch(Exception ex){
		  ex.printStackTrace();
		  System.out.println(ex);
	  }
  }
	

 private Connection getConnection(){
	 Connection con = null;
	 try{
		 Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/lifitollgate","root","admin");
			
		 
	 }catch(Exception ex){
		 ex.printStackTrace();
		 System.out.println(ex);
	 }
	 return con;
 }
	
}
