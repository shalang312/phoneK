package edu.feicui.app.phone.adapter;
import java.io.ObjectInputStream.GetField;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.base.util.BitmapUtil;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.entity.FileInfo;
public class FileAdapter extends BaseBaseAdapter<FileInfo>{
	private LayoutInflater layoutInflater;
	private LruCache<String,Bitmap> lruCache=null;
	public FileAdapter(Context context) {
		super(context);
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(Build.VERSION.SDK_INT>=12){//���SDK�汾���ڵ���12�Ų���
			lruCache=new LruCache<String, Bitmap>(1*1024*1024){
				@SuppressLint("NewApi")
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount();					
				}
			};
		}else{
			//��ʽ�ֻ��Ż���ô�Ͱ汾
			lruCache=new LruCache<String, Bitmap>(100);
		}
	}
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.layout_filemgl_listitem, null);
		}
		CheckBox cb_filedel=(CheckBox) convertView.findViewById(R.id.cb_filedel);
		ImageView iv_fileicon=(ImageView) convertView.findViewById(R.id.iv_fileicon);
		TextView tv_filename=(TextView) convertView.findViewById(R.id.tv_filename);
		TextView tv_filesize=(TextView) convertView.findViewById(R.id.tv_filesize);
		TextView tv_filetime=(TextView) convertView.findViewById(R.id.tv_filetime);
		//����CheckBox(ѡ������ݶ��仯)
		//��ȡCheckBoxλ��
		cb_filedel.setTag(position);
		cb_filedel.setOnCheckedChangeListener(changeListener);
		//��ȡ����
		FileInfo fileInfo=getItem(position);//��ȡitem���󣬶����Ա������洢������
		String fileName=fileInfo.getFile().getName();
		String fileSize=CommonUtil.getFileSize(fileInfo.getFile().length());//��ȡ�ļ���С
		//lastModified() �ļ���ķ��������ڻ�ȡ���һ���޸�ʱ��
		String fileTime=CommonUtil.getFileSize(fileInfo.getFile().lastModified());//��ȡ�ļ����һ���޸�ʱ��
		boolean isSelect=fileInfo.isSelect();//CheckBox�Ƿ�ѡ
		//Bitmap����ͼƬ���Խ��мӹ�����(ѹ�������ã���ת...)
		Bitmap fileIcon=getFileIcon(fileInfo,iv_fileicon);		
		if(fileIcon!=null){
			iv_fileicon.setImageBitmap(fileIcon);
		}
		tv_filename.setText(fileName);
		tv_filesize.setText(fileSize);
		tv_filetime.setText(fileTime);
		cb_filedel.setChecked(isSelect);
		return convertView;
	}	
	private Bitmap getFileIcon(FileInfo fileInfo, ImageView iv_fileicon) {
		Bitmap bitmap=null;
		//��ȡͼƬ�ļ�·��
		String filePath=fileInfo.getFile().getPath();
		// �ӻ�������ȡ��key���ļ�����,ȡ��ֱ�ӷ��ش�ͼ (��ͼ��·����Ϊkey)
		//��ȡ���ٴ�
		bitmap=lruCache.get(filePath);
		if(bitmap!=null){
			return bitmap;//��������ͼ��Ļ�ֱ�ӷ���ͼ��
		}
		//Ҫ�ǻ�������û�еĻ����ٴ�drawable���ȡ
		
		// �����ͼ�񣬸���ͼ��·������ͼ��(����ͼ��ŵ�������)
		if(fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)){
			bitmap=BitmapUtil.loadBitmap(filePath, new BitmapUtil.SizeMessage(context, true, 40, 40));
			if(bitmap!=null){
				lruCache.put(filePath, bitmap);//��ѹ������ͼƬ���뻺�����У�ͼ��·��Ϊkey
				return bitmap;
			}
		}
		
		//��Res�ڵ�ָ��ͼ����Դ��Ϊͼ��ͼ��
		Resources res=context.getResources();				
		//String iconName=iconAndTypeNames[0];//ͼ������
		//FileInfo fileInfo=new FileInfo(file, iconName, typeName);		
		//�������ǻ�ȡ��ͼ�����ƻ�ȡdrawable�¶�Ӧ���Ƶ�ͼ��
		//��ȡR.drawable�µ�ͼƬ��icon��һ��16��������ͨ�����16�����������ҵ�dtawableĿ¼�µ�ͼƬ
		//  ͼ������   ��ȡdrawableĿ¼����Դ     context.getPackageName() ����(edu.feicui.app.phone.avticvity)
		int icon=res.getIdentifier(fileInfo.getIconName(), "drawable", context.getPackageName());
		if(icon<=0){//�Ҳ���icon��Ӧ��16���������򷵻�0
			icon=R.drawable.icon_file;//Ĭ�Ϸ���һ��ͼƬ			
		}
		//��R.drawable�µ�ԭʼͼƬ��Դ��ɿ��޸ĵ�Bitmap����
		bitmap=BitmapFactory.decodeResource(context.getResources(),icon);
		return bitmap;
		
	}
	
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//buttonView����CheckBox
			int position=(Integer) buttonView.getTag();
			getItem(position).setSelect(isChecked);
		}
	};
}
