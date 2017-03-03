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
	//����ImageView���͵�IconԲ��
	ImageView[] icons=new ImageView[3]; 
	TextView tv_skip;
	ViewPager viewPager;
	BasePagerAdapter leadPagerAdapter;
	//�Ƿ���������ҳ��
	boolean isFromSetting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		//�ж��Ǵ��ĸ�λ�ý�������ҳ��
		String fromClassName=intent.getStringExtra("className");
		//�ж��Ƿ��Ǵ�����ҳ���������ҳ��
		if(fromClassName!=null&&fromClassName.equals("SettingActivity")){
			isFromSetting=true;
		}
		//���ڻ�ȡ�û���һ�˵�¼����Ϣ
		SharedPreferences preferences=getSharedPreferences("Lead_config", Context.MODE_PRIVATE);
		//true��ʾȡ�������ݣ�Ĭ�Ϸ���Ϊtrue����ʾΪ��һ�ε�¼��
		boolean isFirstRun=preferences.getBoolean("isFirstRun",true);
		if(!isFirstRun){
			//���û����ǵ�һ�ε�¼��ֱ�ӽ���LogoActivity
			startActivity(LogoActivity.class);
			finish();
		}else{
			setContentView(R.layout.activity_lead);
			//��ʼ������ͼƬ
			initLeadIcon();
			//����������
			initViewPager();
			//�����ݣ���ȡviewpager��������ͼ���ٷ�����ͼ������
			initPagerData();
			//����ҳ�沥�����֣�����	����
			Intent musicIntent=new Intent(this, MusicServices.class);
			startService(musicIntent);
		}
	}
	private void initPagerData() {
		ImageView imageView=null;
		//getLayoutInflater() ��ò��ּ�����
		//���ֲ��ּ�������ʽ������
		//View.inflate(context, resource, root);
		//getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//ImageView imageView=new ImageView(this);
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		//ͨ�����ּ������������ǵ���ͼ����������ͼ����
		imageView.setImageResource(R.drawable.adware_style_applist);
		//��ͼƬ����imageview��ͼ��
		leadPagerAdapter.addViewToAdapter(imageView);
		//���˺���ͼƬ����ͼ������viewList��ͼ������
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_banner);
		leadPagerAdapter.addViewToAdapter(imageView);
		imageView=(ImageView) getLayoutInflater().inflate(R.layout.activity_lead_item, null);
		imageView.setImageResource(R.drawable.adware_style_creditswall);
		leadPagerAdapter.addViewToAdapter(imageView);
		//������Դ�仯����
		//��viewpager�����ݸı�ʱ�����ô˷��������¼���viewpager
		leadPagerAdapter.notifyDataSetChanged();
	}
	private void initViewPager() {
		//����viewPager�ؼ�������ViewPager����
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		leadPagerAdapter=new BasePagerAdapter(this);
		//����������
		viewPager.setAdapter(leadPagerAdapter);
		//��viewpager���û����ı����
		viewPager.setOnPageChangeListener(pageChangeListener);		
	}
	OnPageChangeListener pageChangeListener=new OnPageChangeListener() {
		//ҳ�滬��ʱ����icon��ɫ
		//arg0����position
		@Override
		public void onPageSelected(int arg0) {
			//��tv_skip����ʾ
			tv_skip.setVisibility(View.INVISIBLE);
			if(arg0>=icons.length-1){
				tv_skip.setVisibility(View.VISIBLE);
			}
			//����iconͼ����ɫ
			for(int num=0;num<icons.length;num++){
				//������Ĭ��ͼ��Ϊ��ɫ
				icons[num].setImageResource(R.drawable.adware_style_default);				
			}
			//�����ڼ���ҳ�棬�Ͱѵڼ���iconͼ������Ϊ��ɫ
			icons[arg0].setImageResource(R.drawable.adware_style_selected);
		}		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	//�Կؼ����г�ʼ��(��װ�ؼ�)
	@Override
	protected void initView() {	
	}
	@Override
	public void onClick(View v) {
		//�����û���һ�ε�¼��Ϣ
		savePreferences();
		if(isFromSetting){
			//����Ǵ�SettingActivity��������ҳ�棬������ֱ�ӽ��롱��ť���Ͱ�HashMap.put(isFirstRun,false)����ʾ��¼����
			startActivity(SettingActivity.class);
		}else{
			startActivity(LogoActivity.class);
		}
		//ֹͣ����
		Intent musicIntent=new Intent(this, MusicServices.class);
		stopService(musicIntent);//���ٷ���
		finish();//���ٴ���
	}
	private void savePreferences() {
		//��data/dataĿ¼�´���һ��lead_config.xml�ļ����ڱ�������
		SharedPreferences preferences=getSharedPreferences("Lead_config", Context.MODE_PRIVATE);
		Editor editor=preferences.edit();//��ñ༭������
		//������ˡ�ֱ����������ť���Ͱ�isFirstRun=false����ʾ��¼����
		editor.putBoolean("isFirstRun", false);
		editor.commit();//�ύ����
	}
	void initLeadIcon() {
		//������ImageView���͵�iconԲ�����icons�����У����г�ʼ��
		icons[0]=(ImageView) findViewById(R.id.icon1);
		icons[1]=(ImageView) findViewById(R.id.icon2);
		icons[2]=(ImageView) findViewById(R.id.icon3);
		icons[0].setImageResource(R.drawable.adware_style_selected);
		//��ֱ�������ı����г�ʼ��
		tv_skip=(TextView) findViewById(R.id.tv_skip);
		tv_skip.setVisibility(View.INVISIBLE);//��ֱ�������ı�����ʾ
		tv_skip.setOnClickListener(this);//����ֱ��������ť����
	}
	@Override
	public void onBackPressed() {//�������˰�ť
		Intent musicIntent=new Intent(this, MusicServices.class);
		stopService(musicIntent);//���ٷ���
		finish();
	}
	
}
