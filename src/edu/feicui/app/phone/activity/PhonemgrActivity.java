package edu.feicui.app.phone.activity;
import edu.feicui.app.phone.adapter.PhonemgrAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.biz.PhoneManager;
import edu.feicui.app.phone.entity.PhoneInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class PhonemgrActivity extends BaseActivity {	
	private View layout_battery;//电池电量整体布局
	private TextView tv_battery;//电池电量百分比
	private ProgressBar pb_battery;//电池电量进度
	private BatteryBroadcastReceiver broadcastReceiver;//电池电量广播接收器
	private ProgressBar pb_loading;
	private ListView exListView;//手机检测信息
	private PhonemgrAdapter phonemgrAdapter;
	int currentBattery;//当前电量
	int temperatureBattery;//电池温度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonemgr);
		String title=getResources().getString(R.string.phonemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		exListView=(ListView) findViewById(R.id.listviewLoad);
		phonemgrAdapter=new PhonemgrAdapter(this);
		exListView.setAdapter(phonemgrAdapter);
		//获取数据在工作线程中
		//初始化手机检测信息
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initAdapterData();
			}
		}).start();
		initMainButton();
	}
	private void initAdapterData() {
		// TODO Auto-generated method stub
		pb_loading.setVisibility(View.VISIBLE);
		exListView.setVisibility(View.INVISIBLE);
		//此方法获得的对象始终只有一个（单例模式）
		PhoneManager manager=PhoneManager.getPhoneManager(this);
		String title;
		String text;
		Drawable icon;
		//
		title="设备名称:"+manager.getPhoneName1();
		text="系统版本:"+manager.getPhoneSystemVersion();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_version);
		//将数据封装到对象中
		final PhoneInfo info1=new PhoneInfo(title, text, icon);
		//
		title="全部运行内存"+CommonUtil.getFileSize(MemoryManager.getPhoneTotalRamMemory());
		text="剩余运行内存"+CommonUtil.getFileSize(MemoryManager.getPhoneFreeRamMemory(this));
		icon=getResources().getDrawable(R.drawable.setting_info_icon_space);
		final PhoneInfo info2=new PhoneInfo(title, text, icon);
		//
		title="cpu名称"+manager.getPhoneCpuName();
		text="cpu数量"+manager.getPhoneCpuNumber();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_cpu);
		final PhoneInfo info3=new PhoneInfo(title, text, icon);
		//
		title="手机分辨率"+manager.getResolution();
		text="相机分辨率"+manager.getMaxPhotoSize();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_camera);
		final PhoneInfo info4=new PhoneInfo(title, text, icon);
		//
		title="基带版本"+manager.getPhoneSystemBasebandVersion();
		text="是否ROOT"+(manager.isRoot()?"是":"否");
		icon=getResources().getDrawable(R.drawable.setting_info_icon_root);
		final PhoneInfo info5=new PhoneInfo(title, text, icon);
		//
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//向phonemgrAdapter里的集合中放置一个元素
				phonemgrAdapter.addDataToAdapter(info1);
				phonemgrAdapter.addDataToAdapter(info2);
				phonemgrAdapter.addDataToAdapter(info3);
				phonemgrAdapter.addDataToAdapter(info4);
				phonemgrAdapter.addDataToAdapter(info5);
				//通知更新
				phonemgrAdapter.notifyDataSetChanged();
				pb_loading.setVisibility(View.INVISIBLE);
				exListView.setVisibility(View.VISIBLE);
			}
		});
	}
	OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_left:
				startActivity(HomeActivity.class);
				finish();
				break;
			case R.id.ll_layout_battery:
				showBatteryMessage();
				break;
			default:
				break;
			}
		}
//		private void showBatteryMessage() {
//			AlertDialog.Builder builder=new AlertDialog.Builder(PhonemgrActivity.this);
//			builder.setTitle("电池电量信息");
//			//内部类里无法调用成员变量数据，需要final修饰
//			builder.setItems(new String[]{"当前电量:"+currentBattery,"电池温度:"+temperatureBattery}, null);
//			builder.show();
//		}
	};
	private void showBatteryMessage() {
		AlertDialog.Builder builder=new AlertDialog.Builder(PhonemgrActivity.this);
		builder.setTitle("电池电量信息");
		//内部类里无法调用成员变量数据，需要final修饰
		builder.setItems(new String[]{"当前电量:"+currentBattery,"电池温度:"+temperatureBattery}, null);
		builder.show();
	}
	private void initMainButton() {
		//电池线性布局
		layout_battery=findViewById(R.id.ll_layout_battery);
		layout_battery.setOnClickListener(clickListener);
		//表示当前电量百分比
		tv_battery=(TextView)findViewById(R.id.tv_battery);
		//表示进度条的电量
		pb_battery=(ProgressBar)findViewById(R.id.pb_battery);
		pb_loading=(ProgressBar) findViewById(R.id.progressBar);
		//3.注册电池电量广播接收器
		broadcastReceiver=new BatteryBroadcastReceiver();
		//意图过滤器
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(broadcastReceiver, filter);
	}
	public class BatteryBroadcastReceiver extends BroadcastReceiver{//1.继承BroadcastReceiver
		@Override
		public void onReceive(Context context, Intent intent) {//2.重写onReceive方法
			//如果电池状态发生变化，就执行以下方法
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				Bundle bundle=intent.getExtras();
				//获取电池总电量  BatteryManager底层管理电池的类
				int maxBattery=(Integer) bundle.get(BatteryManager.EXTRA_SCALE);
				//当前电池电量
				currentBattery=(Integer) bundle.get(BatteryManager.EXTRA_LEVEL);
				//当前电池温度
				temperatureBattery=(Integer) bundle.get(BatteryManager.EXTRA_TEMPERATURE);
				pb_battery.setMax(maxBattery);
				pb_battery.setProgress(currentBattery);
				int current100=currentBattery*100/maxBattery;//例如：500/1000*100=50
				tv_battery.setText(current100+"%");//即50%
			}
		}		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//销毁以便释放资源
		unregisterReceiver(broadcastReceiver);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}	
}
