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
	private ImageView view_homeclear;//一键清理圆
	private TextView view_homeclear_text;//一键清理文本
	private CleararcView view_homeclear_arc;//一键清理圆环（自定义）
	private TextView view_homeclear_score;//一键清理
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	
		String title=getResources().getString(R.string.app_name);
		initActionBar(title, R.drawable.ic_launcher_1,R.drawable.ic_child_configs, clickListener);
		//初始化控件
		initHomeClear();
		//放置数据
		initHomeClearData();
	}
	private void initHomeClearData() {
		//获得全部运行内存RAM
		float totalRam=MemoryManager.getPhoneTotalRamMemory();
		//获得空闲运行内存
		float freeRam=MemoryManager.getPhoneFreeRamMemory(getApplicationContext());
		//获得已使用的运行内存
		float usedRam=totalRam-freeRam;
		//计算出已使用运行内存比例
		float usedP=usedRam/totalRam;//比如0.5
		int used100=(int) (usedP*100);//比如0.5*100=50
		view_homeclear_score.setText(used100+"");
		int angle=(int) (usedP*360);//比如0.5*360=180计算出已使用运行内存角度
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
			case R.id.iv_left://ActionBar左边按钮
				//跳转到关于我们页面
				startActivity(AboutActivity.class, R.anim.left_in, R.anim.right_out);
				finish();
				break;
			case R.id.iv_right:
				startActivity(SettingActivity.class, R.anim.right_in, R.anim.left_out);
				finish();
				break;	
			case R.id.iv_homeclear://点击一键清理文本和一键清理圆环都可以清理用户进程
			case R.id.tv_homeclear_text:
				//清理所有正在运行的用户进程
				AppInfoManager.getAppInfoManager(HomeActivity.this).killAllProgresses();
				//重新初始化控件数据
				initHomeClearData();
				break;
			default:
				break;
			}
		}
	};
	//监听6个按钮，进行页面跳转
	public void hitHomeitem(View view){
		switch (view.getId()) {
		case R.id.ll_rocket:
			//跳转到手机加速页面
			startActivity(SpeedupActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_softmgr:
			//跳转到软件管理页面
			startActivity(SoftmgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_phonemgr:
			//跳转到手机检测页面
			startActivity(PhonemgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_telmgr:
			//跳转到通信大全页面
			startActivity(TelmgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_filemgr:
			//跳转到文件管理页面
			startActivity(FilemgrActivity.class, R.anim.down_in, 
					R.anim.up_out);
			break;
		case R.id.ll_sdclean:
			//跳转到垃圾清理页面
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
