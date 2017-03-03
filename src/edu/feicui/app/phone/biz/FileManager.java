package edu.feicui.app.phone.biz;
import java.io.File;
import java.util.ArrayList;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.entity.FileInfo;
public class FileManager {
	//内置存储卡目录（可能为null）
	public static File inSdcardDir=null;
	//外置存储卡目录（可能为null）
	public static File outSdcardDir=null;//storage/sdcard1

	//static代码块-----------------------------------------------------
	static{
		//如果有手机内置sdcard路径，进行内置File实例化（）
		if(MemoryManager.getPhoneInSDCardPath()!=null){//如果可以获得内置sd卡
			inSdcardDir=null;
			inSdcardDir=new File(MemoryManager.getPhoneInSDCardPath());//获得内置sdcard
		}
		if(MemoryManager.getPhoneOutSDCardPath()!=null){
			outSdcardDir=null;
			outSdcardDir=new File(MemoryManager.getPhoneOutSDCardPath());//获得外置sdcard
		}
	}

	//单例（单态）模式---------------------------------------------------
	//private 修饰构造方法，不能在其他类中直接new新建，所以只能通过调用单例方法来创建对象
	private FileManager() {
		// TODO Auto-generated constructor stub
	}
	private static FileManager fileManager=new FileManager();
	public static FileManager getFileManager(){
		return fileManager;		
	}

	//创建各个数据集合以便缓存数据	
	// 文件搜索----------------------------------------------------------
	//任意文件（非目录）
	private ArrayList<FileInfo> anyFileList=new ArrayList<FileInfo>();
	//文档文件
	private ArrayList<FileInfo> txtFileList=new ArrayList<FileInfo>();	
	//视频文件
	private ArrayList<FileInfo> videoFileList=new ArrayList<FileInfo>();
	//音频文件
	private ArrayList<FileInfo> audioFileList=new ArrayList<FileInfo>();
	//图像
	private ArrayList<FileInfo> imageFileList=new ArrayList<FileInfo>();
	//压缩包
	private ArrayList<FileInfo> zipFileList=new ArrayList<FileInfo>();
	//apk文件
	private ArrayList<FileInfo> apkFileList=new ArrayList<FileInfo>();
	// 任意文件总大小(在搜索过程中，实时迭加，计算总大小)
	private long anyFileSize; 
	// 文档文件总大小(同上)
	private long txtFileSize;
	// 视频文件总大小(同上)
	private long videoFileSize;
	// 音乐文件总大小(同上)
	private long audioFileSize;
	// 图像文件总大小(同上)
	private long imageFileSize;
	// ZIP文件总大小(同上)
	private long zipFileSize;
	// APK文件总大小(同上)
	private long apkFileSize;
	
	//终止搜索的控制-------------------------------------------------------
	//是否停止搜索
	private boolean isStopSearch=false;
	public boolean isStopSearch(){
		return isStopSearch;		
	}
	//通过传值(boolean isStopSearch)设置是否终止
	public void setStopSearch(boolean isStopSearch){
		this.isStopSearch=isStopSearch;
	}
	
