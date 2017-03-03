package edu.feicui.app.phone.activity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import edu.feicui.app.phone.adapter.FileAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.biz.FileManager;
import edu.feicui.app.phone.entity.FileInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/*�ļ��б���ʾ���棨��FilemgrActivity���룬���ݴ�������ID��ʾ��ͬ���ݣ�
 */
public class FilemgrsShowActivity extends BaseActivity {
	private int id;//ȷ����ǰҳ������
	private TextView textViewSize;//�ļ���С
	private TextView textViewNumber;//�ļ�����
	private Button btn_delall;//ɾ����ѡ�ļ�
	private ListView fileListView;//�ļ��б�
	private FileAdapter fileAdapter;//�ļ��б�������
	private ArrayList<FileInfo> fileInfos;
	private long fileSize=0;//ռ�ÿռ�Ĵ�С
	private long fileNumber=0;//�ļ�����
	private String title="ȫ���ļ�";//����󣬻����⣬���磺��Ƶ����Ƶ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgrs_show);
		//��ȡID��������ʾ��ȫ������Ƶ����Ƶ�ȣ�
		id=getIntent().getIntExtra("id", -1);
		//��ʼ����������
		initMainData(id);
		//��ʼ��ActionBar
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//��ʼ����ǰҳ����Ҫ�ؼ�
		initMainUI();
		//��ʼ����ǰҳ����Ҫ�ؼ��ϵ�����
		initMainUIData();
	}
	private void initMainUIData() {
		//���ļ���С���ݷŵ��ؼ���
		textViewSize.setText(CommonUtil.getFileSize(fileSize));
		textViewNumber.setText(fileNumber+"��");
		fileAdapter=new FileAdapter(this);
		fileListView.setAdapter(fileAdapter);
		fileListView.setOnItemClickListener(itemClickListener);
		//��ʼ����������
		fileAdapter.setDataToAdapter(fileInfos);
		fileAdapter.notifyDataSetChanged();
	}
	private void initMainUI() {
		// TODO Auto-generated method stub
		textViewSize=(TextView) findViewById(R.id.tv_file_size);
		textViewNumber=(TextView) findViewById(R.id.tv_file_number);
		fileListView=(ListView) findViewById(R.id.filelistview);
		btn_delall=(Button) findViewById(R.id.btn_delall);
		btn_delall.setOnClickListener(clickListener);
	}
	
	
	private void initMainData(int viewID) {
		//��ʼ��
		fileInfos=new ArrayList<FileInfo>();
		switch (viewID) {
		case R.id.file_classlist_all:
			//��ȡ�ļ���С
			fileSize=FileManager.getFileManager().getAnyFileSize();
			//��ȡ�ļ��б�
			fileInfos=FileManager.getFileManager().getAnyFileList();
			//��ȡ��������
			title=getResources().getString(R.string.filetype_all);
			break;
		case R.id.file_classlist_txt:
			fileSize = FileManager.getFileManager().getTxtFileSize();
			fileInfos = FileManager.getFileManager().getTextFileList();
			title = getResources().getString(R.string.filetype_text);
			break;
		case R.id.file_classlist_video:
			fileSize = FileManager.getFileManager().getVideoFileSize();
			fileInfos = FileManager.getFileManager().getVideoFileList();
			title = getResources().getString(R.string.filetype_video);
			break;
		case R.id.file_classlist_audio:
			fileSize = FileManager.getFileManager().getAudioFileSize();
			fileInfos = FileManager.getFileManager().getAudioFileList();
			title = getResources().getString(R.string.filetype_audio);
			break;
		case R.id.file_classlist_image:
			fileSize = FileManager.getFileManager().getImageFileSize();
			fileInfos = FileManager.getFileManager().getImageFileList();
			title = getResources().getString(R.string.filetype_image);
			break;
		case R.id.file_classlist_apk:
			fileSize = FileManager.getFileManager().getApkFileSize();
			fileInfos = FileManager.getFileManager().getApkFileList();
			title = getResources().getString(R.string.filetype_apk);
			break;
		case R.id.file_classlist_zip:
			fileSize = FileManager.getFileManager().getZipFileSize();
			fileInfos = FileManager.getFileManager().getZipFileList();
			title = getResources().getString(R.string.filetype_zip);
			break;		
		}
		fileNumber=fileInfos.size();
	}
OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_delall:
				delFile();
				break;
			default:
				break;
			}
		}
	};
	//ɾ��ѡ�е��ļ�
	protected void delFile() {
		//������������ѡ�е�ɾ���ļ�
		List<FileInfo> delFileInfos=new ArrayList<FileInfo>();
		//���������ļ��ļ���,�ҵ���ѡ���ļ�
		for(int num=0;num<fileAdapter.getDataList().size();num++){
			FileInfo fileInfo=fileAdapter.getDataList().get(num);
	//		FileInfo fileInfo=fileInfos.get(num);//��ȡÿһ��Ԫ��
			//�ж��ļ��Ƿ���ѡ��
			if(fileInfo.isSelect()){
				//�����ѡ������뼯��
				delFileInfos.add(fileInfo);
			}		
		}
		//ɾ����ѡ�е��ļ�
		for(int num=0;num<delFileInfos.size();num++){
			FileInfo fileInfo=delFileInfos.get(num);
			File file=fileInfo.getFile();//��fileInfo����ת����file����
			long size=file.length();//��ȡ�ļ���С
			if(file.delete()){//ɾ��File�����ļ���Ϊtrue��ʾɾ���ɹ�
				fileAdapter.getDataList().remove(fileInfo);//��������������ɾ����Ԫ��
				FileManager.getFileManager().getAnyFileList().remove(fileInfo);//��ȫ�����item��ɾ���˶���
				//��ȫ�����item����ٴ��ļ��Ĵ�С
				FileManager.getFileManager().setAnyFileSize(FileManager.getFileManager().getAnyFileSize()-size);
				switch (id) {
				case R.id.file_classlist_txt:
					FileManager.getFileManager().getTextFileList().remove(fileInfo);
					FileManager.getFileManager().setTextFileSize(FileManager.getFileManager().getTxtFileSize() - size);
					break;
				case R.id.file_classlist_video:
					FileManager.getFileManager().getVideoFileList().remove(fileInfo);
					FileManager.getFileManager().setVideoFileSize(FileManager.getFileManager().getVideoFileSize() - size);
					break;
				case R.id.file_classlist_audio:
					FileManager.getFileManager().getAudioFileList().remove(fileInfo);
					FileManager.getFileManager().setAudioFileSize(FileManager.getFileManager().getAudioFileSize() - size);
					break;
				case R.id.file_classlist_image:
					FileManager.getFileManager().getImageFileList().remove(fileInfo);
					FileManager.getFileManager().setImageFileSize(FileManager.getFileManager().getImageFileSize() - size);
					break;
				case R.id.file_classlist_apk:
					FileManager.getFileManager().getApkFileList().remove(fileInfo);
					FileManager.getFileManager().setApkFileSize(FileManager.getFileManager().getApkFileSize() - size);
					break;
				case R.id.file_classlist_zip:
					FileManager.getFileManager().getZipFileList().remove(fileInfo);
					FileManager.getFileManager().setZipFileSize(FileManager.getFileManager().getZipFileSize() - size);
					break;
				}
				
			}
		}
		//���±����ϵ�����
		switch (id) {
		case R.id.file_classlist_all:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getAnyFileSize()));
			break;
		case R.id.file_classlist_apk:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getApkFileSize()));
			break;
		case R.id.file_classlist_audio:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getAudioFileSize()));
			break;
		case R.id.file_classlist_image:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getImageFileSize()));
			break;
		case R.id.file_classlist_txt:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getTxtFileSize()));
			break;
		case R.id.file_classlist_video:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getVideoFileSize()));
			break;
		case R.id.file_classlist_zip:
			textViewSize.setText(CommonUtil.getFileSize(FileManager.getFileManager().getZipFileSize()));
			break;
		}
		
		//�����б�
		fileAdapter.notifyDataSetChanged();
		//��ȡ�ļ�����
		fileNumber=fileAdapter.getDataList().size();
		//��ʾ
		textViewNumber.setText(fileNumber+"��");		
		//�ֶ�ʹ��������������
		System.gc();
		//������ǰ�߳�ִ��Ȩ
		Thread.yield();
	}
	OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//�õ�item����
			FileInfo fileInfo=fileAdapter.getItem(position);
			//��ȡitem�϶�Ӧ���ļ�
			File file=fileInfo.getFile();
			//��ȡfile�ļ�����Ӧ��MIME������video/mp4
			String type=FileTypeUtil.getMIMEType(file);
			//androidϵͳ�ײ㣬������Ӧ��Ӧ�ó���MIME�������ļ�
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			//���ļ�����ת����Uri��type�����ǻ�õ�MIME�����磺video��MP4
			intent.setDataAndType(Uri.fromFile(file), type);
			startActivity(intent);
		}
	};
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

}
