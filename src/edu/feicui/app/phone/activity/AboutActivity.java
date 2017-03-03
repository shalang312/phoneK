package edu.feicui.app.phone.activity;
import edu.feicui.app.phone.base.BaseActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		String title=getResources().getString(R.string.about);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
	}
	private OnClickListener clickListener=new OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_left:
				String fromClassName=getIntent().getStringExtra("className");
				if(fromClassName==null||fromClassName.equals("")){
					startActivity(HomeActivity.class);
					finish();
					return;
				}
				if(fromClassName.equals(SettingActivity.class.getSimpleName())){
					startActivity(SettingActivity.class);
				}else{
					startActivity(HomeActivity.class);
				}
				finish();
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
