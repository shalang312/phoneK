package edu.feicui.app.phone.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.adapter.BasePagerAdapter;
import edu.feicui.app.phone.service.MusicServices;
public class LeadActivity extends BaseActivity implements OnClickListener{
	//三个ImageView类型的Icon圆点
	ImageView[] icons=new ImageView[3]; 
	TextView tv_skip;
	ViewPager viewPager;
	BasePagerAdapter leadPagerAdapter;
	//是否来自设置页面
	boolean isFromSetting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		//判断是从哪个位置进入引导页面
		String fromClassName=intent.getStringExtra("className");
		//判断是否是从设置页面进入引导页面
		if(fromClassName!=null&&fromClassName.equals("SettingActivity")){
			isFromSetting=true;
		}
		//用于获取用户第一此登录的信息
		SharedPreferences preferences=getSharedPreferences("Lead_config", Context.MODE_PRIVATE);
		//true表示取不到数据，默认返回为true（表示为第一次登录）
		boolean isFirstRun=preferences.getBoolean("isFirstRun",true);
		if(!isFirstRun){
			//当用户不是第一次登录，直接进入LogoActivity
			startActivity(LogoActivity.class);
			finish();
		}else{
			setContentView(R.layout.activity_lead);
			//初始化引导图片
			initLeadIcon();
			//操作适配器
			initViewPager();
			//拿数据，获取viewpager中三张视图，再放入视图集合中
			initPagerData();
			//引导页面播放音乐，启动	服务
			Intent musicIntent=new Intent(this, MusicServices.class);
			startService(musicIntent);
		}
	}
	private void initPagerData() {
		ImageView imageView=null;
		//getLayoutInflater() 获得布局加载器
		//三种布局加载器方式都可以
		//View.inflate(context, resource, root);
		//getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//ImageView imageView=new ImageView(this);
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		//通过布局加载器加载我们的视图即创建此视图对象
		imageView.setImageResource(R.drawable.adware_style_applist);
		//将图片放入imageview试图中
		leadPagerAdapter.addViewToAdapter(imageView);
		//将此含有图片的视图，放入viewList视图集合中
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_banner);
		leadPagerAdapter.addViewToAdapter(imageView);
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_creditswall);
		leadPagerAdapter.addViewToAdapter(imageView);
		//提醒资源变化更新
		//当viewpager里数据改变时，调用此方法，重新加载viewpager
		leadPagerAdapter.notifyDataSetChanged();
	}
	private void initViewPager() {
		//解析viewPager控件，创建ViewPager对象
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		leadPagerAdapter=new BasePagerAdapter(this);
		//设置适配器
		viewPager.setAdapter(leadPagerAdapter);
		//给viewpager设置滑动改变监听
		viewPager.setOnPageChangeListener(pageChangeListener);		
	}
	OnPageChangeListener pageChangeListener=new OnPageChangeListener() {
		//页面滑动时设置icon变色
		//arg0代表position
		@Override
		public void onPageSelected(int arg0) {
			//让tv_skip不显示
			tv_skip.setVisibility(View.INVISIBLE);
			if(arg0>=icons.length-1){
				tv_skip.setVisibility(View.VISIBLE);
			}
			//更新icon图标颜色
			for(int num=0;num<icons.length;num++){
				//先设置默认图标为无色
				icons[num].setImageResource(R.drawable.adware_style_default);				
			}
			//滑到第几个页面，就把第几个icon图标设置为有色
			icons[arg0].setImageResource(R.drawable.adware_style_selected);
		}		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	//对控件进行初始化(封装控件)
	@Override
	protected void initView() {	
	}
	@Override
	public void onClick(View v) {
		//保存用户第一次登录信息
		savePreferences();
		if(isFromSetting){
			//如果是从SettingActivity进入引导页面，则点击“直接进入”按钮，就把HashMap.put(isFirstRun,false)，表示登录过了
			startActivity(SettingActivity.class);
		}else{
			startActivity(LogoActivity.class);
		}
		//停止播放
		Intent musicIntent=new Intent(this, MusicServices.class);
		stopService(musicIntent);//销毁服务
		finish();//销毁窗口
	}
	private void savePreferences() {
		//在data/data目录下创建一个lead_config.xml文件用于保存数据
		SharedPreferences preferences=getSharedPreferences("Lead_config", Context.MODE_PRIVATE);
		Editor editor=preferences.edit();//获得编辑器对象
		//如果点了“直接跳过”按钮，就把isFirstRun=false，表示登录过了
		editor.putBoolean("isFirstRun", false);
		editor.commit();//提交数据
	}
	void initLeadIcon() {
		//将三个ImageView类型的icon圆点放入icons数组中，进行初始化
		icons[0]=(ImageView) findViewById(R.id.icon1);
		icons[1]=(ImageView) findViewById(R.id.icon2);
		icons[2]=(ImageView) findViewById(R.id.icon3);
		icons[0].setImageResource(R.drawable.adware_style_selected);
		//对直接跳过文本进行初始化
		tv_skip=(TextView) findViewById(R.id.tv_skip);
		tv_skip.setVisibility(View.INVISIBLE);//让直接跳过文本不显示
		tv_skip.setOnClickListener(this);//设置直接跳过按钮监听
	}
	@Override
	public void onBackPressed() {//监听回退按钮
		Intent musicIntent=new Intent(this, MusicServices.class);
		stopService(musicIntent);//销毁服务
		finish();
	}
	
}
