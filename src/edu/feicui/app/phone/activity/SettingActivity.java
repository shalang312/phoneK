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
		//��ʼ������ť������������֪ͨͼ�꣬��Ϣ���ͣ��������ǵȣ�
		initMainButton();
	}
private void initMainButton() {
		// TODO Auto-generated method stub
		tb_notify=(ToggleButton) findViewById(R.id.tb_toggle_notification);
		//��һ�ν���ʱĬ�ϴ�		
		//getApplicationContext()����������Ӧ�ó����Context����
		tb_notify.setChecked(NotificationUtil.isOpenNotification(getApplicationContext()));
		tb_notify.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//isChecked��ʾ�Ƿ񱻹�ѡ״̬
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){//�򿪿��ذ�ť����֪ͨ
					NotificationUtil.showAppIconNotification(getApplicationContext());					
				}else{
					NotificationUtil.cancelAppIconNotification(getApplicationContext());
				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		//tb_notify.isChecked()��ÿ��ذ�ť״̬
		super.onDestroy();
		NotificationUtil.setOpenNotification(getApplicationContext(), tb_notify.isChecked());
		finish();
	}
	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int viewID=v.getId();
			switch (viewID) {
			case R.id.iv_left://����ҳ������Ͻǻ��˰�ť
				startActivity(HomeActivity.class);//������ҳ��
				finish();//���ٵ�ǰActivity����SettingActivity
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
			//�����ж��Ǵ��������������ǽ���
			Bundle bundle=new Bundle();
			String className=SettingActivity.this.getClass().getSimpleName();//"SettingActivity"
			bundle.putString("className",className);//HashMap.put("className","SettingActivity")
			startActivity(AboutActivity.class, bundle);
			finish();
			break;
		case R.id.rl_setting_help:
			SharedPreferences preferences=getSharedPreferences("Lead_config",Context.MODE_PRIVATE);
			Editor editor=preferences.edit();
			editor.putBoolean("isFirstRun",true);//���������˵������"isFirstRun"��Ӧ��valueֵ��Ϊtrue
			editor.commit();
			//�����ж��Ǵ��ĸ�ҳ�����������ǽ���
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
