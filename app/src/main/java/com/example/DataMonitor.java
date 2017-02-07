package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


public class DataMonitor extends FragmentActivity implements OnClickListener {
	String[] AfterSplit;
	private int num;
	List<String> macID = new ArrayList<>();
	File myfile1;
	File myfile2;
	File myfile3;
	File myfile4;


	String strDate;
	String ipaddress = "140.138.178.72";
	String server_url = "http://"+ipaddress+"/android/upload.php";
	File[] files = new File[2];

	boolean slideAction = false;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothService mBluetoothService = null;
	private BluetoothService mBluetoothService2 = null;
	private BluetoothService mBluetoothService3 = null;
	private BluetoothService mBluetoothService4 = null;
	private BluetoothService mBluetoothService5 = null;
	private String mConnectedDeviceName = null;

	private TextView mTitle;
	private TextView mTitle2;
	private TextView mTitle3;
	private TextView mTitle4;
	private boolean recordStartorStop=false;

	private DataFragment dataFragment;

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	protected static final String TAG = null;
	private short sOffsetAccX,sOffsetAccY,sOffsetAccZ;

	private Button btn_start, btn_stop, btn_add;
	private Button btn_sit, btn_walk, btn_trot, btn_OnStairs, btn_DownStairs,btn_stand,btn_shake,btn_talk;

