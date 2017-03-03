package edu.feicui.app.phone.activity;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.biz.FileManager;
import edu.feicui.app.phone.biz.FileManager.SearchFileListener;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class FilemgrActivity extends BaseActivity {
	private Thread thread;//读取文件数据的工作线程
	private TextView tv_textsize;//蓝色布局那块
	private View view_all;//全部
	private View view_txt; // 文档
	private View view_video; // 视频
	private View view_audio; // 音频
	private View view_image; // 图像
	private View view_apk; // apk
	private View view_zip; // zip
	private TextView tv_all_size;//全部item的大小(当加载过程中，实时更新此分类的文件大小)
	private TextView tv_txt_size;//文档item的大小
	private TextView tv_video_size;//视频item的大小
	private TextView tv_audio_size;//音频item的大小
	private TextView tv_image_size;//图像item的大小
	private TextView tv_apk_size;//apk的item的大小
	private TextView tv_zip_size;//Zip的item的大小
	private ProgressBar pb_all_loading;///全部item的加载中图像(当加载过程中会显示,加载完后将隐藏)
	private ProgressBar pb_txt_loading;//文档item的加载中图像
	private ProgressBar pb_video_loading;// 视频item的加载中图像
	private ProgressBar pb_audio_loading;// 音乐item的加载中图像
	private ProgressBar pb_image_loading;// 图像item的加载中图像
	private ProgressBar pb_apk_loading;// apk item的加载中图像
	private ProgressBar pb_zip_loading;// zip item的加载中图像
	private ImageView iv_all_righticon;//全部item的右侧图像(当加载结束后，将显示出来(加载中显示的是loading))
	private ImageView iv_txt_righticon;//文档item的右侧图像
	private ImageView iv_video_righticon; // 视频item的右侧图像
	private ImageView iv_audio_righticon; // 音乐item的右侧图像
	private ImageView iv_image_righticon; // 图像item的右侧图像
	private ImageView iv_apk_righticon; // apk item的右侧图像
	private ImageView iv_zip_righticon; // zip item的右侧图像	
	private FileManager fileManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgr);
		//初始化ActionBar
		String title=getResources().getString(R.string.filemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//初始化控件
		initMainUI();
		//加载数据
		asynload();
	}
	private void asynload() {
		// TODO Auto-generated method stub
		fileManager=FileManager.getFileManager();//获取fileManager
		fileManager.setSearchListener(searchFileListener);//在主线程设置监听
		//启动工作线程进行文件搜索
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
//相当于将需要在工作线程获得的数据直接封装成方法
				fileManager.searchCardFile();//将数据存储到文件集合和文件大小里
								
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						//测试将所有文件大小显示
//						tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
//						tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
//						tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
//						tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
//						tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
//						tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
//						tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
//					}
//				});				
			}
		});
		thread.start();
	}
	private void initMainUI() {
		tv_textsize=(TextView) findViewById(R.id.tv_filesize);
		//布局
		view_all=findViewById(R.id.file_classlist_all);
		view_txt=findViewById(R.id.file_classlist_txt);
		view_video=findViewById(R.id.file_classlist_video);
		view_audio=findViewById(R.id.file_classlist_audio);
		view_image=findViewById(R.id.file_classlist_image);
		view_zip=findViewById(R.id.file_classlist_zip);
		view_apk=findViewById(R.id.file_classlist_apk);
		//textView
		tv_all_size=(TextView) findViewById(R.id.file_all_size);
		tv_txt_size=(TextView) findViewById(R.id.file_txt_size);
		tv_video_size=(TextView) findViewById(R.id.file_video_size);
		tv_audio_size=(TextView) findViewById(R.id.file_audio_size);
		tv_image_size=(TextView) findViewById(R.id.file_image_size);
		tv_zip_size=(TextView) findViewById(R.id.file_zip_size);
		tv_apk_size=(TextView) findViewById(R.id.file_apk_size);
		//图片
		iv_all_righticon=(ImageView) findViewById(R.id.file_all_righticon);
		iv_txt_righticon=(ImageView) findViewById(R.id.file_text_righticon);
		iv_video_righticon=(ImageView) findViewById(R.id.file_video_righticon);
		iv_audio_righticon=(ImageView) findViewById(R.id.file_audio_righticon);
		iv_image_righticon=(ImageView) findViewById(R.id.file_image_righticon);
		iv_zip_righticon=(ImageView) findViewById(R.id.file_zip_righticon);
		iv_apk_righticon=(ImageView) findViewById(R.id.file_apk_righticon);
		//进度条
		pb_all_loading=(ProgressBar) findViewById(R.id.file_all_loading);
		pb_txt_loading=(ProgressBar) findViewById(R.id.file_text_loading);
		pb_video_loading=(ProgressBar) findViewById(R.id.file_video_loading);
		pb_audio_loading=(ProgressBar) findViewById(R.id.file_audio_loading);
		pb_image_loading=(ProgressBar) findViewById(R.id.file_image_loading);
		pb_zip_loading=(ProgressBar) findViewById(R.id.file_zip_loading);
		pb_apk_loading=(ProgressBar) findViewById(R.id.file_apk_loading);
	}
	//回调接口初始化
	private SearchFileListener searchFileListener=new SearchFileListener() {
		//重写SearchFileListener接口里的方法
		//searching方法表示一直在搜索，即数据增加，progressBar在旋转
		@Override
		public void searching(String typeName) {
			//实时通知UI界面更新数据
			Message msg=myHandler.obtainMessage();//myHandler在BaseActivity中，可以用myHandler.obtainMessage()来创建对象
			msg.what=1;
			msg.obj=typeName;//文件类型名称
			myHandler.sendMessage(msg);
		}
		//end表示搜索结束，数据搜索完毕，progressBar消失
		@Override
		public void end(boolean isExceptionEnd) {
			//通知主线程数据加载完毕，ProgressBar消失
			//次what=2
			myHandler.sendEmptyMessage(2);//不携带数据的空消息
		}
	};
	//重写BaseActivity里的myHandlerMessage方法
	//实时更新各类文件大小,主线程更新
	protected void myHandlerMessage(Message msg) {
		//文件实时更新操作,还在操作过程中
		if(msg.what==1){//实时操作，ProgressBar不消失
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
			
//			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
//			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
//			String typeName=(String) msg.obj;//返回文件类型名称
//			if(FileTypeUtil.TYPE_APK.equals(typeName)){//如果文件名称是APK就放到APK里
//				tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
//			}else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
//				tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
//			} else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
//				tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
//			} else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
//				tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
//			} else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
//				tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
//			} else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
//				tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
//			}
		}
		//文件加载完毕之后
		if(msg.what==2){
			//加载完毕后直接设置数据，没有加载的过程
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
			//ProgressBar消失
			pb_all_loading.setVisibility(View.GONE);
			pb_txt_loading.setVisibility(View.GONE);
			pb_video_loading.setVisibility(View.GONE);
			pb_audio_loading.setVisibility(View.GONE);
			pb_image_loading.setVisibility(View.GONE);
			pb_apk_loading.setVisibility(View.GONE);
			pb_zip_loading.setVisibility(View.GONE);
			//箭头显示
			iv_all_righticon.setVisibility(View.VISIBLE);
			iv_txt_righticon.setVisibility(View.VISIBLE);
			iv_video_righticon.setVisibility(View.VISIBLE);
			iv_audio_righticon.setVisibility(View.VISIBLE);
			iv_image_righticon.setVisibility(View.VISIBLE);
			iv_apk_righticon.setVisibility(View.VISIBLE);
			iv_zip_righticon.setVisibility(View.VISIBLE);
			//每个item设置跳转监听
			view_all.setOnClickListener(clickListener);
			view_txt.setOnClickListener(clickListener);
			view_video.setOnClickListener(clickListener);
			view_audio.setOnClickListener(clickListener);
			view_image.setOnClickListener(clickListener);
			view_apk.setOnClickListener(clickListener);
			view_zip.setOnClickListener(clickListener);
		}
	};
	OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_left:
				startActivity(HomeActivity.class);
				finish();
				break;
			case R.id.file_classlist_all:
			case R.id.file_classlist_txt:
			case R.id.file_classlist_video:
			case R.id.file_classlist_audio:
			case R.id.file_classlist_image:
			case R.id.file_classlist_apk:
			case R.id.file_classlist_zip:
				Intent intent = new Intent(FilemgrActivity.this, FilemgrsShowActivity.class);
				intent.putExtra("id", v.getId());//将每个item的ID传过去，再根据ID不同显示不同item数据
				startActivityForResult(intent, 1);
				break;	
			default:
				break;
			}
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//销毁后搜索结束
		fileManager.setStopSearch(true);
		//中断线程，释放资源
		thread.interrupt();
		thread=null;
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			new Thread(new Runnable() {				
				@Override
				public void run() {
					//在工作线程获取数据存储到对应的集合中
					fileManager.searchCardFile();
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//在主线程的控件上显示数据
							tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
							tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
							tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
							tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
							tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
							tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
							tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
							tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
						}
					});					
				}
			}).start();			
		}
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
}
