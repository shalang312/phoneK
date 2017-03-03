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
		if(Build.VERSION.SDK_INT>=12){//如果SDK版本大于等于12才操作
			lruCache=new LruCache<String, Bitmap>(1*1024*1024){
				@SuppressLint("NewApi")
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount();					
				}
			};
		}else{
			//老式手机才会这么低版本
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
		//监听CheckBox(选择和数据都变化)
		//获取CheckBox位置
		cb_filedel.setTag(position);
		cb_filedel.setOnCheckedChangeListener(changeListener);
		//获取数据
		FileInfo fileInfo=getItem(position);//获取item对象，对象成员变量里存储了数据
		String fileName=fileInfo.getFile().getName();
		String fileSize=CommonUtil.getFileSize(fileInfo.getFile().length());//获取文件大小
		//lastModified() 文件里的方法，用于获取最后一次修改时间
		String fileTime=CommonUtil.getFileSize(fileInfo.getFile().lastModified());//获取文件最后一次修改时间
		boolean isSelect=fileInfo.isSelect();//CheckBox是否勾选
		//Bitmap类型图片可以进行加工操作(压缩，剪裁，旋转...)
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
		//获取图片文件路径
		String filePath=fileInfo.getFile().getPath();
		// 从缓存区先取（key是文件名）,取到直接返回此图 (用图像路径作为key)
		//先取，再存
		bitmap=lruCache.get(filePath);
		if(bitmap!=null){
			return bitmap;//缓存区有图像的话直接返回图像
		}
		//要是缓存区里没有的话，再从drawable里获取
		
		// 如果是图像，根据图像路径加载图像(即将图像放到缓存区)
		if(fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)){
			bitmap=BitmapUtil.loadBitmap(filePath, new BitmapUtil.SizeMessage(context, true, 40, 40));
			if(bitmap!=null){
				lruCache.put(filePath, bitmap);//将压缩过的图片放入缓存区中，图像路径为key
				return bitmap;
			}
		}
		
		//用Res内的指定图像资源作为图标图像
		Resources res=context.getResources();				
		//String iconName=iconAndTypeNames[0];//图标名称
		//FileInfo fileInfo=new FileInfo(file, iconName, typeName);		
		//根据我们获取的图标名称获取drawable下对应名称的图标
		//获取R.drawable下的图片，icon是一个16进制数，通过这个16进制数可以找到dtawable目录下的图片
		//  图标名称   获取drawable目录下资源     context.getPackageName() 包名(edu.feicui.app.phone.avticvity)
		int icon=res.getIdentifier(fileInfo.getIconName(), "drawable", context.getPackageName());
		if(icon<=0){//找不到icon对应的16进制数，则返回0
			icon=R.drawable.icon_file;//默认放置一张图片			
		}
		//把R.drawable下的原始图片资源变成可修改的Bitmap类型
		bitmap=BitmapFactory.decodeResource(context.getResources(),icon);
		return bitmap;
		
	}
	
	OnCheckedChangeListener changeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//buttonView代表CheckBox
			int position=(Integer) buttonView.getTag();
			getItem(position).setSelect(isChecked);
		}
	};
}
