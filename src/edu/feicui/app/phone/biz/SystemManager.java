package edu.feicui.app.phone.biz;
import android.os.Build;
public class SystemManager {
	//获取需要的手机硬件信息（手机型号，生产商名称等）
	//设备品牌
	public static String getPhoneName(){
		return Build.BRAND;
	}
	//设备型号名称（例如L55u）
	public static String getPhoneModelName(){
		//获得手机型号和版本号
		return Build.MODEL+" Android "+getPhoneSystemVersion();		
	}
	//设备系统版本号（4.4.2）
	public static String getPhoneSystemVersion(){
		//获得系统版本字符串
		return Build.VERSION.RELEASE;		
	}
}
