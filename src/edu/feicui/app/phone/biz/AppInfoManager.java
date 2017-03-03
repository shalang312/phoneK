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
//Ӧ�ó��������
public class AppInfoManager {	
	private Context context;
	//�������ڰ�װж��Ӧ�ã���ѯ��װӦ����Ϣ������û����ݼ�����
	private PackageManager packageManager;
	//��װ�����ڽ��е��������Ϣ
	private ActivityManager activityManager;
	//�����������
	private List<AppInfo> allPackageInfos=new ArrayList<AppInfo>();
	//����ϵͳ���
	private List<AppInfo> systemPackageInfos=new ArrayList<AppInfo>();
	//�����û����
	private List<AppInfo> userPackageInfos=new ArrayList<AppInfo>();
	//�Գ�Ա�������г�ʼ��
	public AppInfoManager(Context context) {
		this.context=context;
		//���packageManager����
		packageManager=context.getPackageManager();
		//ϵͳ������ࣨ��layoutinflate���ƣ�
		activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}
	public AppInfoManager() {
		// TODO Auto-generated constructor stub
	}
	//��̬ģʽ������ģʽ��
	//static���εĳ�Ա�����ڷ�����������ֻ��һ��
	//���ڷ��ر����Ψһ����(���ٴ�������Ĵ������൱���Ż�)
	public static AppInfoManager appManager=null;
	public static AppInfoManager getAppInfoManager(Context context){
		if(appManager==null){
			//��ֹ�߳�֮�����
			synchronized (context) {
				appManager=new AppInfoManager(context);
			}
		}
		return appManager;		
	}
	//���������������������ActivityӦ�ó������
	public void loadAllActivityPackage(){
		//PackageInfo�ײ�洢��װ����Ϣ����
		//�������Ӧ�ó���ÿ��Ӧ�ó���ΪPackageInfo����
		//Installedװ�ع���,Ȼ���װ��PackageInfo����
		// |��1Ϊ1
		List<PackageInfo> infos=packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES|PackageManager.GET_UNINSTALLED_PACKAGES);
		Log.i("TAG", "infos:"+infos);
		allPackageInfos.clear();
		for(PackageInfo packageInfo:infos){
			//���ֻ����õ�ÿһ��װ�ع���Ӧ�ó��򣬷ŵ�allPackageInfos��
			//���ǰ����ݷ�װ�������У�Ȼ�󽫶���ŵ�������
			allPackageInfos.add(new AppInfo(packageInfo));
		}
	}
	//��������Ӧ�ó����б�
	public List<AppInfo> getAllPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
		}
		return allPackageInfos;	//�����������
	}
	public List<AppInfo> getSystemPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
			systemPackageInfos.clear();
			for(AppInfo appInfo:allPackageInfos){
				//��������������0ʱ����˧ѡ�Ķ�Ϊϵͳ���
				if((appInfo.getPackageInfo().applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
					//��ÿһ������������ϵͳ�������systemPackageInfos������
					systemPackageInfos.add(appInfo);
				}
			}
		}
		return systemPackageInfos;	//����ϵͳ���	
	}
	public List<AppInfo> getUserPackageInfo(boolean isReset){
		if(isReset){
			loadAllActivityPackage();
			userPackageInfos.clear();
			for(AppInfo appInfo:allPackageInfos){
				if((appInfo.getPackageInfo().applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){				
				}else{
					//��������0����ʾΪ�û�����������������û�������Ԫ�أ���ӵ�userPackageInfos������
					userPackageInfos.add(appInfo);
				}
			}
		}
		return userPackageInfos;//�����û����		
	}
	public static final int RUNNING_APP_TYPE_SYS=0;
	public static final int RUNNING_APP_TYPE_USER=1;
	//��ȡ�������е�Ӧ��
	//Integer��Ӧ������ĳ���״̬
	//List<RunningAppInfo>��Ӧ����ÿһ����������Ӧ�ó������Ϣ
	public Map<Integer,List<RunningAppInfo>> getRunningAppInfos(){
		Map<Integer,List<RunningAppInfo>> runningAppInfos=new HashMap<Integer, List<RunningAppInfo>>();
		//ϵͳ�������
		List<RunningAppInfo> sysapp=new ArrayList<RunningAppInfo>();
		//�û��������
		List<RunningAppInfo> userapp=new ArrayList<RunningAppInfo>();
		//��ȡ�����������е�Ӧ�ã������������������ģ�
		//RunningAppProcessInfo�ײ��װ�������е�Ӧ�ó������
		List<RunningAppProcessInfo> appProcessInfos=activityManager.getRunningAppProcesses();
		//�������н������
		for(RunningAppProcessInfo appProcessInfo:appProcessInfos){
			//��ȡ�������г���Ľ�����
			String packageName=appProcessInfo.processName;
			//����������г�����̵�ID
			int pid=appProcessInfo.pid;
			//����������г�����̵����ȼ�
			int importance=appProcessInfo.importance;	
			//�ս���>��̨����(400)>�������(300)>�ɼ�����(200)>ǰ̨����(100)
			if(importance>=RunningAppProcessInfo.IMPORTANCE_SERVICE){
				Drawable icon;//���н���ͼ��
				String labelName;//���г�������
				long size;//���г�����ռ�ڴ�
				//�������н���ID��ȡ���г���
				Debug.MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[]{pid});
				size=(memoryInfos[0].getTotalPrivateDirty())*1024;//��ȡ�����ڴ�,��KBת����B
				try {
					//�൱����AndroidManifest�в�����
					icon=packageManager.getApplicationIcon(packageName);
					//flags����ֱ��д0
					ApplicationInfo applicationInfo=packageManager.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA|PackageManager.GET_SHARED_LIBRARY_FILES|
							PackageManager.GET_UNINSTALLED_PACKAGES);
					labelName=packageManager.getApplicationLabel(applicationInfo).toString();
					//�����ݷ�������д洢
					RunningAppInfo runningAppInfo=new RunningAppInfo(packageName, icon, labelName, size);
					//������0��ʾ��ϵͳ����
					if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
						runningAppInfo.setSystem(true);
						runningAppInfo.setClear(false);
						//���Ӧ�ó�����ϵͳ���̣��ͷŵ�ϵͳ���̼�����
						sysapp.add(runningAppInfo);
					}else{
						runningAppInfo.setSystem(false);
						//Ĭ���������û����̣�ϵͳ������Ҫ�Լ���ѡ
						runningAppInfo.setClear(true);
						userapp.add(runningAppInfo);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//��ϵͳ���̼��Ϻ��û����̼������ݴ洢��runningAppInfos���Map������
		runningAppInfos.put(RUNNING_APP_TYPE_SYS, sysapp);//key=0;valueΪϵͳ���̼���
		runningAppInfos.put(RUNNING_APP_TYPE_USER, userapp);
		return runningAppInfos;	//������װ�õ�����	
	}
	//����Ӧ�ó���������ָ������
	public void killProgress(String packageName){
		activityManager.killBackgroundProcesses(packageName);
	}
	//���������������е��û����򣨼���Ϊ����������ϵķ�ϵͳ���̣�
	public void killAllProgresses(){
		List<RunningAppProcessInfo> appProcessInfos=activityManager.getRunningAppProcesses();
		//���������������еĽ���
		for(RunningAppProcessInfo appProcessInfo:appProcessInfos){
			//�ս��̡���̨���̡�������̡����ӽ��̡�ǰ̨����
			if(appProcessInfo.importance>=RunningAppProcessInfo.IMPORTANCE_SERVICE){
				String packageName=appProcessInfo.processName;//��ȡ�������
				try {
					ApplicationInfo applicationInfo=packageManager.getApplicationInfo(packageName, 
							PackageManager.GET_META_DATA|PackageManager.GET_SHARED_LIBRARY_FILES);
					if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){//��=0Ϊϵͳ����						
					}else{//�û�����
						activityManager.killBackgroundProcesses(packageName);//��ͨ��������õ��û�����ɱ��
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
