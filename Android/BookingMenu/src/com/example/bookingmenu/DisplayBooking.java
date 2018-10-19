package com.example.bookingmenu;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayBooking extends ListActivity {
	static String text = "";
	String message = "";
	String Encode;
	 private HttpResponse response;
     private HttpClient httpclient;
     private String responseText = "";
	public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		   LinearLayout linearLayout = new LinearLayout(this);
		   
		
		   String reply = getServerConnection("GETBOOKING$");
		 //  String reply="1";
		   
		   if(reply!=null && reply.intern()!="NODATA"){
			   message = reply;
			  // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
		   }else{
			  // Toast.makeText(getApplicationContext(), "No Data found", Toast.LENGTH_LONG).show();
		   }
		  
	 	 //  Toast.makeText(getApplicationContext(), "MEssage...."+message, Toast.LENGTH_LONG).show();
		    StringTokenizer st = new StringTokenizer(message,"#");
		    //st.nextToken();
		    ArrayList<String> al = new ArrayList<String>();
		    al.add("Booking :");
		    
		    while(st.hasMoreTokens()){
		    	String parent=st.nextToken();
		    	StringTokenizer stt=new StringTokenizer(parent,"$");
		    	String category=stt.nextToken().toString();
		    	String date=stt.nextToken().toString();		    	
		    	String cusId=stt.nextToken().toString();
		    	StringBuilder sb=new StringBuilder();
		    	sb.append("Category :"+category+"\n");
		    	sb.append("Date :"+date+"\n");
		    	sb.append("CustomerId :"+cusId+"\n");
			  al.add(sb.toString());
			   
		   }
		    al.add("Back");
		   Log.v("Search output",al.toString());
		   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, al));

		   ListView lv = getListView();
		   lv.setTextFilterEnabled(true);

		   lv.setOnItemClickListener(new OnItemClickListener() {
		     public void onItemClick(AdapterView<?> parent, View view,
		         int position, long id) {
		      	String selectedText = ((TextView) view).getText().toString();
		      	//ServerIPAddress.setSelectedvalue(selectedText);
		      	 if(selectedText.intern() == "Back"){
		      		Intent intent = new Intent(DisplayBooking.this,MainActivity.class);
		      		 startActivity(intent);
		      	 }else{
		      		text = selectedText;
		      		
		      	 }
		       }
		   });
		 }
		
	private String getServerConnection(String data){
    	
		 try {
	    	httpclient = new DefaultHttpClient();
	    	HttpGet httpget = new HttpGet("http://"+ServerIPAddress.getserverIPaddress()+":222/"+data);
	        response = httpclient.execute(httpget);
	        HttpEntity entity = response.getEntity();
	        responseText = EntityUtils.toString(entity);
	         
	      }catch (Exception e){
	        Toast.makeText(this, "error"+e.toString(), Toast.LENGTH_LONG).show();
	        
	    }
		 return responseText.trim();
	
}


	}

