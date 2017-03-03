package edu.feicui.app.phone.activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
public class LogoActivity extends Activity {
	ImageView img_logo;
	Animation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		//��ʼ�ؼ�������
		img_logo=(ImageView) findViewById(R.id.iv_Logo);
		animation=AnimationUtils.loadAnimation(this, R.anim.anim_logo);
		animation.setAnimationListener(animationListener);
		//logoͼ���	����ʼ����
		img_logo.startAnimation(animation);
	}
	private AnimationListener animationListener=new AnimationListener(){
		//������ʼ
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		//��������
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(LogoActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
		//�����ظ�
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}		
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}
}