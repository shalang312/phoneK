package edu.feicui.app.phone.activity;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.app.phone.adapter.RunningAppAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.biz.SystemManager;
import edu.feicui.app.phone.entity.RunningAppInfo;
public class SpeedupActivity extends BaseActivity {
	private ListView runningApplistView;
	private RunningAppAdapter runningAppAdapter;
	private Button btn_clear;//һ������
	private CheckBox cb_checkClearAll;//ȫѡ
	private Button btn_showall;//��ʾAPP��ϵͳ���û���
	private ProgressBar progressBarArc;//����ListView֮ǰ��progressbar
	private TextView tv_ramMessage;//�����ڴ��ı�
	private ProgressBar pb_ram;//�ڴ����
	private TextView tv_phoneName;//����������
	private TextView tv_phoneModel;//�ֻ��ͺ�
	private Map<Integer,List<RunningAppInfo>> runningAppInfos=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedup);
		String title=getResources().getString(R.string.speedup);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//������Ӧ�ó����б�ؼ�
		runningApplistView=(ListView) findViewById(R.id.listviewLoad);
		runningAppAdapter=new RunningAppAdapter(this);
		runningApplistView.setAdapter(runningAppAdapter);
		//��ʼ���������ؼ�
		initMainButton();
		//��������
		loadData();
	}
	private void loadData() {
		//һ��ʼ��������ʾ��ListView����ʾ
		progressBarArc.setVisibility(View.VISIBLE);
		runningApplistView.setVisibility(View.INVISIBLE);
		//�ڹ����߳��У���ȡ����
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//���HashMap����runningAppInfos�����ݣ����key=0�����ϵͳ���̼���,...
				runningAppInfos=AppInfoManager.getAppInfoManager(getApplicationContext()).getRunningAppInfos();
				//�����ڽ�������ʾ��Ҫ�����߳��ﴦ��
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						initRamData();
						progressBarArc.setVisibility(View.INVISIBLE);
						runningApplistView.setVisibility(View.VISIBLE);
						//key=1,��ȡ�û����̼���
						runningAppAdapter.setDataToAdapter(runningAppInfos.get(AppInfoManager.
								RUNNING_APP_TYPE_USER));
						runningAppAdapter.setState(RunningAppAdapter.STATE_SHOW_USER);
						runningAppAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}
	private void initMainButton() {
		// TODO Auto-generated method stub
		tv_phoneName=(TextView) findViewById(R.id.tv_phoneName);
		tv_phoneModel=(TextView) findViewById(R.id.tv_phoneModel);
		pb_ram=(ProgressBar) findViewById(R.id.pb_ram);
		tv_ramMessage=(TextView) findViewById(R.id.tv_ramMessage);
		progressBarArc=(ProgressBar) findViewById(R.id.progressBar);
		cb_checkClearAll=(CheckBox) findViewById(R.id.cb_all);
		btn_clear=(Button) findViewById(R.id.btn_onekeyClear);
		btn_showall=(Button) findViewById(R.id.btn_showapp);
		//���ؼ����ü���
		btn_clear.setOnClickListener(clickListener);
		btn_showall.setOnClickListener(clickListener);
		cb_checkClearAll.setOnCheckedChangeListener(changeListener);//ȫѡ
		initPhoneData();//�ֻ��ͺŵ�����
		initRamData();//�ֻ��ڴ�����
	}
	private void initPhoneData() {
		// TODO Auto-generated method stub
		tv_phoneName.setText(SystemManager.getPhoneName().toUpperCase());
		tv_phoneModel.setText(SystemManager.getPhoneModelName());
	}
	private void initRamData() {
		//���ȫ�������ڴ�ram rom
		float totalRam=MemoryManager.getPhoneTotalRamMemory();
		//��ÿ����ڴ�
		float freeRam=MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		//�����ʹ�������ڴ�
		float usedRam=totalRam-freeRam;
		tv_ramMessage.setText("�����ڴ�: "+CommonUtil.getFileSize((long)usedRam)+"/"+CommonUtil.getFileSize((long)totalRam));
		//�������ʹ�������ڴ����
		float usedP=usedRam/totalRam;//�Ƚ�ռ��0.7
		int used100=(int) (usedP*100);//�������ʹ�������ڴ�ٷֱ�
		pb_ram.setMax(100);
		pb_ram.setProgress(used100);
	}
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//�õ�����Ӧ��
			List<RunningAppInfo> appInfos=runningAppAdapter.getDataList();
			for(RunningAppInfo appInfo:appInfos){//��������Ӧ�ó���
				//ȫѡ
				appInfo.setClear(isChecked);
			}
			//����������
			runningAppAdapter.notifyDataSetChanged();
		}
	};
	OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_left:
				startActivity(HomeActivity.class);
				finish();
				break;
			case R.id.btn_onekeyClear://һ������ť
				List<RunningAppInfo> appInfos=runningAppAdapter.getDataList();
				for(RunningAppInfo appInfo:appInfos){//����ÿһ���������
					if(appInfo.isClear()){//����˽��̹�ѡ�ˣ�������
						String packageName=appInfo.getPackageName();
						//ͨ��Ӧ�ó�������������
						AppInfoManager.getAppInfoManager(getApplicationContext()).killProgress(packageName);
						//���¼���ˢ������
						loadData();
						cb_checkClearAll.setChecked(false);
					}
				}
				break;
			case R.id.btn_showapp:
				if(runningAppInfos!=null){
					//��ȡ��ǰ״̬
					switch (runningAppAdapter.getState()) {
					//�����ǰ״̬ʱ�û�״̬
					case RunningAppAdapter.STATE_SHOW_USER:
						//���û�����״̬�£������ť������ϵͳ������Ϣ
						runningAppAdapter.setDataToAdapter(
								runningAppInfos.get(AppInfoManager.RUNNING_APP_TYPE_SYS));
						runningAppAdapter.setState(RunningAppAdapter.STATE_SHOW_SYS);
						btn_showall.setText(getResources().getString(R.string.speedup_show_userapp));
						break;
					case RunningAppAdapter.STATE_SHOW_SYS:
						runningAppAdapter.setDataToAdapter(runningAppInfos.get(
								AppInfoManager.RUNNING_APP_TYPE_USER));
						runningAppAdapter.setState(RunningAppAdapter.STATE_SHOW_USER);
						btn_showall.setText(getResources().getString(R.string.speedup_show_sysapp));
						
						break;
					default:
						break;
					}
					runningAppAdapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
}
