package edu.feicui.app.phone.base;
import java.util.ArrayList;
import java.util.Iterator;

import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.util.LogUtil;
import edu.feicui.app.phone.view.ActionBarView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
/**
 * 基础Activity，定义基础方法，用于继承
 *
 */
public abstract class BaseActivity extends Activity {
	private static final String TAG = "ActivityLife";
	// --------------------------------------------------------------------------------------
	/** 用来保存所在存在的Activity */
	private static ArrayList<BaseActivity> onlineActivityList = new ArrayList<BaseActivity>();

	/** 依次退出当前存在的所有Activity */
	public static void finishAll() {
		Iterator<BaseActivity> iterator = onlineActivityList.iterator();
		while (iterator.hasNext()) {
			iterator.next().finish();
		}
	}
	public void addActivity(BaseActivity activity){
		onlineActivityList.add(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onlineActivityList.add(this);
	}

	protected void startActivity(Class<?> targetClass) {
		Intent intent = new Intent(this, targetClass);
		startActivity(intent);
	}

	protected void startActivity(Class<?> targetClass, Bundle bundle) {
		Intent intent = new Intent(this, targetClass);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	protected void startActivity(Class<?> targetClass, int inAnimID,
			int outAnimID) {
		Intent intent = new Intent(this, targetClass);
		startActivity(intent);
		overridePendingTransition(inAnimID, outAnimID);
	}

	protected void startActivity(Class<?> targetClass, int inAnimID,
			int outAnimID, Bundle bundle) {
		Intent intent = new Intent(this, targetClass);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(inAnimID, outAnimID);
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.d(TAG, getClass().getSimpleName() + " onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.d(TAG, getClass().getSimpleName() + " onResume()");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.d(TAG, getClass().getSimpleName() + " onPause()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.d(TAG, getClass().getSimpleName() + " onStop()");
	}
//Handler------------------------------------------------------------	
	public Handler myHandler=new Handler(){
		//在主线程里更新数据
		public void handleMessage(android.os.Message msg) {
			myHandlerMessage(msg);
		};		
	};
	//在主线程里更新数据
	protected void myHandlerMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
//---------------------------------------------------------------------------	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.d(TAG, getClass().getSimpleName() + " onDestroy()");
		if (onlineActivityList.contains(this)) {
			onlineActivityList.remove(this);
		}
	}	
	//ActionBar初始化
	protected void initActionBar(String title,int leftResID,int rightResID,
			OnClickListener listener){
		ActionBarView actionBarView=(ActionBarView) findViewById(R.id.actionbar);
		//给ActionBar设置标题，图片，点击效果
		actionBarView.initActionBar(title, leftResID, rightResID, listener);
	}
	protected abstract void initView();
	
 }
