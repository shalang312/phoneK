package edu.feicui.app.phone.biz;
import java.io.File;
import java.util.ArrayList;
import edu.feicui.app.phone.base.util.FileTypeUtil;
import edu.feicui.app.phone.entity.FileInfo;
public class FileManager {
	//���ô洢��Ŀ¼������Ϊnull��
	public static File inSdcardDir=null;
	//���ô洢��Ŀ¼������Ϊnull��
	public static File outSdcardDir=null;//storage/sdcard1

	//static�����-----------------------------------------------------
	static{
		//������ֻ�����sdcard·������������Fileʵ��������
		if(MemoryManager.getPhoneInSDCardPath()!=null){//������Ի������sd��
			inSdcardDir=null;
			inSdcardDir=new File(MemoryManager.getPhoneInSDCardPath());//�������sdcard
		}
		if(MemoryManager.getPhoneOutSDCardPath()!=null){
			outSdcardDir=null;
			outSdcardDir=new File(MemoryManager.getPhoneOutSDCardPath());//�������sdcard
		}
	}

	//��������̬��ģʽ---------------------------------------------------
	//private ���ι��췽������������������ֱ��new�½�������ֻ��ͨ�����õ�����������������
	private FileManager() {
		// TODO Auto-generated constructor stub
	}
	private static FileManager fileManager=new FileManager();
	public static FileManager getFileManager(){
		return fileManager;		
	}

	//�����������ݼ����Ա㻺������	
	// �ļ�����----------------------------------------------------------
	//�����ļ�����Ŀ¼��
	private ArrayList<FileInfo> anyFileList=new ArrayList<FileInfo>();
	//�ĵ��ļ�
	private ArrayList<FileInfo> txtFileList=new ArrayList<FileInfo>();	
	//��Ƶ�ļ�
	private ArrayList<FileInfo> videoFileList=new ArrayList<FileInfo>();
	//��Ƶ�ļ�
	private ArrayList<FileInfo> audioFileList=new ArrayList<FileInfo>();
	//ͼ��
	private ArrayList<FileInfo> imageFileList=new ArrayList<FileInfo>();
	//ѹ����
	private ArrayList<FileInfo> zipFileList=new ArrayList<FileInfo>();
	//apk�ļ�
	private ArrayList<FileInfo> apkFileList=new ArrayList<FileInfo>();
	// �����ļ��ܴ�С(�����������У�ʵʱ���ӣ������ܴ�С)
	private long anyFileSize; 
	// �ĵ��ļ��ܴ�С(ͬ��)
	private long txtFileSize;
	// ��Ƶ�ļ��ܴ�С(ͬ��)
	private long videoFileSize;
	// �����ļ��ܴ�С(ͬ��)
	private long audioFileSize;
	// ͼ���ļ��ܴ�С(ͬ��)
	private long imageFileSize;
	// ZIP�ļ��ܴ�С(ͬ��)
	private long zipFileSize;
	// APK�ļ��ܴ�С(ͬ��)
	private long apkFileSize;
	
	//��ֹ�����Ŀ���-------------------------------------------------------
	//�Ƿ�ֹͣ����
	private boolean isStopSearch=false;
	public boolean isStopSearch(){
		return isStopSearch;		
	}
	//ͨ����ֵ(boolean isStopSearch)�����Ƿ���ֹ
	public void setStopSearch(boolean isStopSearch){
		this.isStopSearch=isStopSearch;
	}
	
