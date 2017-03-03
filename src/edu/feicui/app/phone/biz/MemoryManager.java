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
//操作运行内存
public class MemoryManager {
	//获取内置sdcard地址
	public static String getPhoneInSDCardPath(){
		//mnt/shell/emulated/0
		String sdcardState=Environment.getExternalStorageState();
		if(!sdcardState.equals(Environment.MEDIA_MOUNTED)){
			return null;
		}
		Log.d("TAG","path:"+Environment.getExternalStorageDirectory().getAbsolutePath());
		return Environment.getExternalStorageDirectory().getAbsolutePath();		
	}
	//获取手机外置sdcard路径,必须用手机测试，因为夜神不自带可移动的sdcard
	public static String getPhoneOutSDCardPath(){//获得手机的移动sdcard
		Map<String,String> map=System.getenv();//可以获取一些目录的存储（例如外置sdcard1）
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
	//设备外置存储SDCard全部大小 单位B,当没有外置sdcard，大小为0
	public static long getPhoneOutSDCardSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());
			Log.d("TAG","path:"+path);
			if (path == null) {
				return 0;
			}
			//StatFs用于获取文件状态（如果是手机的话，就是storage/sdcard1,如果是夜神，则为storage/sdcard0）
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long blockCount = stat.getBlockCount();
			return blockCount * blockSize;//获得此文件的容量，单位B		
		} catch (Exception e) {
			Toast.makeText(context, "外置存储卡异常", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	//设备外置存储SDCard空闲大小 单位B
	public static long getPhoneOutSDCardFreeSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());//catch目录
			Log.d("TAG","path:"+path);
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availaBlock = stat.getAvailableBlocks();
			return availaBlock * blockSize;
		} catch (Exception e) {
			Toast.makeText(context, "外置存储卡异常", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	//设备自身存储全部大小 单位B(不包含data)
	public static long getPhoneSelfSize() {
		File path = Environment.getRootDirectory();//根目录
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;//容量

		path = Environment.getDownloadCacheDirectory();//catch目录
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}
	// 设备自身存储空闲大小 单位B 
	public static long getPhoneSelfFreeSize() {
		File path = Environment.getRootDirectory();
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();//可用
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		Log.d("TAG","path:"+path);
		
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}
	//设备内置存储卡全部大小(手机自带32G存储空间?) 单位B 
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
	//设备内置存储卡空余大小(手机自带32G存储空间?) 单位B 
	public static long getPhoneSelfSDCardFreeSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();		
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availaBlock = stat.getAvailableBlocks();//返回可用块
		return availaBlock * blockSize;//返回可用容量
	}
	
	//获取手机总存储大小(包含data)
	public static long getPhoneAllSize() {
		File path = Environment.getRootDirectory();// /system
		Log.d("TAG","path:"+path);
		
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();// /data(只是内部存储空间，不存在什么内置或者外置)
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
	///获取手机总闲置存储大小
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
	
	//设备完整运行内存，单位B
	public static long getPhoneTotalRamMemory(){//通过此方法返回所有的运行内存
		//meminfo此文件记录了Android手机的一些内存信息
			try {
				FileReader fr=new FileReader("/proc/meminfo");
				BufferedReader br=new BufferedReader(fr);
				String text=br.readLine();
				//MemTotal 234567 KB-->[MemTotal,234567,KB]
				//根据空字符串来拆分
				String[] array=text.split("\\s+");
				//meminfo文件中默认单位是KB
				//Long.valueOf 将String类型转换成long类型
				Log.d("TAG", "array: "+Arrays.toString(array));
				return Long.valueOf(array[1])*1024;//原为kb，转换成b
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//此范围调用不到meminfo文件中的信息，所以返回0
		return 0;		
	}
	//获得设备空闲运行内存，单位B
	public static long getPhoneFreeRamMemory(Context context){
		MemoryInfo info=new MemoryInfo();
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(info);
		Log.i("TAG", "info.availMem:"+info.availMem);
		return info.availMem;//MemFree 被系统留着未使用的内存	
	}
}
