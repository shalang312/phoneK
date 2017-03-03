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
	private View layout_battery;//��ص������岼��
	private TextView tv_battery;//��ص����ٷֱ�
	private ProgressBar pb_battery;//��ص�������
	private BatteryBroadcastReceiver broadcastReceiver;//��ص����㲥������
	private ProgressBar pb_loading;
	private ListView exListView;//�ֻ������Ϣ
	private PhonemgrAdapter phonemgrAdapter;
	int currentBattery;//��ǰ����
	int temperatureBattery;//����¶�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonemgr);
		String title=getResources().getString(R.string.phonemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		exListView=(ListView) findViewById(R.id.listviewLoad);
		phonemgrAdapter=new PhonemgrAdapter(this);
		exListView.setAdapter(phonemgrAdapter);
		//��ȡ�����ڹ����߳���
		//��ʼ���ֻ������Ϣ
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
		//�˷�����õĶ���ʼ��ֻ��һ��������ģʽ��
		PhoneManager manager=PhoneManager.getPhoneManager(this);
		String title;
		String text;
		Drawable icon;
		//
		title="�豸����:"+manager.getPhoneName1();
		text="ϵͳ�汾:"+manager.getPhoneSystemVersion();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_version);
		//�����ݷ�װ��������
		final PhoneInfo info1=new PhoneInfo(title, text, icon);
		//
		title="ȫ�������ڴ�"+CommonUtil.getFileSize(MemoryManager.getPhoneTotalRamMemory());
		text="ʣ�������ڴ�"+CommonUtil.getFileSize(MemoryManager.getPhoneFreeRamMemory(this));
		icon=getResources().getDrawable(R.drawable.setting_info_icon_space);
		final PhoneInfo info2=new PhoneInfo(title, text, icon);
		//
		title="cpu����"+manager.getPhoneCpuName();
		text="cpu����"+manager.getPhoneCpuNumber();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_cpu);
		final PhoneInfo info3=new PhoneInfo(title, text, icon);
		//
		title="�ֻ��ֱ���"+manager.getResolution();
		text="����ֱ���"+manager.getMaxPhotoSize();
		icon=getResources().getDrawable(R.drawable.setting_info_icon_camera);
		final PhoneInfo info4=new PhoneInfo(title, text, icon);
		//
		title="�����汾"+manager.getPhoneSystemBasebandVersion();
		text="�Ƿ�ROOT"+(manager.isRoot()?"��":"��");
		icon=getResources().getDrawable(R.drawable.setting_info_icon_root);
		final PhoneInfo info5=new PhoneInfo(title, text, icon);
		//
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//��phonemgrAdapter��ļ����з���һ��Ԫ��
				phonemgrAdapter.addDataToAdapter(info1);
				phonemgrAdapter.addDataToAdapter(info2);
				phonemgrAdapter.addDataToAdapter(info3);
				phonemgrAdapter.addDataToAdapter(info4);
				phonemgrAdapter.addDataToAdapter(info5);
				//֪ͨ����
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
//			builder.setTitle("��ص�����Ϣ");
//			//�ڲ������޷����ó�Ա�������ݣ���Ҫfinal����
//			builder.setItems(new String[]{"��ǰ����:"+currentBattery,"����¶�:"+temperatureBattery}, null);
//			builder.show();
//		}
	};
	private void showBatteryMessage() {
		AlertDialog.Builder builder=new AlertDialog.Builder(PhonemgrActivity.this);
		builder.setTitle("��ص�����Ϣ");
		//�ڲ������޷����ó�Ա�������ݣ���Ҫfinal����
		builder.setItems(new String[]{"��ǰ����:"+currentBattery,"����¶�:"+temperatureBattery}, null);
		builder.show();
	}
	private void initMainButton() {
		//������Բ���
		layout_battery=findViewById(R.id.ll_layout_battery);
		layout_battery.setOnClickListener(clickListener);
		//��ʾ��ǰ�����ٷֱ�
		tv_battery=(TextView)findViewById(R.id.tv_battery);
		//��ʾ�������ĵ���
		pb_battery=(ProgressBar)findViewById(R.id.pb_battery);
		pb_loading=(ProgressBar) findViewById(R.id.progressBar);
		//3.ע���ص����㲥������
		broadcastReceiver=new BatteryBroadcastReceiver();
		//��ͼ������
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(broadcastReceiver, filter);
	}
	public class BatteryBroadcastReceiver extends BroadcastReceiver{//1.�̳�BroadcastReceiver
		@Override
		public void onReceive(Context context, Intent intent) {//2.��дonReceive����
			//������״̬�����仯����ִ�����·���
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				Bundle bundle=intent.getExtras();
				//��ȡ����ܵ���  BatteryManager�ײ�����ص���
				int maxBattery=(Integer) bundle.get(BatteryManager.EXTRA_SCALE);
				//��ǰ��ص���
				currentBattery=(Integer) bundle.get(BatteryManager.EXTRA_LEVEL);
				//��ǰ����¶�
				temperatureBattery=(Integer) bundle.get(BatteryManager.EXTRA_TEMPERATURE);
				pb_battery.setMax(maxBattery);
				pb_battery.setProgress(currentBattery);
				int current100=currentBattery*100/maxBattery;//���磺500/1000*100=50
				tv_battery.setText(current100+"%");//��50%
			}
		}		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//�����Ա��ͷ���Դ
		unregisterReceiver(broadcastReceiver);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}	
}
