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
	private Thread thread;//��ȡ�ļ����ݵĹ����߳�
	private TextView tv_textsize;//��ɫ�����ǿ�
	private View view_all;//ȫ��
	private View view_txt; // �ĵ�
	private View view_video; // ��Ƶ
	private View view_audio; // ��Ƶ
	private View view_image; // ͼ��
	private View view_apk; // apk
	private View view_zip; // zip
	private TextView tv_all_size;//ȫ��item�Ĵ�С(�����ع����У�ʵʱ���´˷�����ļ���С)
	private TextView tv_txt_size;//�ĵ�item�Ĵ�С
	private TextView tv_video_size;//��Ƶitem�Ĵ�С
	private TextView tv_audio_size;//��Ƶitem�Ĵ�С
	private TextView tv_image_size;//ͼ��item�Ĵ�С
	private TextView tv_apk_size;//apk��item�Ĵ�С
	private TextView tv_zip_size;//Zip��item�Ĵ�С
	private ProgressBar pb_all_loading;///ȫ��item�ļ�����ͼ��(�����ع����л���ʾ,�����������)
	private ProgressBar pb_txt_loading;//�ĵ�item�ļ�����ͼ��
	private ProgressBar pb_video_loading;// ��Ƶitem�ļ�����ͼ��
	private ProgressBar pb_audio_loading;// ����item�ļ�����ͼ��
	private ProgressBar pb_image_loading;// ͼ��item�ļ�����ͼ��
	private ProgressBar pb_apk_loading;// apk item�ļ�����ͼ��
	private ProgressBar pb_zip_loading;// zip item�ļ�����ͼ��
	private ImageView iv_all_righticon;//ȫ��item���Ҳ�ͼ��(�����ؽ����󣬽���ʾ����(��������ʾ����loading))
	private ImageView iv_txt_righticon;//�ĵ�item���Ҳ�ͼ��
	private ImageView iv_video_righticon; // ��Ƶitem���Ҳ�ͼ��
	private ImageView iv_audio_righticon; // ����item���Ҳ�ͼ��
	private ImageView iv_image_righticon; // ͼ��item���Ҳ�ͼ��
	private ImageView iv_apk_righticon; // apk item���Ҳ�ͼ��
	private ImageView iv_zip_righticon; // zip item���Ҳ�ͼ��	
	private FileManager fileManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgr);
		//��ʼ��ActionBar
		String title=getResources().getString(R.string.filemgr);
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//��ʼ���ؼ�
		initMainUI();
		//��������
		asynload();
	}
	private void asynload() {
		// TODO Auto-generated method stub
		fileManager=FileManager.getFileManager();//��ȡfileManager
		fileManager.setSearchListener(searchFileListener);//�����߳����ü���
		//���������߳̽����ļ�����
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
//�൱�ڽ���Ҫ�ڹ����̻߳�õ�����ֱ�ӷ�װ�ɷ���
				fileManager.searchCardFile();//�����ݴ洢���ļ����Ϻ��ļ���С��
								
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						//���Խ������ļ���С��ʾ
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
		//����
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
		//ͼƬ
		iv_all_righticon=(ImageView) findViewById(R.id.file_all_righticon);
		iv_txt_righticon=(ImageView) findViewById(R.id.file_text_righticon);
		iv_video_righticon=(ImageView) findViewById(R.id.file_video_righticon);
		iv_audio_righticon=(ImageView) findViewById(R.id.file_audio_righticon);
		iv_image_righticon=(ImageView) findViewById(R.id.file_image_righticon);
		iv_zip_righticon=(ImageView) findViewById(R.id.file_zip_righticon);
		iv_apk_righticon=(ImageView) findViewById(R.id.file_apk_righticon);
		//������
		pb_all_loading=(ProgressBar) findViewById(R.id.file_all_loading);
		pb_txt_loading=(ProgressBar) findViewById(R.id.file_text_loading);
		pb_video_loading=(ProgressBar) findViewById(R.id.file_video_loading);
		pb_audio_loading=(ProgressBar) findViewById(R.id.file_audio_loading);
		pb_image_loading=(ProgressBar) findViewById(R.id.file_image_loading);
		pb_zip_loading=(ProgressBar) findViewById(R.id.file_zip_loading);
		pb_apk_loading=(ProgressBar) findViewById(R.id.file_apk_loading);
	}
	//�ص��ӿڳ�ʼ��
	private SearchFileListener searchFileListener=new SearchFileListener() {
		//��дSearchFileListener�ӿ���ķ���
		//searching������ʾһֱ�����������������ӣ�progressBar����ת
		@Override
		public void searching(String typeName) {
			//ʵʱ֪ͨUI�����������
			Message msg=myHandler.obtainMessage();//myHandler��BaseActivity�У�������myHandler.obtainMessage()����������
			msg.what=1;
			msg.obj=typeName;//�ļ���������
			myHandler.sendMessage(msg);
		}
		//end��ʾ��������������������ϣ�progressBar��ʧ
		@Override
		public void end(boolean isExceptionEnd) {
			//֪ͨ���߳����ݼ�����ϣ�ProgressBar��ʧ
			//��what=2
			myHandler.sendEmptyMessage(2);//��Я�����ݵĿ���Ϣ
		}
	};
	//��дBaseActivity���myHandlerMessage����
	//ʵʱ���¸����ļ���С,���̸߳���
	protected void myHandlerMessage(Message msg) {
		//�ļ�ʵʱ���²���,���ڲ���������
		if(msg.what==1){//ʵʱ������ProgressBar����ʧ
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
//			String typeName=(String) msg.obj;//�����ļ���������
//			if(FileTypeUtil.TYPE_APK.equals(typeName)){//����ļ�������APK�ͷŵ�APK��
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
		//�ļ��������֮��
		if(msg.what==2){
			//������Ϻ�ֱ���������ݣ�û�м��صĹ���
			tv_textsize.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_all_size.setText(CommonUtil.getFileSize(fileManager.getAnyFileSize()));
			tv_apk_size.setText(CommonUtil.getFileSize(fileManager.getApkFileSize()));
			tv_audio_size.setText(CommonUtil.getFileSize(fileManager.getAudioFileSize()));
			tv_image_size.setText(CommonUtil.getFileSize(fileManager.getImageFileSize()));
			tv_txt_size.setText(CommonUtil.getFileSize(fileManager.getTxtFileSize()));
			tv_video_size.setText(CommonUtil.getFileSize(fileManager.getVideoFileSize()));
			tv_zip_size.setText(CommonUtil.getFileSize(fileManager.getZipFileSize()));
			//ProgressBar��ʧ
			pb_all_loading.setVisibility(View.GONE);
			pb_txt_loading.setVisibility(View.GONE);
			pb_video_loading.setVisibility(View.GONE);
			pb_audio_loading.setVisibility(View.GONE);
			pb_image_loading.setVisibility(View.GONE);
			pb_apk_loading.setVisibility(View.GONE);
			pb_zip_loading.setVisibility(View.GONE);
			//��ͷ��ʾ
			iv_all_righticon.setVisibility(View.VISIBLE);
			iv_txt_righticon.setVisibility(View.VISIBLE);
			iv_video_righticon.setVisibility(View.VISIBLE);
			iv_audio_righticon.setVisibility(View.VISIBLE);
			iv_image_righticon.setVisibility(View.VISIBLE);
			iv_apk_righticon.setVisibility(View.VISIBLE);
			iv_zip_righticon.setVisibility(View.VISIBLE);
			//ÿ��item������ת����
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
				intent.putExtra("id", v.getId());//��ÿ��item��ID����ȥ���ٸ���ID��ͬ��ʾ��ͬitem����
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
		//���ٺ���������
		fileManager.setStopSearch(true);
		//�ж��̣߳��ͷ���Դ
		thread.interrupt();
		thread=null;
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			new Thread(new Runnable() {				
				@Override
				public void run() {
					//�ڹ����̻߳�ȡ���ݴ洢����Ӧ�ļ�����
					fileManager.searchCardFile();
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//�����̵߳Ŀؼ�����ʾ����
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
