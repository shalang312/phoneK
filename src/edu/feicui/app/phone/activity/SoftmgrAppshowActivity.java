package edu.feicui.app.phone.activity;
import java.util.List;

import edu.feicui.app.phone.adapter.AppAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.entity.AppInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
public class SoftmgrAppshowActivity extends BaseActivity {
	int id;
	ListView appListView;
	AppAdapter appAdapter;
	ProgressBar progressBarArc;//中间部分的进度条
	CheckBox cb_delall;//左边的CheckBox
	Button btn_delall;//布局下方的按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr_appshow);
//		String title=getResources().getString(R.string.allsoft);
//		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//如果没取到ID，就直接用默认的
		id=getIntent().getIntExtra("id", R.id.rl_soft_all);
		String title=null;
		switch (id) {
		case R.id.rl_soft_all:
			title=getResources().getString(R.string.allsoft);
			break;
		case R.id.rl_soft_sys:
			title=getResources().getString(R.string.syssoft);
			break;
		case R.id.rl_soft_use:
			title=getResources().getString(R.string.usesoft);
			break;
		default:
			break;
		}
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		this.id=id;
		//创建适配器
		appAdapter=new AppAdapter(this);
		progressBarArc=(ProgressBar) findViewById(R.id.progressBar);
		appListView=(ListView) findViewById(R.id.listviewLoad);
		cb_delall=(CheckBox) findViewById(R.id.cb_all);//勾上全选
		cb_delall.setOnCheckedChangeListener(changeListener);
		btn_delall=(Button) findViewById(R.id.btn_delall);
		btn_delall.setOnClickListener(clickListener);
		appListView.setAdapter(appAdapter);
		//加载适配器资源
		asynLoadApp();
	}
	//用于存放软件（包括系统软件，用户软件等所有软件）
	private List<AppInfo> appInfos=null;
	private void asynLoadApp() {
		// TODO Auto-generated method stub
		progressBarArc.setVisibility(View.VISIBLE); 
		appListView.setVisibility(View.INVISIBLE);
		//创建一个新的线程,用来处理耗时操作，比如获取数据资源
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//此为工作线程
				switch (id) {
				case R.id.rl_soft_all://获取所有软件信息
					appInfos=AppInfoManager.getAppInfoManager(SoftmgrAppshowActivity.this).getAllPackageInfo(true);
					break;
				case R.id.rl_soft_sys:
					appInfos=AppInfoManager.getAppInfoManager(getApplicationContext()).getSystemPackageInfo(true);
					break;
				case R.id.rl_soft_use:
					appInfos=AppInfoManager.getAppInfoManager(getApplicationContext()).getUserPackageInfo(true);
					break;
				default:
					break;
				}
				runOnUiThread(new Runnable() {
					//从工作线程跳到主线程中，主线程一般用来显示UI（界面）
					@Override
					public void run() {
						//当数据显示时，进度条消失
						progressBarArc.setVisibility(View.INVISIBLE);
						appListView.setVisibility(View.VISIBLE);
						appAdapter.setDataToAdapter(appInfos);//将数据放到适配器集合中
						appAdapter.notifyDataSetChanged();//数据该表时，调用此方法
					}
				});
			}
		}).start();
	}
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//全选监听
			//通过listView的getAdapter()的方法获得此listView的适配器
//			AppAdapter appListAdapter=(AppAdapter) appListView.getAdapter();
			//返回适配器中存储数据的集合
			List<AppInfo> appInfos=appAdapter.getDataList();
			for(AppInfo appInfo:appInfos){
				//拿到每一个应用程序
				appInfo.setDel(isChecked);
			}
			//appListAdapter和appAdapter都是同一个适配器，只是名称不一样
			appAdapter.notifyDataSetChanged();
		}
	};
	
	OnClickListener clickListener=new OnClickListener() {	
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_left:
				//销毁当前窗口则直接回到之前窗口
				finish();
				break;
			case R.id.btn_delall:
				appInfos=appAdapter.getDataList();
				for(AppInfo appInfo:appInfos){
					if(appInfo.isDel()){//只卸载勾选的应用程序
						//获取每个应用程序的包名
						String packageName=appInfo.getPackageInfo().packageName;
						//此意图为卸载软件意图
						Intent intent=new Intent(Intent.ACTION_DELETE);
						//根据软件的包名，卸载此软件
						intent.setData(Uri.parse("package:"+packageName));
						startActivity(intent);
					}				
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void initView() {
	}
}
