package edu.feicui.app.phone.biz;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;
//���������ڴ�
public class MemoryManager {
	//��ȡ����sdcard��ַ
	public static String getPhoneInSDCardPath(){
		//mnt/shell/emulated/0
		String sdcardState=Environment.getExternalStorageState();
		if(!sdcardState.equals(Environment.MEDIA_MOUNTED)){
			return null;
		}
		Log.d("TAG","path:"+Environment.getExternalStorageDirectory().getAbsolutePath());
		return Environment.getExternalStorageDirectory().getAbsolutePath();		
	}
	//��ȡ�ֻ�����sdcard·��,�������ֻ����ԣ���Ϊҹ���Դ����ƶ���sdcard
	public static String getPhoneOutSDCardPath(){//����ֻ����ƶ�sdcard
		Map<String,String> map=System.getenv();//���Ի�ȡһЩĿ¼�Ĵ洢����������sdcard1��
		Log.d("TAG", "System.getenv():"+map);
		if(map.containsKey("SECONDARY_STORAGE")){//SECONDARY_STORAGE:storage/sdcard1
			String paths=map.get("SECONDARY_STORAGE");
			String[] path=paths.split(":");
			if(path==null||path.length<=0){
				return null;
			}
			Log.d("TAG", "path[0]:"+path[0]);
			return path[0];
		}
		return null;		
	}
	//�豸���ô洢SDCardȫ����С ��λB,��û������sdcard����СΪ0
	public static long getPhoneOutSDCardSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());
			Log.d("TAG","path:"+path);
			if (path == null) {
				return 0;
			}
			//StatFs���ڻ�ȡ�ļ�״̬��������ֻ��Ļ�������storage/sdcard1,�����ҹ����Ϊstorage/sdcard0��
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long blockCount = stat.getBlockCount();
			return blockCount * blockSize;//��ô��ļ�����������λB		
		} catch (Exception e) {
			Toast.makeText(context, "���ô洢���쳣", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	//�豸���ô洢SDCard���д�С ��λB
	public static long getPhoneOutSDCardFreeSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());//catchĿ¼
			Log.d("TAG","path:"+path);
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availaBlock = stat.getAvailableBlocks();
			return availaBlock * blockSize;
		} catch (Exception e) {
			Toast.makeText(context, "���ô洢���쳣", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	//�豸����洢ȫ����С ��λB(������data)
	public static long getPhoneSelfSize() {
		File path = Environment.getRootDirectory();//��Ŀ¼
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;//����

		path = Environment.getDownloadCacheDirectory();//catchĿ¼
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}
	// �豸����洢���д�С ��λB 
	public static long getPhoneSelfFreeSize() {
		File path = Environment.getRootDirectory();
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();//����
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}
	//�豸���ô洢��ȫ����С(�ֻ��Դ�32G�洢�ռ�?) ��λB 
	public static long getPhoneSelfSDCardSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();//mnt
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		return blockCount * blockSize;
	}
	//�豸���ô洢�������С(�ֻ��Դ�32G�洢�ռ�?) ��λB 
	public static long getPhoneSelfSDCardFreeSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();		
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availaBlock = stat.getAvailableBlocks();//���ؿ��ÿ�
		return availaBlock * blockSize;//���ؿ�������
	}
	
	//��ȡ�ֻ��ܴ洢��С(����data)
	public static long getPhoneAllSize() {
		File path = Environment.getRootDirectory();// /system
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();// /data(ֻ���ڲ��洢�ռ䣬������ʲô���û�������)
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();//  /catch
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}
	///��ȡ�ֻ������ô洢��С
	public static long getPhoneAllFreeSize() {
		File path = Environment.getRootDirectory();
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}
	
	//�豸���������ڴ棬��λB
	public static long getPhoneTotalRamMemory(){//ͨ���˷����������е������ڴ�
		//meminfo���ļ���¼��Android�ֻ���һЩ�ڴ���Ϣ
			try {
				FileReader fr=new FileReader("/proc/meminfo");
				BufferedReader br=new BufferedReader(fr);
				String text=br.readLine();
				//MemTotal 234567 KB-->[MemTotal,234567,KB]
				//���ݿ��ַ��������
				String[] array=text.split("\\s+");
				//meminfo�ļ���Ĭ�ϵ�λ��KB
				//Long.valueOf ��String����ת����long����
				Log.d("TAG", "array: "+Arrays.toString(array));
				return Long.valueOf(array[1])*1024;//ԭΪkb��ת����b
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//�˷�Χ���ò���meminfo�ļ��е���Ϣ�����Է���0
		return 0;		
	}
	//����豸���������ڴ棬��λB
	public static long getPhoneFreeRamMemory(Context context){
		MemoryInfo info=new MemoryInfo();
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(info);
		Log.i("TAG", "info.availMem:"+info.availMem);
		return info.availMem;//MemFree ��ϵͳ����δʹ�õ��ڴ�	
	}
}
