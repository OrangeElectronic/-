package com.qs.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.qs.adapter.TestInfoAdapter;
import com.qs.bean.TestInfo;
import com.qs.wiget.App;
import com.qs404demo.R;

/**
 * 主界面
 * @author wsl
 *
 */
public class MainActivity extends Activity {
	
	private GridView mGridView = null;
	private List<TestInfo> testInfos = null;
	private TestInfoAdapter  mAdapter = null;
	private TextView textView2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		initViews();
		initData();
		setListener();
        //列表分3行
		mGridView.setNumColumns(3);
		
	}

	private void initData() {
		// TODO Auto-generated method stub

		testInfos = new ArrayList<TestInfo>();
		TestInfo  info = null;

		info = new TestInfo("Scan",R.drawable.barcode,-2);
		testInfos.add(info);

		info = new TestInfo("Update",R.drawable.ic_update,-3);
		testInfos.add(info);
		
		info = new TestInfo("Send CMD",R.drawable.setting,-4);
		testInfos.add(info);
		
		info = new TestInfo("SerialPort2",R.drawable.psam,-5);
		testInfos.add(info);
		
		info = new TestInfo("SerialPort4",R.drawable.psam,-6);
		testInfos.add(info);
		
		mAdapter = new TestInfoAdapter(MainActivity.this, testInfos);
		mGridView.setAdapter(mAdapter);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mGridView = (GridView)this.findViewById(R.id.mGridView);
		
		textView2=(TextView) findViewById(R.id.textView2);
		
		textView2.setText("QS404demo 2019-05-14 V1.0");
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TestInfo info = (TestInfo) parent.getAdapter().getItem(position);
				switch(info.getCmd()){
				case -2:
					startActivity(new Intent(MainActivity.this, ScanActivity.class));
					break;
				case -3:
					//更新打印机程序
					startActivity(new Intent(MainActivity.this, UpdateActivity.class));
					break;
				case -4:
					//
					startActivity(new Intent(MainActivity.this, SendCmdActivity.class));
					break;
					
				case -5:
					//
					startActivity(new Intent(MainActivity.this, SerialPort2Activity.class));
					break;
					
				case -6:
					//
					startActivity(new Intent(MainActivity.this, SerialPort4Activity.class));
					break;
					
				default:
					startActivity(new Intent(MainActivity.this, ScanActivity.class));
					break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		App.closeCommonApi();
		System.exit(0);
		super.onDestroy();
	}

}

