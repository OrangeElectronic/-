package com.qs.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.qs.wiget.App;
import com.qs404demo.R;

public class SerialPort2Activity extends Activity {

	// SCAN按键监听
	private ScanBroadcastReceiver scanBroadcastReceiver;
	
	private EditText ed_serial2_send,ed_serial2_read;
	
	public static StringBuffer sb1 = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial_port2);
		
		ed_serial2_send = (EditText) findViewById(R.id.ed_serial2_send);
		ed_serial2_read = (EditText) findViewById(R.id.ed_serial2_read);
		
		final String str1="0A 00 00 03 00 09 F5";
		
		final String str4=getString(R.string.test_str);
		
		findViewById(R.id.set1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_serial2_send.setText(str1);
			}
		});
		
	findViewById(R.id.set2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_serial2_send.setText(str4);
			}
		});

		findViewById(R.id.btn_serial2_send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String str=ed_serial2_send.getText().toString().trim();
				
				if(str.length()<=0){
					
					Toast.makeText(getApplicationContext(), "请输入数据", 0).show();
				
				}else{
					
					str=str.replace(" ", ""); 
					
					Log.e("", "输入数据："+str);
					
					if(str.length()%2!=0){
						
						Toast.makeText(getApplicationContext(), "数据长度必须为双数，不足双位则以0为左位补足", 0).show();
						
						return;
					}
					
					str=str_tobyte(str);
					
					Log.e("", "数据："+str);
					
					byte[] byte_send=hexStringToBytes(str);
					
					App.send(new byte[]{0x1B,0x23,0x23,0x55,0x54,0x54,0x32});
					
					App.send(new byte[]{(byte) byte_send.length});
					
					App.send(byte_send);
				}
			}
		});
		
		findViewById(R.id.btn_serial2_clear).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ed_serial2_read.setText("");
				
			}
			
		});
		
		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qs.uart2code");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);
	}
	

	class ScanBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text1 = intent.getExtras().getString("code");
			ed_serial2_read.setText(text1.trim());
//			String str1 = ed_serial2_read.getText().toString();
//			ed_serial2_read.setText(str1.trim()+"\n"+text1.trim());
//			CharSequence text = ed_serial2_read.getText();
//			if (text instanceof Spannable) {
//				Spannable spanText = (Spannable) text;
//				Selection.setSelection(spanText, text.length());
//			}

		}
	}
	
	public static String str_tobyte(String str){
		
	        StringBuffer s1 = new StringBuffer(str);
	        StringBuffer s2 = new StringBuffer();
	        int index;
	        for (index = 2; index < s1.length(); index += 3) {
	            s1.insert(index, ',');
	        }
	        String[] array = s1.toString().split(",");
	        for (String string : array) {
	        	s2.append(string+" ");
	        }
	        
	        return s2.toString();
	}

	/**
	 * hex String to byte array
	 */
	public static byte[] hexStringToBytes(String hexString) {
		hexString = hexString.toLowerCase();
		String[] hexStrings = hexString.split(" ");
		byte[] bytes = new byte[hexStrings.length];
		for (int i = 0; i < hexStrings.length; i++) {
			char[] hexChars = hexStrings[i].toCharArray();
			bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
		}
		return bytes;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(scanBroadcastReceiver);
		super.onDestroy();
	}
	

}

