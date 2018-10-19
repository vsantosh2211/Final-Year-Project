package com.example.bookingmenu;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	EditText ip_Addr_txt;
	Button View_btn;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		  if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy =
	                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
		   }
		ip_Addr_txt=(EditText)findViewById(R.id.editText_ip);
		View_btn=(Button)findViewById(R.id.button_view);
		View_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ServerIPAddress.setserverIPaddress(ip_Addr_txt.getText().toString());
			Intent i=new Intent(MainActivity.this,DisplayBooking.class);
			startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