	//初始化相关变量(在每次重新开始搜索前,如searchSDCardFile())---------------
	private void initData(){
		isStopSearch=false;//是否停止搜索
		anyFileSize=0;
		txtFileSize = 0;
		videoFileSize = 0;
		audioFileSize = 0;
		imageFileSize = 0;
		zipFileSize = 0;
		apkFileSize = 0;		
		anyFileList.clear();//清空集合中所有元素
		txtFileList.clear();
		videoFileList.clear();
		audioFileList.clear();
		imageFileList.clear();
		zipFileList.clear();
		apkFileList.clear();
	}
	// 搜索存储卡(内置和外置)目录下的所有文件,结果实时保存在anyFileList内
	public void searchCardFile(){
		if(anyFileList==null||anyFileList.size()<=0){
			initData();//初始化数据
			searchFile(inSdcardDir,false);//表示内置SDcard中所有内容都搜索完毕
			/*true表示第一次进入searchFile方法时，isFirstRunFlag是true，到184行递归
			 * 再次调用searchFile方法时，isFirstRunFlag设置为false，因而可以一直搜索，
			 * 搜索完毕一层一层退出时，即for循环结束，运行188行方法，结束搜索
			 */
			searchFile(outSdcardDir,true);
		}else{
			callbackSearchFileListenerEnd(false);//当数据不为空时直接加载
		}
	}
	
//---------------------------------------------------------------------------	
	/*file 内置或者外置sdcard
	 * isFirstRunFlag  判断是否检索每一个目录
	 */
	private void searchFile(File file, boolean isFirstRunFlag) {
		//-----终止搜索---
		if(isStopSearch){
			//是首次运行就结束（搜索结束）
			if(isFirstRunFlag){
				// 回调接口end()方法,搜索结束,progressBar消失，箭头显示，数据显示
				callbackSearchFileListenerEnd(true);
			}
			return;
		}
		//#1 排除“不正常”文件（空或者不可读或者不存在）
		if(file==null||!file.canRead()||!file.exists()){
			//引用  回调函数
			if(isFirstRunFlag){
				callbackSearchFileListenerEnd(true);
			}
			return;
		}
		//#2  如果是文件（非目录）
		if(!file.isDirectory()){
			//判断文件大小
			if(file.length()<=0){
				return;
			}
			//如果文件名称中没有"."（即后缀名 ）未知文件类型
			if(file.getName().lastIndexOf(".")==-1){
				return;
			}
			//获取图标名称及文件
			String[] iconAndTypeNames=FileTypeUtil.getFileIconAndTypeName(file);
			final String iconName=iconAndTypeNames[0];//图标名称
			final String typeName=iconAndTypeNames[1];//文件类型	
			
			//剩下正常文件，添加到所有文件的集合
			FileInfo fileInfo=new FileInfo(file,typeName, iconName);
			//将获得的file，放到fileInfo对象中
			anyFileList.add(fileInfo);
			//获取文件的大小，迭加计算总文件大小
			anyFileSize +=file.length();
			//分类
			if (typeName.equals(FileTypeUtil.TYPE_APK)) {//此文件后缀名是spk
				apkFileSize += file.length();//文件大小增加
				apkFileList.add(fileInfo);//将文件放到apk集合中
			} else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
				audioFileSize += file.length();
				audioFileList.add(fileInfo);
			} else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
				imageFileSize += file.length();
				imageFileList.add(fileInfo);
			} else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
				txtFileSize += file.length();
				txtFileList.add(fileInfo);
			} else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
				videoFileSize += file.length();
				videoFileList.add(fileInfo);
			} else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
				zipFileSize += file.length();
				zipFileList.add(fileInfo);
			}
			
			//回调接口searching方法(实时更新数据)，只更新数据，progressBar不消失
			callbackSearchFileListenerSearching(typeName);
			return;
		}
		//#3 如果是目录
		File[] files=file.listFiles();//拿到此目录下所有内容
		//如果此目录里没有任何元素则直接return
		if(files==null||files.length<=0){
			return;
		}
		for(int num=0;num<files.length;num++){
			//遍历此目录下所有内容（文件和目录	）
			File tmpFile=files[num];
			searchFile(tmpFile, false);//递归，再次调用searchFile方法判断tmpFile是文件还是目录
		}
		//一层一层搜索完毕之后退出，调用callbackSearchFileListenerEnd方法
		if(isFirstRunFlag){
			callbackSearchFileListenerEnd(false);
		}
	}
	
//回调方法----------------------------------------------------------------
	//搜索过程监听
//接口（例如OnClickListener）	
	public interface SearchFileListener{
		void searching(String typeName);//正在搜索
		//调用此方法时，实际是调用FilemgrActivity中的end方法，但end中没有用到Boolean值，
		//所以没影响,可以直接写成void end()
		void end(boolean isExceptionEnd);//搜索结束
	}
	//设置文件查找监听
	//例如Button文件类
	private SearchFileListener listener;//搜索过程监听
	public void setSearchListener(SearchFileListener listener){
		this.listener=listener;
	}
	//用来回调SearchFileListener接口内方法searching(String typeName)
	//数据正在加载(相当于go方法)
	private void callbackSearchFileListenerSearching(String typeName) {//相当于doClick()方法
		if (listener != null) {
			listener.searching(typeName);//相当于onClick()方法
		}
	}
	//用来回调SearchFileListener接口内方法end(boolean isExceptionEnd)
	//数据加载结束，或数据正在加载时临时退出(相当于go方法)
	private void callbackSearchFileListenerEnd(boolean isExceptionEnd) {
		if (listener != null) {
			listener.end(isExceptionEnd);
		}
	}
	