	private final Handler mHandler = new Handler() {
		// 匿名内部类写法，实现接口Handler的一些方法
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothService.STATE_CONNECTED:
							mTitle.setText(R.string.title_connected_to);
//							mTitle.append(mConnectedDeviceName);
							break;
						case BluetoothService.STATE_CONNECTING:
							mTitle.setText(R.string.title_connecting);
							break;
						case BluetoothService.STATE_LISTEN:
						case BluetoothService.STATE_NONE:
							mTitle.setText(R.string.title_not_connected);
							break;
					}
					break;
				case MESSAGE_READ:
					try {
//					float [] fData=msg.getData().getFloatArray("Data");
//					switch (RunMode){
//						case 0:
//							switch (iCurrentGroup){
//								case 0:
//									((TextView)findViewById(R.id.tvNum1)).setText(msg.getData().getString("Date"));
//									((TextView)findViewById(R.id.tvNum2)).setText(msg.getData().getString("Time"));
//									break;
//								case 1:
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2fg", fData[0]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2fg", fData[1]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.2fg", fData[2]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 10.2f℃", fData[17]));
//									break;
//								case 2:
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2f°/s", fData[3]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2f°/s", fData[4]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.2f°/s", fData[5]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 10.2f℃", fData[17]));
//									break;
//								case 3:
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2f°", fData[6]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2f°", fData[7]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.2f°", fData[8]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 10.2f℃", fData[17]));
//									break;
//								case 4://磁场
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.0f", fData[9]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.0f", fData[10]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.0f", fData[11]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 10.2f℃", fData[17]));
//									break;
//								case 5://端口
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2f", fData[12]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2f", fData[13]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.2f", fData[14]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 10.2f", fData[15]));
//									break;
//								case 6://气压
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2fPa", fData[16]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2fm", fData[17]));
//									break;
//								case 7://经纬度
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 14.6f°", fData[18]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 14.6f°", fData[19]));
//									break;
//								case 8://地速
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 10.2m", fData[20]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 10.2°", fData[21]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 10.2m/s", fData[22]));
//									break;
//								case 9://四元数
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 7.3f", fData[23]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 7.3f", fData[24]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 7.3f", fData[25]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 7.3f", fData[26]));
//									break;
//								case 10:
//									((TextView)findViewById(R.id.tvNum1)).setText(String.format("% 5.0f", fData[27]));
//									((TextView)findViewById(R.id.tvNum2)).setText(String.format("% 7.1f", fData[28]));
//									((TextView)findViewById(R.id.tvNum3)).setText(String.format("% 7.1f", fData[29]));
//									((TextView)findViewById(R.id.tvNum4)).setText(String.format("% 7.1f", fData[30]));
//									break;
//							}
//							break;
//					}
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case MESSAGE_DEVICE_NAME:
					mConnectedDeviceName = msg.getData().getString("device_name");
					Toast.makeText(getApplicationContext(),"Connected to " + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	private final Handler mHandler2 = new Handler() {
		// 匿名内部类写法，实现接口Handler的一些方法
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothService.STATE_CONNECTED:
							mTitle2.setText(R.string.title_connected_to);
//							mTitle.append(mConnectedDeviceName);
							break;
						case BluetoothService.STATE_CONNECTING:
							mTitle2.setText(R.string.title_connecting);
							break;
						case BluetoothService.STATE_LISTEN:
						case BluetoothService.STATE_NONE:
							mTitle2.setText(R.string.title_not_connected);
							break;
					}
					break;
				case MESSAGE_READ:
					try {
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case MESSAGE_DEVICE_NAME:
					mConnectedDeviceName = msg.getData().getString("device_name");
					Toast.makeText(getApplicationContext(),"Connected to " + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	private final Handler mHandler3 = new Handler() {
		// 匿名内部类写法，实现接口Handler的一些方法
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothService.STATE_CONNECTED:
							mTitle3.setText(R.string.title_connected_to);
//							mTitle.append(mConnectedDeviceName);
							break;
						case BluetoothService.STATE_CONNECTING:
							mTitle3.setText(R.string.title_connecting);
							break;
						case BluetoothService.STATE_LISTEN:
						case BluetoothService.STATE_NONE:
							mTitle3.setText(R.string.title_not_connected);
							break;
					}
					break;
				case MESSAGE_READ:
					try {
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case MESSAGE_DEVICE_NAME:
					mConnectedDeviceName = msg.getData().getString("device_name");
					Toast.makeText(getApplicationContext(),"Connected to " + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	private final Handler mHandler4 = new Handler() {
		// 匿名内部类写法，实现接口Handler的一些方法
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothService.STATE_CONNECTED:
							mTitle4.setText(R.string.title_connected_to);
//							mTitle.append(mConnectedDeviceName);
							break;
						case BluetoothService.STATE_CONNECTING:
							mTitle4.setText(R.string.title_connecting);
							break;
						case BluetoothService.STATE_LISTEN:
						case BluetoothService.STATE_NONE:
							mTitle4.setText(R.string.title_not_connected);
							break;
					}
					break;
				case MESSAGE_READ:
					try {
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case MESSAGE_DEVICE_NAME:
					mConnectedDeviceName = msg.getData().getString("device_name");
					Toast.makeText(getApplicationContext(),"Connected to " + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	private static final int REQUEST_CONNECT_DEVICE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		verifyStoragePermissions(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		mTitle = (TextView) findViewById(R.id.title_right_text);
		mTitle  = (TextView) findViewById(R.id.textView2);
		mTitle2 = (TextView) findViewById(R.id.textView3);
		mTitle3 = (TextView) findViewById(R.id.textView4);
		mTitle4 = (TextView) findViewById(R.id.textView5);
		SelectFragment(0);

		btn_start = (Button) findViewById(R.id.start);
		btn_stop = (Button) findViewById(R.id.stop);
		btn_add = (Button) findViewById(R.id.add);

		btn_sit = (Button) findViewById(R.id.btn_sit);
		btn_walk = (Button) findViewById(R.id.btn_walk);
		btn_trot = (Button) findViewById(R.id.btn_trot);
		btn_OnStairs = (Button) findViewById(R.id.btn_OnStairs);
		btn_DownStairs = (Button) findViewById(R.id.btn_DownStairs);
		btn_stand = (Button) findViewById(R.id.btn_stand);
		btn_shake = (Button) findViewById(R.id.btn_shake);
		btn_talk = (Button) findViewById(R.id.btn_talk);

		btn_add.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "動作切換",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.add();
				mBluetoothService2.add();
				//mBluetoothService3.add();
				//mBluetoothService4.add();

			}
		});

		btn_sit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "坐",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.sit();
				mBluetoothService2.sit();
				//mBluetoothService3.sit();
				//mBluetoothService4.sit();

			}
		});

		btn_walk.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "走路",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.walk();
				mBluetoothService2.walk();
				//mBluetoothService3.walk();
				//mBluetoothService4.walk();

			}
		});

		btn_trot.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "小跑步",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.trot();
				mBluetoothService2.trot();
				//mBluetoothService3.trot();
				//mBluetoothService4.trot();

			}
		});

		btn_OnStairs.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "上樓梯",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.OnTheStairs();
				mBluetoothService2.OnTheStairs();
				//mBluetoothService3.OnTheStairs();
				//mBluetoothService4.OnTheStairs();

			}
		});

		btn_DownStairs.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "下樓梯",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.DownTheStairs();
				mBluetoothService2.DownTheStairs();
				//mBluetoothService3.DownTheStairs();
				//mBluetoothService4.DownTheStairs();

			}
		});

		btn_stand.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "定點站立",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.stand();
				mBluetoothService2.stand();
				//mBluetoothService3.stand();
				//mBluetoothService4.stand();

			}
		});

		btn_shake.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "定點搖晃",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.shake();
				mBluetoothService2.shake();
				//mBluetoothService3.shake();
				//mBluetoothService4.shake();

			}
		});

		btn_talk.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "與人交談",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.talk();
				mBluetoothService2.talk();
				//mBluetoothService3.talk();
				//mBluetoothService4.talk();

			}
		});

		btn_start.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Start",
						Toast.LENGTH_SHORT).show();
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
				strDate = sdf.format(c.getTime());
				mBluetoothService.start(v, strDate);
				mBluetoothService2.start(v, strDate);
				//mBluetoothService3.start(v, strDate);
				//mBluetoothService4.start(v, strDate);

			}
		});
		btn_stop.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Finish",
						Toast.LENGTH_SHORT).show();
				mBluetoothService.stop(v);
				mBluetoothService2.stop(v);
				//mBluetoothService3.stop(v);
				//mBluetoothService4.stop(v);

				//String path  = android.os.Environment.getExternalStorageDirectory() + "/" + strDate + "-" + "3E" + ".csv";
				//String path2 = android.os.Environment.getExternalStorageDirectory() + "/" + strDate + "-" + "31" + ".csv";
				String path3 = android.os.Environment.getExternalStorageDirectory() + "/" + strDate + "-" + "6D" + ".csv";
				String path4 = android.os.Environment.getExternalStorageDirectory() + "/" + strDate + "-" + "DB" + ".csv";

				try {
					//myfile1 = new File(path);
					//myfile2 = new File(path2);
					myfile3 = new File(path3);
					myfile4 = new File(path4);
					//files[0] = myfile1;
					//files[1] = myfile2;
					files[0] = myfile3;
					files[1] = myfile4;
					if (myfile3.exists() && myfile4.exists()) {
						Message msg = mHandler.obtainMessage(DataMonitor.MESSAGE_TOAST);
						Bundle bundle = new Bundle();
						bundle.putString("toast", "file exists");
						msg.setData(bundle);
						mHandler.sendMessage(msg);
						postFile2Server(server_url, files);
					}
				}catch (Exception e){
				}
			}
		});

		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
				//finish();
				return;
			}

			if (!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
			if (mBluetoothService == null)
				mBluetoothService = new BluetoothService(this, mHandler); // 用来管理蓝牙的连接
			if (mBluetoothService2 == null)
					mBluetoothService2 = new BluetoothService(this, mHandler2); // 用来管理蓝牙的连接
			if (mBluetoothService3 == null)
				mBluetoothService3 = new BluetoothService(this, mHandler3); // 用来管理蓝牙的连接
			if (mBluetoothService4 == null)
				mBluetoothService4 = new BluetoothService(this, mHandler4); // 用来管理蓝牙的连接
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		}
		catch (Exception err){}
	}
	public void onClickedBTSet(View v){
		try {
			if (!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
			if (mBluetoothService == null)
				mBluetoothService = new BluetoothService(this, mHandler); // 用来管理蓝牙的连接
//			if (mBluetoothService2 == null) {
//				for (int i = 0; i < 3; i++)
//					mBluetoothService2[i] = new BluetoothService(this, mHandler); // 用来管理蓝牙的连接

			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		}
		catch (Exception err){}
	}
	@SuppressLint("NewApi")
	private void SelectFragment(int Index) {
		// TODO Auto-generated method stub
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

		if (dataFragment==null) {dataFragment = new DataFragment();transaction.add(R.id.id_content, dataFragment);}
		transaction.show(dataFragment);
		transaction.commit();
	}

	@Override
	public void onStart() {
		super.onStart();
		try{
			GetSelected();
		}
		catch (Exception err){}

	}

	public synchronized void onResume() {
		super.onResume();

		if (mBluetoothService != null) {
			if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
				mBluetoothService.start();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBluetoothService != null) mBluetoothService.stop();
		if (mBluetoothService2 != null) mBluetoothService2.stop();
		if (mBluetoothService3 != null) mBluetoothService3.stop();
		if (mBluetoothService4 != null) mBluetoothService4.stop();
		if (mBluetoothService5 != null) mBluetoothService5.stop();
	}
	public BluetoothDevice device;
	// 利用startActivityForResult 和 onActivityResult在activity间传递数据
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:// When DeviceListActivity returns with a device to connect			
			if (resultCode == Activity.RESULT_OK) {				
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);// Get the device MAC address
				AfterSplit = address.split(",");
				for (int i = 1; i < AfterSplit.length; i++) {
					String[] mac2id =AfterSplit[i].split(":");
					macID.add(mac2id[mac2id.length-1]);
				}
//				String[] stockArr = new String[macID.size()];
//				stockArr = macID.toArray(stockArr);
//				for(String s : stockArr)
//					System.out.println(s);
				num = AfterSplit.length;
				device = mBluetoothAdapter.getRemoteDevice(AfterSplit[0]);// Get the BLuetoothDevice object
				mBluetoothService.connect(device);
				device = mBluetoothAdapter.getRemoteDevice(AfterSplit[1]);// Get the BLuetoothDevice object
				mBluetoothService2.connect(device);// Attempt to connect to the device
//				device = mBluetoothAdapter.getRemoteDevice(AfterSplit[2]);// Get the BLuetoothDevice object
//				mBluetoothService3.connect(device);// Attempt to connect to the device
//				device = mBluetoothAdapter.getRemoteDevice(AfterSplit[3]);// Get the BLuetoothDevice object
//				mBluetoothService4.connect(device);// Attempt to connect to the device
//				device = mBluetoothAdapter.getRemoteDevice(AfterSplit[4]);// Get the BLuetoothDevice object
//				mBluetoothService5.connect(device);// Attempt to connect to the device

			}
			break;
		}
	}
	boolean[] selected = new boolean[]{false,true,true,true,false,false,false,false,false,false,false};
	String[] SelectItem = new String[]{"時間","加速度","角速度","角度","磁場","端口","氣壓","經緯度","地速","四元數","衛星数"};
	public void RefreshButtonStatus(){
		if (selected[0]) ((TextView)findViewById(R.id.button0)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button0)).setTextColor(Color.GRAY);
		if (selected[1]) ((TextView)findViewById(R.id.button1)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button1)).setTextColor(Color.GRAY);
		if (selected[2]) ((TextView)findViewById(R.id.button2)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button2)).setTextColor(Color.GRAY);
		if (selected[3]) ((TextView)findViewById(R.id.button3)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button3)).setTextColor(Color.GRAY);
		if (selected[4]) ((TextView)findViewById(R.id.button4)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button4)).setTextColor(Color.GRAY);
		if (selected[5]) ((TextView)findViewById(R.id.button5)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button5)).setTextColor(Color.GRAY);
		if (selected[6]) ((TextView)findViewById(R.id.button6)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button6)).setTextColor(Color.GRAY);
		if (selected[7]) ((TextView)findViewById(R.id.button7)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button7)).setTextColor(Color.GRAY);
	//	if (selected[8]) ((TextView)findViewById(R.id.button8)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button8)).setTextColor(Color.GRAY);
	//	if (selected[9]) ((TextView)findViewById(R.id.button9)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.button9)).setTextColor(Color.GRAY);
	//	if (selected[10]) ((TextView)findViewById(R.id.buttonA)).setTextColor(Color.BLACK); else ((TextView)findViewById(R.id.buttonA)).setTextColor(Color.GRAY);
	}
	public void GetSelected(){
		SharedPreferences mySharedPreferences= getSharedPreferences("Output", Activity.MODE_PRIVATE);
		try{
			int iOut = Integer.parseInt(mySharedPreferences.getString("Out", "15"));
			for (int i=0;i<selected.length;i++){
				selected[i]=((iOut>>i)&0x01)==0x01;
			}
			RefreshButtonStatus();
		}
		catch (Exception err){}
	}

	public void OnClickConfig(View v) {

		GetSelected();
		new AlertDialog.Builder(this)
				.setTitle("请选择输出内容：")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMultiChoiceItems(SelectItem, selected, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i, boolean b) {
						selected[i] = b;
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						byte[] buffer = new byte[5];
						buffer[0] = (byte) 0xff;
						buffer[1] = (byte) 0xaa;
						buffer[2] = (byte) 0x02;
						short sOut = 0;
						for (int i = 0; i < selected.length; i++) {
							if (selected[i]) sOut |= 0x01 << i;
						}
						buffer[3] = (byte) (sOut&0xff);
						buffer[4] = (byte) (sOut>>8);
						SharedPreferences mySharedPreferences= getSharedPreferences("Output",Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = mySharedPreferences.edit();
						editor.putString("Out",String.format("%d",sOut));
						editor.commit();
						RefreshButtonStatus();
						mBluetoothService.Send(buffer);
					}
				})
				.setNegativeButton("取消", null)
				.show();

	}

	int RunMode = 0;

	int iCurrentGroup=3;
	public void ControlClick(View v) {
		switch (v.getId()) {
			case R.id.button0:
				if (selected[0]==false) return;
				iCurrentGroup=0;
				((TextView)findViewById(R.id.tvDataName1)).setText("日期：");((TextView)findViewById(R.id.tvNum1)).setText("2015-1-1");
				((TextView)findViewById(R.id.tvDataName2)).setText("时间：");((TextView)findViewById(R.id.tvNum2)).setText("00:00:00.0");
				((TextView)findViewById(R.id.tvDataName3)).setText("");((TextView)findViewById(R.id.tvNum3)).setText("");
				((TextView)findViewById(R.id.tvDataName4)).setText("");((TextView)findViewById(R.id.tvNum4)).setText("");
				break;
			case R.id.button1:
				if (selected[1]==false) return;
				iCurrentGroup=1;
				((TextView)findViewById(R.id.tvDataName1)).setText("X轴：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("Y轴：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("Z轴：");((TextView)findViewById(R.id.tvNum3)).setText("0");
				((TextView)findViewById(R.id.tvDataName4)).setText("温度：");((TextView)findViewById(R.id.tvNum4)).setText("25℃");
				break;
			case R.id.button2:
				if (selected[2]==false) return;
				iCurrentGroup=2;
				((TextView)findViewById(R.id.tvDataName1)).setText("X轴：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("Y轴：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("Z轴：");((TextView)findViewById(R.id.tvNum3)).setText("0");
				((TextView)findViewById(R.id.tvDataName4)).setText("温度：");((TextView)findViewById(R.id.tvNum4)).setText("25℃");
				break;
			case R.id.button3:
				if (selected[3]==false) return;
				iCurrentGroup=3;
				((TextView)findViewById(R.id.tvDataName1)).setText("X轴：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("Y轴：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("Z轴：");((TextView)findViewById(R.id.tvNum3)).setText("0");
				((TextView)findViewById(R.id.tvDataName4)).setText("温度：");((TextView)findViewById(R.id.tvNum4)).setText("25℃");
				break;
			case R.id.button4:
				if (selected[4]==false) return;
				iCurrentGroup=4;
				((TextView)findViewById(R.id.tvDataName1)).setText("X轴：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("Y轴：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("Z轴：");((TextView)findViewById(R.id.tvNum3)).setText("0");
				((TextView)findViewById(R.id.tvDataName4)).setText("温度：");((TextView)findViewById(R.id.tvNum4)).setText("25℃");
				break;
			case R.id.button5:
				if (selected[5]==false) return;
				iCurrentGroup=5;
				((TextView)findViewById(R.id.tvDataName1)).setText("D0：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("D1：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("D2：");((TextView)findViewById(R.id.tvNum3)).setText("0");
				((TextView)findViewById(R.id.tvDataName4)).setText("D3：");((TextView)findViewById(R.id.tvNum4)).setText("0");
				break;
			case R.id.button6:
				if (selected[6]==false) return;
				iCurrentGroup=6;
				((TextView)findViewById(R.id.tvDataName1)).setText("气压：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("海拔：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("");((TextView)findViewById(R.id.tvNum3)).setText("");
				((TextView)findViewById(R.id.tvDataName4)).setText("");((TextView)findViewById(R.id.tvNum4)).setText("");
				break;
			case R.id.button7:
				if (selected[7]==false) return;
				iCurrentGroup=7;
				((TextView)findViewById(R.id.tvDataName1)).setText("经度：");((TextView)findViewById(R.id.tvNum1)).setText("0");
				((TextView)findViewById(R.id.tvDataName2)).setText("纬度：");((TextView)findViewById(R.id.tvNum2)).setText("0");
				((TextView)findViewById(R.id.tvDataName3)).setText("");((TextView)findViewById(R.id.tvNum3)).setText("");
				((TextView)findViewById(R.id.tvDataName4)).setText("");((TextView)findViewById(R.id.tvNum4)).setText("");
				break;
//			case R.id.button8:
//				if (selected[8]==false) return;
//				iCurrentGroup=8;
//				((TextView)findViewById(R.id.tvDataName1)).setText("地速：");((TextView)findViewById(R.id.tvNum1)).setText("0");
//				((TextView)findViewById(R.id.tvDataName2)).setText("航向：");((TextView)findViewById(R.id.tvNum2)).setText("0");
//				((TextView)findViewById(R.id.tvDataName3)).setText("");((TextView)findViewById(R.id.tvNum3)).setText("");
//				((TextView)findViewById(R.id.tvDataName4)).setText("");((TextView)findViewById(R.id.tvNum4)).setText("");
//				break;
//			case R.id.button9:
//				if (selected[9]==false) return;
//				iCurrentGroup=9;
//				((TextView)findViewById(R.id.tvDataName1)).setText("q0：");((TextView)findViewById(R.id.tvNum1)).setText("0");
//				((TextView)findViewById(R.id.tvDataName2)).setText("q1：");((TextView)findViewById(R.id.tvNum2)).setText("0");
//				((TextView)findViewById(R.id.tvDataName3)).setText("q2：");((TextView)findViewById(R.id.tvNum3)).setText("0");
//				((TextView)findViewById(R.id.tvDataName4)).setText("q3：");((TextView)findViewById(R.id.tvNum4)).setText("0");
//				break;
//			case R.id.buttonA:
//				if (selected[10]==false) return;
//				iCurrentGroup=10;
//				((TextView)findViewById(R.id.tvDataName1)).setText("卫星数：");((TextView)findViewById(R.id.tvNum1)).setText("0");
//				((TextView)findViewById(R.id.tvDataName2)).setText("PDOP：");((TextView)findViewById(R.id.tvNum2)).setText("0");
//				((TextView)findViewById(R.id.tvDataName3)).setText("HDOP：");((TextView)findViewById(R.id.tvNum3)).setText("0");
//				((TextView)findViewById(R.id.tvDataName4)).setText("VDOP：");((TextView)findViewById(R.id.tvNum4)).setText("0");
//				break;
		}
		((Button) findViewById(R.id.button0)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button1)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button2)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button3)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button4)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button5)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button6)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) findViewById(R.id.button7)).setBackgroundResource(R.drawable.ic_preference_single_normal);
	//	((Button) findViewById(R.id.button8)).setBackgroundResource(R.drawable.ic_preference_single_normal);
	//	((Button) findViewById(R.id.button9)).setBackgroundResource(R.drawable.ic_preference_single_normal);
	//	((Button) findViewById(R.id.buttonA)).setBackgroundResource(R.drawable.ic_preference_single_normal);
	//	((Button) findViewById(R.id.buttonB)).setBackgroundResource(R.drawable.ic_preference_single_normal);
		((Button) v).setBackgroundResource(R.drawable.ic_preference_single_pressed);
	}
	public void onRecordBtnClick(View v) {
		if (this.recordStartorStop == false)
		{
			this.recordStartorStop = true;
			mBluetoothService.setRecord(true);
			((Button)v).setText("停止");
			((Button)findViewById(R.id.BtnRecord)).setTextColor(Color.RED);
		}
		else{
			this.recordStartorStop = false;
			mBluetoothService.setRecord(false);
			((Button)findViewById(R.id.BtnRecord)).setText("记录");
			((Button)v).setTextColor(Color.WHITE);
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage("数据已经记录至手机根目录：/mnt/sdcard/Record.txt\n是否打开已保存的文件？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							File myFile=new File("/mnt/sdcard/Record.txt");
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.fromFile(myFile));
							startActivity(intent);
						}
					})
					.setNegativeButton("取消", null)
					.show();
		}
	}
	@Override
	public void onClick(View v) {

	}
	private boolean postFile2Server(String serverURL, File[] files){
		//此處為用於存放用於上傳的檔案
		RequestParams params = new RequestParams();//建立key-value用於ＨＴＴＰ-ＰＯＳＴ
		final AsyncHttpClient myPostFile = new AsyncHttpClient();//使用open的第三方同步httpLibrary(import com.loopj.android.http.*;)
		try{
			params.put("dataFileName[]", files);
		} catch(Exception e) {
		}

		myPostFile.post(serverURL, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
				Message msg = mHandler.obtainMessage(DataMonitor.MESSAGE_TOAST);
				Bundle bundle = new Bundle();
				bundle.putString("toast", "fail");
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			}

			@Override
			public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, String s) {
				Message msg = mHandler.obtainMessage(DataMonitor.MESSAGE_TOAST);
				Bundle bundle = new Bundle();
				bundle.putString("toast", "Good");
				msg.setData(bundle);
				mHandler.sendMessage(msg);

			}

		});

		return true;
	}

	// Storage Permissions variables
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			android.Manifest.permission.READ_EXTERNAL_STORAGE,
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE
	};


	//persmission method.
	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have read or write permission
		int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

		if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}

}
