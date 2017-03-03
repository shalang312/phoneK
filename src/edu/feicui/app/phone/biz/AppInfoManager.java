package edu.feicui.app.phone.biz;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.feicui.app.phone.entity.AppInfo;
import edu.feicui.app.phone.entity.RunningAppInfo;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;
//应用程序管理类
public class AppInfoManager {	
	private Context context;
	//此类用于安装卸载应用，查询安装应用信息，清除用户数据及缓存
	private PackageManager packageManager;
	//封装了正在进行的想进程信息
	private ActivityManager activityManager;
	//保存所有软件
	private List<AppInfo> allPackageInfos=new ArrayList<AppInfo>();
	//保存系统软件
	private List<AppInfo> systemPackageInfos=new ArrayList<AppInfo>();
	//保存用户软件
	private List<AppInfo> userPackageInfos=new ArrayList<AppInfo>();
	//对成员变量进行初始化
	public AppInfoManager(Context context) {
		this.context=context;
		//获得packageManager对象
		packageManager=context.getPackageManager();
		//系统级别的类（和layoutinflate类似）
		activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}
	public AppInfoManager() {
		// TODO Auto-generated constructor stub
	}
	//单态模式（单例模式）
	//static修饰的成员变量在方法区，而且只有一份
	//用于返回本类的唯一对象(减少创建对象的次数，相当于优化)
	public static AppInfoManager appManager=null;
	public static AppInfoManager getAppInfoManager(Context context){
		if(appManager==null){
			//防止线程之间干扰
			synchronized (context) {
				appManager=new AppInfoManager(context);
			}
		}
		return appManager;		
	}
	//加载所有软件（加载所有Activity应用程序包）
	public void loadAllActivityPackage(){
		//PackageInfo底层存储安装包信息的类
		//获得所有应用程序，每个应用程序都为PackageInfo类型
		//Installed装载过的,然后封装成PackageInfo类型
		// |遇1为1
		List<PackageInfo> infos=packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES|PackageManager.GET_UNINSTALLED_PACKAGES);
		Log.i("TAG", "infos:"+infos);
		allPackageInfos.clear();
		for(PackageInfo packageInfo:infos){
			//从手机里拿到每一个装载过的应用程序，放到allPackageInfos中
			//就是把数据封装到对象中，然后将对象放到集合中
			allPackageInfos.add(new AppInfo(packageInfo));
		}
	}
	//返回所有应用程序列表
	public List<AppInfo> getAllPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
		}
		return allPackageInfos;	//返回所有软件
	}
	public List<AppInfo> getSystemPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
			systemPackageInfos.clear();
			for(AppInfo appInfo:allPackageInfos){
				//当此条件不等于0时，所帅选的都为系统软件
				if((appInfo.getPackageInfo().applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
					//将每一个符合条件的系统软件放入systemPackageInfos集合中
					systemPackageInfos.add(appInfo);
				}
			}
		}
		return systemPackageInfos;	//返回系统软件	
	}
	public List<AppInfo> getUserPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
			userPackageInfos.clear();
			for(AppInfo appInfo:allPackageInfos){
				if((appInfo.getPackageInfo().applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){				
				}else{
					//条件等于0，表示为用户软件，将所有满足用户条件的元素，添加到userPackageInfos集合中
					userPackageInfos.add(appInfo);
				}
			}
		}
		return userPackageInfos;//返回用户软件		
	}
	public static final int RUNNING_APP_TYPE_SYS=0;
	public static final int RUNNING_APP_TYPE_USER=1;
	//获取正在运行的应用
	//Integer对应的上面的常量状态
	//List<RunningAppInfo>对应的是每一个正在运行应用程序的信息
	public Map<Integer,List<RunningAppInfo>> getRunningAppInfos(){
		Map<Integer,List<RunningAppInfo>> runningAppInfos=new HashMap<Integer, List<RunningAppInfo>>();
		//系统进程软件
		List<RunningAppInfo> sysapp=new ArrayList<RunningAppInfo>();
		//用户进程软件
		List<RunningAppInfo> userapp=new ArrayList<RunningAppInfo>();
		//获取所有正在运行的应用（包括看见，看不见的）
		//RunningAppProcessInfo底层封装正在运行的应用程序的类
		List<RunningAppProcessInfo> appProcessInfos=activityManager.getRunningAppProcesses();
		//遍历所有进程软件
		for(RunningAppProcessInfo appProcessInfo:appProcessInfos){
			//获取正在运行程序的进程名
			String packageName=appProcessInfo.processName;
			//获得正在运行程序进程的ID
			int pid=appProcessInfo.pid;
			//获得正在运行程序进程的优先级
			int importance=appProcessInfo.importance;	
			//空进程>后台进程(400)>服务进程(300)>可见进程(200)>前台进程(100)
			if(importance>=RunningAppProcessInfo.IMPORTANCE_SERVICE){
				Drawable icon;//运行进程图标
				String labelName;//运行程序名称
				long size;//运行程序所占内存
				//利用运行进程ID获取运行程序
				Debug.MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[]{pid});
				size=(memoryInfos[0].getTotalPrivateDirty())*1024;//获取进程内存,从KB转换成B
				try {
					//相当于在AndroidManifest中操作的
					icon=packageManager.getApplicationIcon(packageName);
					//flags可以直接写0
					ApplicationInfo applicationInfo=packageManager.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA|PackageManager.GET_SHARED_LIBRARY_FILES|
							PackageManager.GET_UNINSTALLED_PACKAGES);
					labelName=packageManager.getApplicationLabel(applicationInfo).toString();
					//将数据放入对象中存储
					RunningAppInfo runningAppInfo=new RunningAppInfo(packageName, icon, labelName, size);
					//不等于0表示是系统进程
					if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
						runningAppInfo.setSystem(true);
						runningAppInfo.setClear(false);
						//如果应用程序是系统进程，就放到系统进程集合中
						sysapp.add(runningAppInfo);
					}else{
						runningAppInfo.setSystem(false);
						//默认先清理用户进程，系统进程需要自己勾选
						runningAppInfo.setClear(true);
						userapp.add(runningAppInfo);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//将系统进程集合和用户进程集合数据存储到runningAppInfos这个Map集合中
		runningAppInfos.put(RUNNING_APP_TYPE_SYS, sysapp);//key=0;value为系统进程集合
		runningAppInfos.put(RUNNING_APP_TYPE_USER, userapp);
		return runningAppInfos;	//返回组装好的数据	
	}
	//根据应用程序包名清除指定程序
	public void killProgress(String packageName){
		activityManager.killBackgroundProcesses(packageName);
	}
	//清理所有正在运行的用户程序（级别为服务进程以上的非系统进程）
	public void killAllProgresses(){
		List<RunningAppProcessInfo> appProcessInfos=activityManager.getRunningAppProcesses();
		//遍历所有正在运行的进程
		for(RunningAppProcessInfo appProcessInfo:appProcessInfos){
			//空进程》后台进程》服务进程》可视进程》前台进程
			if(appProcessInfo.importance>=RunningAppProcessInfo.IMPORTANCE_SERVICE){
				String packageName=appProcessInfo.processName;//获取程序包名
				try {
					ApplicationInfo applicationInfo=packageManager.getApplicationInfo(packageName, 
							PackageManager.GET_META_DATA|PackageManager.GET_SHARED_LIBRARY_FILES);
					if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){//！=0为系统进程						
					}else{//用户进程
						activityManager.killBackgroundProcesses(packageName);//把通过包名获得的用户进程杀死
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
