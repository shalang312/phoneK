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
	private PiechartView piechartView;//饼型图视图
	private ProgressBar phoneSpace;//手机空间进度条
	private ProgressBar sdcardSpace;//sdcard空间进度条
	private TextView phoneSpaceMsg;//手机空间信息 空闲、全部
	private TextView sdcardSpaceMsg;//sdcard空间信息  空闲、全部
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmgr);
		String title=getResources().getString(R.string.softmgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//初始化相关控件
		initSpace();
		//放置相关控件数据（初始化）
		initSpaceData();
	}
	private void initSpaceData() {
		//手机自身全部空间(包括sys目录和catch目录,不包括data目录)
		long phoneSelfTotal=MemoryManager.getPhoneSelfSize();
		//获取手机自身空闲的
		long phoneSelfUnused=MemoryManager.getPhoneSelfFreeSize();
		//以使用的容量
		long phoneSelfUsed=phoneSelfTotal-phoneSelfUnused;
		//手机自带sdcard空间（即mnt）
		long phoneSelfSDCardTotal=MemoryManager.getPhoneSelfSDCardSize();
		//可用（空闲）
		long phoneSelfSDCardUnused=MemoryManager.getPhoneSelfSDCardFreeSize();
		//已经使用
		long phoneSelfSDCardUsed=phoneSelfSDCardTotal-phoneSelfSDCardUnused;
		//手机外置存储卡空间(Second_storage    storage/sdcard1)
		long phoneOutSDCardTotal=MemoryManager.getPhoneOutSDCardSize(getApplicationContext());
		long phoneOutSDCardUnused=MemoryManager.getPhoneOutSDCardFreeSize(getApplicationContext());
		long phoneOutSDCardUsed=phoneOutSDCardTotal-phoneOutSDCardUnused;
		//手机总空间
		float phoneAllSpace=phoneSelfTotal+phoneOutSDCardTotal+phoneSelfSDCardTotal;
		//计算饼型图比例
		float phoneSpaceF=(phoneSelfTotal+phoneSelfSDCardTotal)/phoneAllSpace;
		float sdcardSpaceF=1-phoneSpaceF;
		//保留所占比例小数点后两位
		DecimalFormat df=new DecimalFormat("#.00");
		//0.777(float)-->0.78(String)-->0.78(float)
		phoneSpaceF=Float.parseFloat(df.format(phoneSpaceF));
		sdcardSpaceF=Float.parseFloat(df.format(sdcardSpaceF));
		//设置饼型图比例
		piechartView.setPiechartProportionWithAnim(phoneSpaceF,sdcardSpaceF);
		//设置手机(sys和catch)和存储卡可用和全部内存
		long phoneTotalSpace=phoneSelfTotal+phoneSelfSDCardTotal;
		long phoneUnusedSpace=phoneSelfUnused+phoneSelfSDCardUnused;
		long phoneUsedSpace=phoneTotalSpace-phoneUnusedSpace;
		//设置空间使用情况()可用/全部
		phoneSpaceMsg.setText("可用:"+CommonUtil.getFileSize(phoneUnusedSpace)+"/"+
								CommonUtil.getFileSize(phoneTotalSpace));
		sdcardSpaceMsg.setText("可用:"+CommonUtil.getFileSize(phoneOutSDCardUnused)+"/"+CommonUtil.getFileSize(phoneOutSDCardTotal));
		//设置空间使用情况进度
		phoneSpace.setMax((int)(phoneTotalSpace/1024));//把B装换程K
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
