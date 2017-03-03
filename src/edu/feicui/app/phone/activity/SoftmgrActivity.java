package edu.feicui.app.phone.activity;
import java.text.DecimalFormat;

import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.biz.MemoryManager;
import edu.feicui.app.phone.view.PiechartView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
public class SoftmgrActivity extends BaseActivity {
	private PiechartView piechartView;//����ͼ��ͼ
	private ProgressBar phoneSpace;//�ֻ��ռ������
	private ProgressBar sdcardSpace;//sdcard�ռ������
	private TextView phoneSpaceMsg;//�ֻ��ռ���Ϣ ���С�ȫ��
	private TextView sdcardSpaceMsg;//sdcard�ռ���Ϣ  ���С�ȫ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr);
		String title=getResources().getString(R.string.softmgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//��ʼ����ؿؼ�
		initSpace();
		//������ؿؼ����ݣ���ʼ����
		initSpaceData();
	}
	private void initSpaceData() {
		//�ֻ�����ȫ���ռ�(����sysĿ¼��catchĿ¼,������dataĿ¼)
		long phoneSelfTotal=MemoryManager.getPhoneSelfSize();
		//��ȡ�ֻ�������е�
		long phoneSelfUnused=MemoryManager.getPhoneSelfFreeSize();
		//��ʹ�õ�����
		long phoneSelfUsed=phoneSelfTotal-phoneSelfUnused;
		//�ֻ��Դ�sdcard�ռ䣨��mnt��
		long phoneSelfSDCardTotal=MemoryManager.getPhoneSelfSDCardSize();
		//���ã����У�
		long phoneSelfSDCardUnused=MemoryManager.getPhoneSelfSDCardFreeSize();
		//�Ѿ�ʹ��
		long phoneSelfSDCardUsed=phoneSelfSDCardTotal-phoneSelfSDCardUnused;
		//�ֻ����ô洢���ռ�(Second_storage    storage/sdcard1)
		long phoneOutSDCardTotal=MemoryManager.getPhoneOutSDCardSize(getApplicationContext());
		long phoneOutSDCardUnused=MemoryManager.getPhoneOutSDCardFreeSize(getApplicationContext());
		long phoneOutSDCardUsed=phoneOutSDCardTotal-phoneOutSDCardUnused;
		//�ֻ��ܿռ�
		float phoneAllSpace=phoneSelfTotal+phoneOutSDCardTotal+phoneSelfSDCardTotal;
		//�������ͼ����
		float phoneSpaceF=(phoneSelfTotal+phoneSelfSDCardTotal)/phoneAllSpace;
		float sdcardSpaceF=1-phoneSpaceF;
		//������ռ����С�������λ
		DecimalFormat df=new DecimalFormat("#.00");
		//0.777(float)-->0.78(String)-->0.78(float)
		phoneSpaceF=Float.parseFloat(df.format(phoneSpaceF));
		sdcardSpaceF=Float.parseFloat(df.format(sdcardSpaceF));
		//���ñ���ͼ����
		piechartView.setPiechartProportionWithAnim(phoneSpaceF,sdcardSpaceF);
		//�����ֻ�(sys��catch)�ʹ洢�����ú�ȫ���ڴ�
		long phoneTotalSpace=phoneSelfTotal+phoneSelfSDCardTotal;
		long phoneUnusedSpace=phoneSelfUnused+phoneSelfSDCardUnused;
		long phoneUsedSpace=phoneTotalSpace-phoneUnusedSpace;
		//���ÿռ�ʹ�����()����/ȫ��
		phoneSpaceMsg.setText("����:"+CommonUtil.getFileSize(phoneUnusedSpace)+"/"+
								CommonUtil.getFileSize(phoneTotalSpace));
		sdcardSpaceMsg.setText("����:"+CommonUtil.getFileSize(phoneOutSDCardUnused)+"/"+CommonUtil.getFileSize(phoneOutSDCardTotal));
		//���ÿռ�ʹ���������
		phoneSpace.setMax((int)(phoneTotalSpace/1024));//��Bװ����K
		phoneSpace.setProgress((int)(phoneUsedSpace/1024));
		sdcardSpace.setMax((int)(phoneOutSDCardTotal/1024));
		sdcardSpace.setProgress((int)(phoneOutSDCardUnused/1024));
	}
	private void initSpace() {
		piechartView=(PiechartView) findViewById(R.id.piechart);
		phoneSpace=(ProgressBar) findViewById(R.id.pb_phone_space);
		sdcardSpace=(ProgressBar) findViewById(R.id.pb_sdcard_space);
		phoneSpaceMsg=(TextView) findViewById(R.id.tv_phone_space_msg);
		sdcardSpaceMsg=(TextView) findViewById(R.id.tv_sdcard_space_msg);
	}
	public void hitListitem(View view){
		switch (view.getId()) {
		case R.id.rl_soft_all:
		case R.id.rl_soft_sys:
		case R.id.rl_soft_use:
			Bundle bundle=new Bundle();
			bundle.putInt("id", view.getId());
			startActivity(SoftmgrAppshowActivity.class, bundle);
			break;
		default:
			break;
		}
	}
	OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_left:
				startActivity(HomeActivity.class, R.anim.in_up, R.anim.out_down);
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
