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
/*文件列表显示界面（从FilemgrActivity进入，根据传过来的ID显示不同数据）
 */
public class FilemgrsShowActivity extends BaseActivity {
	private int id;//确定当前页面内容
	private TextView textViewSize;//文件大小
	private TextView textViewNumber;//文件数量
	private Button btn_delall;//删除所选文件
	private ListView fileListView;//文件列表
	private FileAdapter fileAdapter;//文件列表适配器
	private ArrayList<FileInfo> fileInfos;
	private long fileSize=0;//占用空间的大小
	private long fileNumber=0;//文件数量
	private String title="全部文件";//点击后，换标题，例如：音频，视频等
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemgrs_show);
		//获取ID数据来显示（全部？音频？视频等）
		id=getIntent().getIntExtra("id", -1);
		//初始化所需数据
		initMainData(id);
		//初始化ActionBar
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		//初始化当前页面主要控件
		initMainUI();
		//初始化当前页面主要控件上的数据
		initMainUIData();
	}
	private void initMainUIData() {
		//将文件大小数据放到控件上
		textViewSize.setText(CommonUtil.getFileSize(fileSize));
		textViewNumber.setText(fileNumber+"个");
		fileAdapter=new FileAdapter(this);
		fileListView.setAdapter(fileAdapter);
		fileListView.setOnItemClickListener(itemClickListener);
		//初始化设置数据
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
		//初始化
		fileInfos=new ArrayList<FileInfo>();
		switch (viewID) {
		case R.id.file_classlist_all:
			//获取文件大小
			fileSize=FileManager.getFileManager().getAnyFileSize();
			//获取文件列表
			fileInfos=FileManager.getFileManager().getAnyFileList();
			//获取标题名称
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
	//删除选中的文件
	protected void delFile() {
		//用来保存所有选中的删除文件
		List<FileInfo> delFileInfos=new ArrayList<FileInfo>();
		//遍历所有文件的集合,找到勾选的文件
		for(int num=0;num<fileAdapter.getDataList().size();num++){
			FileInfo fileInfo=fileAdapter.getDataList().get(num);
	//		FileInfo fileInfo=fileInfos.get(num);//获取每一个元素
			//判断文件是否已选中
			if(fileInfo.isSelect()){
				//如果勾选，则加入集合
				delFileInfos.add(fileInfo);
			}		
		}
		//删除所选中的文件
		for(int num=0;num<delFileInfos.size();num++){
			FileInfo fileInfo=delFileInfos.get(num);
			File file=fileInfo.getFile();//将fileInfo类型转换成file类型
			long size=file.length();//获取文件大小
			if(file.delete()){//删除File类型文件，为true表示删除成功
				fileAdapter.getDataList().remove(fileInfo);//从适配器集合中删除此元素
				FileManager.getFileManager().getAnyFileList().remove(fileInfo);//在全部这个item里删除此对象
				//在全部这个item里减少此文件的大小
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
		//更新标题上的数据
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
		
		//更新列表
		fileAdapter.notifyDataSetChanged();
		//获取文件数量
		fileNumber=fileAdapter.getDataList().size();
		//显示
		textViewNumber.setText(fileNumber+"个");		
		//手动使垃圾回收器回收
		System.gc();
		//放弃当前线程执行权
		Thread.yield();
	}
	OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//拿到item对象
			FileInfo fileInfo=fileAdapter.getItem(position);
			//获取item上对应的文件
			File file=fileInfo.getFile();
			//获取file文件所对应的MIME，例如video/mp4
			String type=FileTypeUtil.getMIMEType(file);
			//android系统底层，调用相应的应用程序（MIME），打开文件
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			//将文件类型转换成Uri，type是我们获得的MIME，例如：video、MP4
			intent.setDataAndType(Uri.fromFile(file), type);
			startActivity(intent);
		}
	};
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

}
