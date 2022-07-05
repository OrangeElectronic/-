package com.qs.demo;

import com.qs.wiget.App;
import com.qs404demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SendCmdActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sendcmd_layout);
		
		findViewById(R.id.open_5v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 35 56 4F 4E 打开5V使能电压
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x56,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "打开5V使能电压", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		findViewById(R.id.close_5v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 35 56 4F 46 关闭5V使能电压
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x56,0x4F,0x46});
				Toast.makeText(getApplicationContext(), "关闭5V使能电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_9v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 39 56 4F 4E 打开9V使能电压
				App.send(new byte[]{0x1B,0x23,0x23,0x39,0x56,0x4F,0x4E});
				Toast.makeText(getApplicationContext(), "打开9V使能电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_BP5).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 35 39 4F 46 关闭PB5电压
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x39,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "关闭PB5电压", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		findViewById(R.id.open_BP5).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 35 39 4F 4E 打开PB5电压
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x39,0x4F,0x4E});
				Toast.makeText(getApplicationContext(), "打开BP5电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_9v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 39 56 4F 46 关闭9V使能电压
				App.send(new byte[]{0x1B,0x23,0x23,0x39,0x56,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "关闭9V使能电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_gpio1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 31 4F 4F 4E 打开gpio1电压
				App.send(new byte[]{0x1B,0x23,0x23,0x31,0x4F,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "打开gpio1电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_gpio1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 31 4F 4F 46 关闭gpio1电压
				App.send(new byte[]{0x1B,0x23,0x23,0x31,0x4F,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "关闭gpio1电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_gpio2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 32 4F 4F 4E 打开gpio2电压
				App.send(new byte[]{0x1B,0x23,0x23,0x32,0x4F,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "打开gpio2电压", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_gpio2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 32 4F 4F 46 关闭gpio2电压
				App.send(new byte[]{0x1B,0x23,0x23,0x32,0x4F,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "关闭gpio2电压", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 控制指令 
	 *  
 1B 23 23 35 56 4F 4E 打开5V使能电压

 1B 23 23 35 56 4F 46 关闭5V使能电压



 1B 23 23 39 56 4F 4E 打开9V使能电压

 1B 23 23 39 56 4F 46 关闭9V使能电压



 1B 23 23 31 4F 4F 4E 打开gpio1电压

 1B 23 23 31 4F 4F 46 关闭gpio1电压


 1B 23 23 32 4F 4F 4E 打开gpio2电压

 1B 23 23 32 4F 4F 46 关闭gpio2电压
 
	 *
	 **/
	
}
