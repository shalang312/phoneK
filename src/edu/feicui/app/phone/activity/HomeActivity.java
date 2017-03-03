package edu.feicui.app.phone.activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.view.ActionBarView;
import edu.feicui.app.phone.view.CleararcView;
public class HomeActivity extends BaseActivity {
	private ImageView view_homeclear;//һ������Բ
	private TextView view_homeclear_text;//һ�������ı�
	private CleararcView view_homeclear_arc;//һ������Բ�����Զ��壩
	private TextView view_homeclear_score;//һ������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	
		String title=getResources().getString(R.string.app_name);
		initActionBar(title, R.drawable.ic_launcher_1,R.drawable.ic_child_configs, clickListener);
		//��ʼ���ؼ�
		initHomeClear();
		//��������
		initHomeClearData();
	}
	private void initHomeClearData() {
		//���ȫ�������ڴ�RAM
		float totalRam=MemoryManager.getPhoneTotalRamMemory();
		//��ÿ��������ڴ�
		float freeRam=MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		//�����ʹ�õ������ڴ�
		float usedRam=totalRam-freeRam;
		//�������ʹ�������ڴ����
		float usedP=usedRam/totalRam;//����0.5
		int used100=(int) (usedP*100);//����0.5*100=50
		view_homeclear_score.setText(used100+"");
		int angle=(int) (usedP*360);//����0.5*360=180�������ʹ�������ڴ�Ƕ�
		view_homeclear_arc.setAngleWithAnim(angle);
		
	}
	private void initHomeClear() {
		// TODO Auto-generated method stub
		view_homeclear=(ImageView) findViewById(R.id.iv_homeclear);
		view_homeclear_text=(TextView) findViewById(R.id.tv_homeclear_text);
		view_homeclear_score=(TextView) findViewById(R.id.tv_score5);
		view_homeclear_arc=(CleararcView) findViewById(R.id.homeclear_arcs);
		view_homeclear.setOnClickListener(clickListener);
		view_homeclear_text.setOnClickListener(clickListener);
	}
	private OnClickListener clickListener=new OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_left://ActionBar��߰�ť
				//��ת����������ҳ��
				startActivity(AboutActivity.class, R.anim.left_in, R.anim.right_out);
				finish();
				break;
			case R.id.iv_right:
				startActivity(SettingActivity.class, R.anim.right_in, R.anim.left_out);
				finish();
				break;	
			case R.id.iv_homeclear://���һ�������ı���һ������Բ�������������û�����
			case R.id.tv_homeclear_text:
				//���������������е��û�����
				AppInfoManager.getAppInfoManager(HomeActivity.this).killAllProgresses();
				//���³�ʼ���ؼ�����
				initHomeClearData();
				break;
			default:
				break;
			}
		}
	};
	//����6����ť������ҳ����ת
	public void hitHomeitem(View view){
		switch (view.getId()) {
		case R.id.ll_rocket:
			//��ת���ֻ�����ҳ��
			startActivity(SpeedupActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_softmgr:
			//��ת���������ҳ��
			startActivity(SoftmgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_phonemgr:
			//��ת���ֻ����ҳ��
			startActivity(PhonemgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_telmgr:
			//��ת��ͨ�Ŵ�ȫҳ��
			startActivity(TelmgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_filemgr:
			//��ת���ļ�����ҳ��
			startActivity(FilemgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_sdclean:
			//��ת����������ҳ��
			startActivity(SdcleanActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finishAll();
	}
	@Override
	protected void initView() {
	}	
}
