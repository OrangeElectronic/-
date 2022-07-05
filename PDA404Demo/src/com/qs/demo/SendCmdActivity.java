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
				//1B 23 23 35 56 4F 4E ��5Vʹ�ܵ�ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x56,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "��5Vʹ�ܵ�ѹ", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		findViewById(R.id.close_5v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 35 56 4F 46 �ر�5Vʹ�ܵ�ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x56,0x4F,0x46});
				Toast.makeText(getApplicationContext(), "�ر�5Vʹ�ܵ�ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_9v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 39 56 4F 4E ��9Vʹ�ܵ�ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x39,0x56,0x4F,0x4E});
				Toast.makeText(getApplicationContext(), "��9Vʹ�ܵ�ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_BP5).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 35 39 4F 46 �ر�PB5��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x39,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "�ر�PB5��ѹ", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		findViewById(R.id.open_BP5).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 35 39 4F 4E ��PB5��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x35,0x39,0x4F,0x4E});
				Toast.makeText(getApplicationContext(), "��BP5��ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_9v).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//1B 23 23 39 56 4F 46 �ر�9Vʹ�ܵ�ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x39,0x56,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "�ر�9Vʹ�ܵ�ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_gpio1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 31 4F 4F 4E ��gpio1��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x31,0x4F,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "��gpio1��ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_gpio1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 31 4F 4F 46 �ر�gpio1��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x31,0x4F,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "�ر�gpio1��ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.open_gpio2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 32 4F 4F 4E ��gpio2��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x32,0x4F,0x4F,0x4E});
				
				Toast.makeText(getApplicationContext(), "��gpio2��ѹ", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.close_gpio2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 1B 23 23 32 4F 4F 46 �ر�gpio2��ѹ
				App.send(new byte[]{0x1B,0x23,0x23,0x32,0x4F,0x4F,0x46});
				
				Toast.makeText(getApplicationContext(), "�ر�gpio2��ѹ", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * ����ָ�� 
	 *  
 1B 23 23 35 56 4F 4E ��5Vʹ�ܵ�ѹ

 1B 23 23 35 56 4F 46 �ر�5Vʹ�ܵ�ѹ



 1B 23 23 39 56 4F 4E ��9Vʹ�ܵ�ѹ

 1B 23 23 39 56 4F 46 �ر�9Vʹ�ܵ�ѹ



 1B 23 23 31 4F 4F 4E ��gpio1��ѹ

 1B 23 23 31 4F 4F 46 �ر�gpio1��ѹ


 1B 23 23 32 4F 4F 4E ��gpio2��ѹ

 1B 23 23 32 4F 4F 46 �ر�gpio2��ѹ
 
	 *
	 **/
	
}
