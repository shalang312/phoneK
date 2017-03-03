package edu.feicui.app.phone.base.util;

import android.util.Log;

/**
 * ��־��������(��϶ȸ�,�иĽ��ռ�)
 * 
 * @author yuanc
 * 
 */
public class LogUtil {

	public static boolean isOpenDebug = true;
	public static boolean isOpenWarn = true;

	public static void d(String tag, String msg) {
		if (isOpenDebug) {
			Log.d(tag, msg);
		}
	}
	public static void w(String tag, String msg) {
		if (isOpenWarn) {
			Log.w(tag, msg);
		}
	}
}