	//��ʼ����ر���(��ÿ�����¿�ʼ����ǰ,��searchSDCardFile())---------------
	private void initData(){
		isStopSearch=false;//�Ƿ�ֹͣ����
		anyFileSize=0;
		txtFileSize = 0;
		videoFileSize = 0;
		audioFileSize = 0;
		imageFileSize = 0;
		zipFileSize = 0;
		apkFileSize = 0;		
		anyFileList.clear();//��ռ���������Ԫ��
		txtFileList.clear();
		videoFileList.clear();
		audioFileList.clear();
		imageFileList.clear();
		zipFileList.clear();
		apkFileList.clear();
	}
	// �����洢��(���ú�����)Ŀ¼�µ������ļ�,���ʵʱ������anyFileList��
	public void searchCardFile(){
		if(anyFileList==null||anyFileList.size()<=0){
			initData();//��ʼ������
			searchFile(inSdcardDir,false);//��ʾ����SDcard���������ݶ��������
			/*true��ʾ��һ�ν���searchFile����ʱ��isFirstRunFlag��true����184�еݹ�
			 * �ٴε���searchFile����ʱ��isFirstRunFlag����Ϊfalse���������һֱ������
			 * �������һ��һ���˳�ʱ����forѭ������������188�з�������������
			 */
			searchFile(outSdcardDir,true);
		}else{
			callbackSearchFileListenerEnd(false);//�����ݲ�Ϊ��ʱֱ�Ӽ���
		}
	}
	
//---------------------------------------------------------------------------	
	/*file ���û�������sdcard
	 * isFirstRunFlag  �ж��Ƿ����ÿһ��Ŀ¼
	 */
	private void searchFile(File file, boolean isFirstRunFlag) {
		//-----��ֹ����---
		if(isStopSearch){
			//���״����оͽ���������������
			if(isFirstRunFlag){
				// �ص��ӿ�end()����,��������,progressBar��ʧ����ͷ��ʾ��������ʾ
				callbackSearchFileListenerEnd(true);
			}
			return;
		}
		//#1 �ų������������ļ����ջ��߲��ɶ����߲����ڣ�
		if(file==null||!file.canRead()||!file.exists()){
			//����  �ص�����
			if(isFirstRunFlag){
				callbackSearchFileListenerEnd(true);
			}
			return;
		}
		//#2  ������ļ�����Ŀ¼��
		if(!file.isDirectory()){
			//�ж��ļ���С
			if(file.length()<=0){
				return;
			}
			//����ļ�������û��"."������׺�� ��δ֪�ļ�����
			if(file.getName().lastIndexOf(".")==-1){
				return;
			}
			//��ȡͼ�����Ƽ��ļ�
			String[] iconAndTypeNames=FileTypeUtil.getFileIconAndTypeName(file);
			final String iconName=iconAndTypeNames[0];//ͼ������
			final String typeName=iconAndTypeNames[1];//�ļ�����	
			
			//ʣ�������ļ�����ӵ������ļ��ļ���
			FileInfo fileInfo=new FileInfo(file,typeName, iconName);
			//����õ�file���ŵ�fileInfo������
			anyFileList.add(fileInfo);
			//��ȡ�ļ��Ĵ�С�����Ӽ������ļ���С
			anyFileSize +=file.length();
			//����
			if (typeName.equals(FileTypeUtil.TYPE_APK)) {//���ļ���׺����spk
				apkFileSize += file.length();//�ļ���С����
				apkFileList.add(fileInfo);//���ļ��ŵ�apk������
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
			
			//�ص��ӿ�searching����(ʵʱ��������)��ֻ�������ݣ�progressBar����ʧ
			callbackSearchFileListenerSearching(typeName);
			return;
		}
		//#3 �����Ŀ¼
		File[] files=file.listFiles();//�õ���Ŀ¼����������
		//�����Ŀ¼��û���κ�Ԫ����ֱ��return
		if(files==null||files.length<=0){
			return;
		}
		for(int num=0;num<files.length;num++){
			//������Ŀ¼���������ݣ��ļ���Ŀ¼	��
			File tmpFile=files[num];
			searchFile(tmpFile, false);//�ݹ飬�ٴε���searchFile�����ж�tmpFile���ļ�����Ŀ¼
		}
		//һ��һ���������֮���˳�������callbackSearchFileListenerEnd����
		if(isFirstRunFlag){
			callbackSearchFileListenerEnd(false);
		}
	}
	
//�ص�����----------------------------------------------------------------
	//�������̼���
//�ӿڣ�����OnClickListener��	
	public interface SearchFileListener{
		void searching(String typeName);//��������
		//���ô˷���ʱ��ʵ���ǵ���FilemgrActivity�е�end��������end��û���õ�Booleanֵ��
		//����ûӰ��,����ֱ��д��void end()
		void end(boolean isExceptionEnd);//��������
	}
	//�����ļ����Ҽ���
	//����Button�ļ���
	private SearchFileListener listener;//�������̼���
	public void setSearchListener(SearchFileListener listener){
		this.listener=listener;
	}
	//�����ص�SearchFileListener�ӿ��ڷ���searching(String typeName)
	//�������ڼ���(�൱��go����)
	private void callbackSearchFileListenerSearching(String typeName) {//�൱��doClick()����
		if (listener != null) {
			listener.searching(typeName);//�൱��onClick()����
		}
	}
	//�����ص�SearchFileListener�ӿ��ڷ���end(boolean isExceptionEnd)
	//���ݼ��ؽ��������������ڼ���ʱ��ʱ�˳�(�൱��go����)
	private void callbackSearchFileListenerEnd(boolean isExceptionEnd) {
		if (listener != null) {
			listener.end(isExceptionEnd);
		}
	}
	
//setters��getters--------------------------------------------------------	
	public ArrayList<FileInfo> getAnyFileList() {
		return anyFileList;
	}

	//��ȡ�ĵ��ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯)
	public ArrayList<FileInfo> getTextFileList() {
		return txtFileList;
	}

	//��ȡ��Ƶ�ļ��ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public ArrayList<FileInfo> getVideoFileList() {
		return videoFileList;
	}

	// ��ȡ�����ļ��ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯)
	public ArrayList<FileInfo> getAudioFileList() {
		return audioFileList;
	}

	// ��ȡͼ���ļ��ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public ArrayList<FileInfo> getImageFileList() {
		return imageFileList;
	}

	//��ȡAPK�ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯)
	public ArrayList<FileInfo> getApkFileList() {
		return apkFileList;
	}

	// ��ȡzip�ļ��б�(����������searchSDCardFile()ʵʱ�ڱ仯)
	public ArrayList<FileInfo> getZipFileList() {
		return zipFileList;
	}

	// ��ȡAPK�ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public long getApkFileSize() {
		return apkFileSize;
	}

	// ��ȡzip�ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public long getZipFileSize() {
		return zipFileSize;
	}

	// ��ȡ���������ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯)
	public long getAnyFileSize() {
		return anyFileSize;
	}

	//��ȡ�ı��ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯)
	public long getTxtFileSize() {
		return txtFileSize;
	}

	//��ȡ��Ƶ�ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public long getVideoFileSize() {
		return videoFileSize;
	}

	//��ȡ��Ƶ�ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯) 
	public long getAudioFileSize() {
		return audioFileSize;
	}

	//��ȡͼ���ļ���ǰ�ܴ�С(����������searchSDCardFile()ʵʱ�ڱ仯)
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