//setters和getters--------------------------------------------------------	
	public ArrayList<FileInfo> getAnyFileList() {
		return anyFileList;
	}

	//获取文档文件列表(搜索过程中searchSDCardFile()实时在变化)
	public ArrayList<FileInfo> getTextFileList() {
		return txtFileList;
	}

	//获取视频文件文件列表(搜索过程中searchSDCardFile()实时在变化) 
	public ArrayList<FileInfo> getVideoFileList() {
		return videoFileList;
	}

	// 获取音乐文件文件列表(搜索过程中searchSDCardFile()实时在变化)
	public ArrayList<FileInfo> getAudioFileList() {
		return audioFileList;
	}

	// 获取图像文件文件列表(搜索过程中searchSDCardFile()实时在变化) 
	public ArrayList<FileInfo> getImageFileList() {
		return imageFileList;
	}

	//获取APK文件列表(搜索过程中searchSDCardFile()实时在变化)
	public ArrayList<FileInfo> getApkFileList() {
		return apkFileList;
	}

	// 获取zip文件列表(搜索过程中searchSDCardFile()实时在变化)
	public ArrayList<FileInfo> getZipFileList() {
		return zipFileList;
	}

	// 获取APK文件当前总大小(搜索过程中searchSDCardFile()实时在变化) 
	public long getApkFileSize() {
		return apkFileSize;
	}

	// 获取zip文件当前总大小(搜索过程中searchSDCardFile()实时在变化) 
	public long getZipFileSize() {
		return zipFileSize;
	}

	// 获取任意所有文件当前总大小(搜索过程中searchSDCardFile()实时在变化)
	public long getAnyFileSize() {
		return anyFileSize;
	}

	//获取文本文件当前总大小(搜索过程中searchSDCardFile()实时在变化)
	public long getTxtFileSize() {
		return txtFileSize;
	}

	//获取视频文件当前总大小(搜索过程中searchSDCardFile()实时在变化) 
	public long getVideoFileSize() {
		return videoFileSize;
	}

	//获取音频文件当前总大小(搜索过程中searchSDCardFile()实时在变化) 
	public long getAudioFileSize() {
		return audioFileSize;
	}

	//获取图像文件当前总大小(搜索过程中searchSDCardFile()实时在变化)
	public long getImageFileSize() {
		return imageFileSize;
	}

	public void setAnyFileSize(long anyFileSize) {
		this.anyFileSize = anyFileSize;
		if (this.anyFileSize < 0) {
			this.anyFileSize = 0;
		}
	}

	public void setTextFileSize(long txtFileSize) {
		this.txtFileSize = txtFileSize;
		if (this.txtFileSize < 0) {
			this.txtFileSize = 0;
		}
	}

	public void setVideoFileSize(long videoFileSize) {
		this.videoFileSize = videoFileSize;
		if (this.videoFileSize < 0) {
			this.videoFileSize = 0;
		}
	}

	public void setAudioFileSize(long audioFileSize) {
		this.audioFileSize = audioFileSize;
		if (this.audioFileSize < 0) {
			this.audioFileSize = 0;
		}
	}

	public void setImageFileSize(long imageFileSize) {
		this.imageFileSize = imageFileSize;
		if (this.imageFileSize < 0) {
			this.imageFileSize = 0;
		}
	}

	public void setZipFileSize(long zipFileSize) {
		this.zipFileSize = zipFileSize;
		if (this.zipFileSize < 0) {
			this.zipFileSize = 0;
		}
	}

	public void setApkFileSize(long apkFileSize) {
		this.apkFileSize = apkFileSize;
		if (this.apkFileSize < 0) {
			this.apkFileSize = 0;
		}
	}
}
