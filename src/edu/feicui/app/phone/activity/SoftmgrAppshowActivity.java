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
	ProgressBar progressBarArc;//�м䲿�ֵĽ�����
	CheckBox cb_delall;//��ߵ�CheckBox
	Button btn_delall;//�����·��İ�ť
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr_appshow);
//		String title=getResources().getString(R.string.allsoft);
//		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//���ûȡ��ID����ֱ����Ĭ�ϵ�
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
		//����������
		appAdapter=new AppAdapter(this);
		progressBarArc=(ProgressBar) findViewById(R.id.progressBar);
		appListView=(ListView) findViewById(R.id.listviewLoad);
		cb_delall=(CheckBox) findViewById(R.id.cb_all);//����ȫѡ
		cb_delall.setOnCheckedChangeListener(changeListener);
		btn_delall=(Button) findViewById(R.id.btn_delall);
		btn_delall.setOnClickListener(clickListener);
		appListView.setAdapter(appAdapter);
		//������������Դ
		asynLoadApp();
	}
	//���ڴ�����������ϵͳ������û���������������
	private List<AppInfo> appInfos=null;
	private void asynLoadApp() {
		// TODO Auto-generated method stub
		progressBarArc.setVisibility(View.VISIBLE); 
		appListView.setVisibility(View.INVISIBLE);
		//����һ���µ��߳�,���������ʱ�����������ȡ������Դ
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//��Ϊ�����߳�
				switch (id) {
				case R.id.rl_soft_all://��ȡ���������Ϣ
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
					//�ӹ����߳��������߳��У����߳�һ��������ʾUI�����棩
					@Override
					public void run() {
						//��������ʾʱ����������ʧ
						progressBarArc.setVisibility(View.INVISIBLE);
						appListView.setVisibility(View.VISIBLE);
						appAdapter.setDataToAdapter(appInfos);//�����ݷŵ�������������
						appAdapter.notifyDataSetChanged();//���ݸñ�ʱ�����ô˷���
					}
				});
			}
		}).start();
	}
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//ȫѡ����
			//ͨ��listView��getAdapter()�ķ�����ô�listView��������
//			AppAdapter appListAdapter=(AppAdapter) appListView.getAdapter();
			//�����������д洢���ݵļ���
			List<AppInfo> appInfos=appAdapter.getDataList();
			for(AppInfo appInfo:appInfos){
				//�õ�ÿһ��Ӧ�ó���
				appInfo.setDel(isChecked);
			}
			//appListAdapter��appAdapter����ͬһ����������ֻ�����Ʋ�һ��
			appAdapter.notifyDataSetChanged();
		}
	};
	
	OnClickListener clickListener=new OnClickListener() {	
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_left:
				//���ٵ�ǰ������ֱ�ӻص�֮ǰ����
				finish();
				break;
			case R.id.btn_delall:
				appInfos=appAdapter.getDataList();
				for(AppInfo appInfo:appInfos){
					if(appInfo.isDel()){//ֻж�ع�ѡ��Ӧ�ó���
						//��ȡÿ��Ӧ�ó���İ���
						String packageName=appInfo.getPackageInfo().packageName;
						//����ͼΪж�������ͼ
						Intent intent=new Intent(Intent.ACTION_DELETE);
						//��������İ�����ж�ش����
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
