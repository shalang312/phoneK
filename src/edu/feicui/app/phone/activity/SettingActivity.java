package edu.feicui.app.phone.activity;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.NotificationUtil;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;
public class SettingActivity extends BaseActivity {
	private ToggleButton tb_notify;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		String title = getResources().getString(R.string.setting);
		initActionBar(title, R.drawable.btn_homeasup_default, -1,clickListener);
		//初始化主按钮（开机启动，通知图标，消息推送，关于我们等）
		initMainButton();
	}
private void initMainButton() {
		// TODO Auto-generated method stub
		tb_notify=(ToggleButton) findViewById(R.id.tb_toggle_notification);
		//第一次进入时默认打开		
		//getApplicationContext()代表获得整个应用程序的Context对象
		tb_notify.setChecked(NotificationUtil.isOpenNotification(getApplicationContext()));
		tb_notify.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//isChecked表示是否被勾选状态
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){//打开开关按钮，发通知
					NotificationUtil.showAppIconNotification(getApplicationContext());					
				}else{
					NotificationUtil.cancelAppIconNotification(getApplicationContext());
				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		//tb_notify.isChecked()获得开关按钮状态
		super.onDestroy();
		NotificationUtil.setOpenNotification(getApplicationContext(), tb_notify.isChecked());
		finish();
	}
	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int viewID=v.getId();
			switch (viewID) {
			case R.id.iv_left://设置页面的左上角回退按钮
				startActivity(HomeActivity.class);//返回主页面
				finish();//销毁当前Activity，即SettingActivity
				break;
			default:
				break;
			}
			
		}
	};
	public void hitSettingitem(View view){
		int viewID=view.getId();
		switch (viewID) {
		case R.id.rl_setting_about:
			//用来判断是从哪里进入关于我们界面
			Bundle bundle=new Bundle();
			String className=SettingActivity.this.getClass().getSimpleName();//"SettingActivity"
			bundle.putString("className",className);//HashMap.put("className","SettingActivity")
			startActivity(AboutActivity.class, bundle);
			finish();
			break;
		case R.id.rl_setting_help:
			SharedPreferences preferences=getSharedPreferences("Lead_config",Context.MODE_PRIVATE);
			Editor editor=preferences.edit();
			editor.putBoolean("isFirstRun",true);//当点击帮助说明，把"isFirstRun"对应的value值变为true
			editor.commit();
			//用来判断是从哪个页面进入关于我们界面
			Bundle bundle2=new Bundle();
			bundle2.putString("className", SettingActivity.this.getClass().getSimpleName());
			startActivity(LeadActivity.class,bundle2);
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

}
