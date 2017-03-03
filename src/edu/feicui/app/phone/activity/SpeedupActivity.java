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
	private Button btn_clear;//一键清理
	private CheckBox cb_checkClearAll;//全选
	private Button btn_showall;//显示APP（系统？用户）
	private ProgressBar progressBarArc;//加载ListView之前的progressbar
	private TextView tv_ramMessage;//运行内存文本
	private ProgressBar pb_ram;//内存情况
	private TextView tv_phoneName;//制作商名称
	private TextView tv_phoneModel;//手机型号
	private Map<Integer,List<RunningAppInfo>> runningAppInfos=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedup);
		String title=getResources().getString(R.string.speedup);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//运行中应用程序列表控件
		runningApplistView=(ListView) findViewById(R.id.listviewLoad);
		runningAppAdapter=new RunningAppAdapter(this);
		runningApplistView.setAdapter(runningAppAdapter);
		//初始化主操作控件
		initMainButton();
		//加载数据
		loadData();
	}
	private void loadData() {
		//一开始进度条显示，ListView不显示
		progressBarArc.setVisibility(View.VISIBLE);
		runningApplistView.setVisibility(View.INVISIBLE);
		//在工作线程中，获取数据
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//获得HashMap集合runningAppInfos的数据，如果key=0，获得系统进程集合,...
				runningAppInfos=AppInfoManager.getAppInfoManager(getApplicationContext()).getRunningAppInfos();
				//数据在界面上显示，要在主线程里处理
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						initRamData();
						progressBarArc.setVisibility(View.INVISIBLE);
						runningApplistView.setVisibility(View.VISIBLE);
						//key=1,获取用户进程集合
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
		//给控件设置监听
		btn_clear.setOnClickListener(clickListener);
		btn_showall.setOnClickListener(clickListener);
		cb_checkClearAll.setOnCheckedChangeListener(changeListener);//全选
		initPhoneData();//手机型号等数据
		initRamData();//手机内存数据
	}
	private void initPhoneData() {
		// TODO Auto-generated method stub
		tv_phoneName.setText(SystemManager.getPhoneName().toUpperCase());
		tv_phoneModel.setText(SystemManager.getPhoneModelName());
	}
	private void initRamData() {
		//获得全部运行内存ram rom
		float totalRam=MemoryManager.getPhoneTotalRamMemory();
		//获得空闲内存
		float freeRam=MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		//获得已使用运行内存
		float usedRam=totalRam-freeRam;
		tv_ramMessage.setText("可用内存: "+CommonUtil.getFileSize((long)usedRam)+"/"+CommonUtil.getFileSize((long)totalRam));
		//计算出已使用运行内存比例
		float usedP=usedRam/totalRam;//比较占了0.7
		int used100=(int) (usedP*100);//计算出已使用运行内存百分比
		pb_ram.setMax(100);
		pb_ram.setProgress(used100);
	}
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//拿到所有应用
			List<RunningAppInfo> appInfos=runningAppAdapter.getDataList();
			for(RunningAppInfo appInfo:appInfos){//遍历所有应用程序
				//全选
				appInfo.setClear(isChecked);
			}
			//更新适配器
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
			case R.id.btn_onekeyClear://一键清理按钮
				List<RunningAppInfo> appInfos=runningAppAdapter.getDataList();
				for(RunningAppInfo appInfo:appInfos){//遍历每一个进程软件
					if(appInfo.isClear()){//如果此进程勾选了，就清理
						String packageName=appInfo.getPackageName();
						//通过应用程序包名清理进程
						AppInfoManager.getAppInfoManager(getApplicationContext()).killProgress(packageName);
						//重新加载刷新数据
						loadData();
						cb_checkClearAll.setChecked(false);
					}
				}
				break;
			case R.id.btn_showapp:
				if(runningAppInfos!=null){
					//获取当前状态
					switch (runningAppAdapter.getState()) {
					//如果当前状态时用户状态
					case RunningAppAdapter.STATE_SHOW_USER:
						//在用户进程状态下，点击按钮，加载系统进程信息
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
