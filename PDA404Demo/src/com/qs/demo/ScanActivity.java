package com.qs.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.qs.wiget.App;
import com.qs404demo.R;

public class ScanActivity extends Activity {


	private EditText tv;
	
	// SCAN按键监听
	private ScanBroadcastReceiver scanBroadcastReceiver;
	
	Handler h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		tv = (EditText) findViewById(R.id.tv2);

		findViewById(R.id.btn_scan).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					App.openScan();
			}
		});

		findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv.setText("");
			}
		});
		
		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qs.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);
		
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(scanBroadcastReceiver);
	}
	


	class ScanBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text1 = intent.getExtras().getString("code");
			String str1 = tv.getText().toString();
			tv.setText(str1.trim()+"\n"+text1.trim());
			CharSequence text = tv.getText();
			if (text instanceof Spannable) {
				Spannable spanText = (Spannable) text;
				Selection.setSelection(spanText, text.length());
			}
		}
	}
	

}
